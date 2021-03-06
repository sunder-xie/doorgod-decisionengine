/*
 *
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.doorgod.decisionengine.integration.store;

import com.ymatou.doorgod.decisionengine.holder.RuleHolder;
import com.ymatou.doorgod.decisionengine.holder.SampleStatisticCenter;
import com.ymatou.doorgod.decisionengine.model.LimitTimesRule;
import com.ymatou.doorgod.decisionengine.model.Sample;
import com.ymatou.doorgod.decisionengine.util.RedisHelper;
import com.ymatou.performancemonitorclient.PerformanceStatisticContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ymatou.doorgod.decisionengine.constants.Constants.PerformanceServiceEnum.REDIS_SAMPLE_STORE_PER_TIME;
import static com.ymatou.doorgod.decisionengine.constants.Constants.PerformanceServiceEnum.REDIS_SAMPLE_STORE_SINGLE;
import static com.ymatou.doorgod.decisionengine.util.Utils.getExpireByRule;

/**
 * @author luoshiqian 2016/9/14 16:01
 */
@Component("redisSampleStore")
public class RedisSampleStore extends AbstractSampleStore {

    private static final Logger logger = LoggerFactory.getLogger(RedisSampleStore.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Collection<LimitTimesRule> findRule() {
        return RuleHolder.limitTimesRules.values().stream()
                .filter(rule -> CollectionUtils.isEmpty(rule.getGroupByKeys())).collect(Collectors.toSet());
    }

    @Override
    protected Map<String, Map<String, Map<Sample, AtomicInteger>>> getMemoryMap() {
        return SampleStatisticCenter.ruleTimeSampleMaps;
    }

    @Override
    protected int getTopN() {
        return bizProps.getUploadRedisTopN();
    }

    @Override
    public void uploadSampleToDb(LimitTimesRule rule, String uploadTime,
            Collection<Map.Entry<Sample, Object>> samples) {

        PerformanceStatisticContainer.add(() -> {

            // 获取redis zset name
            String zSetName = RedisHelper.getNormalSetName(rule.getName(), uploadTime);

            samples.forEach(entry -> {
                try {
                    double score = ((AtomicInteger) entry.getValue()).doubleValue();
                    double newScore = PerformanceStatisticContainer.addWithReturn(() -> {

                        if (redisTemplate.opsForZSet().getOperations().hasKey(zSetName)) {
                            return redisTemplate.opsForZSet().incrementScore(zSetName, entry.getKey().toString(),
                                    score);
                        } else {
                            redisTemplate.opsForZSet().add(zSetName, entry.getKey().toString(),
                                    score);
                            redisTemplate.opsForZSet().getOperations().expire(zSetName, getExpireByRule(rule),
                                    TimeUnit.SECONDS);// 单位秒
                        }
                        return score;
                    }, REDIS_SAMPLE_STORE_SINGLE.name());


                    logger.debug("ruleName:{},zsetName:{},zsetsample:{},score:{}", rule.getName(),
                            zSetName, entry.getKey().toString(), newScore);
                } catch (Exception e) {
                    logger.error("uploadSample to redis error,zSetName:{}", zSetName, e);
                }
            });

        }, REDIS_SAMPLE_STORE_PER_TIME.name());

    }



}
