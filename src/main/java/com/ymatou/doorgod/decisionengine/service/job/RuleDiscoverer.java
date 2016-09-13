/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.doorgod.decisionengine.service.job;

import static com.ymatou.doorgod.decisionengine.constants.Constants.SEPARATOR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.doorgod.decisionengine.config.props.BizProps;
import com.ymatou.doorgod.decisionengine.holder.RuleHolder;
import com.ymatou.doorgod.decisionengine.model.LimitTimesRule;
import com.ymatou.doorgod.decisionengine.model.ScopeEnum;
import com.ymatou.doorgod.decisionengine.model.po.RulePo;
import com.ymatou.doorgod.decisionengine.repository.RuleRepository;
import com.ymatou.doorgod.decisionengine.service.SchedulerService;

/**
 * 
 * @author qianmin 2016年9月12日 上午11:03:49
 * 
 */
@Component
public class RuleDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(RuleDiscoverer.class);

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private BizProps bizProps;

    public void execute() {
        // 加载Redis定时同步数据到MongoDB任务(添加/修改)
        try {
            schedulerService.addJob(RulePersistence.class, "RedisToMongo", bizProps.getRulePersistenceCronExpression());
        } catch (SchedulerException e) {
            logger.error("add redis to mongo job failed.", e);
        }

        // 加载规则数据，更新规则统计的定时任务
        HashMap<String, LimitTimesRule> updatedRules = fecthRuleData();
        for (LimitTimesRule rule : updatedRules.values()) {
            try {
                String ruleName = rule.getName();
                switch (rule.getUpdateType()) {
                    case "add":
                        RuleHolder.rules.put(ruleName, rule);
                        schedulerService.addJob(RuleExecutor.class, ruleName, bizProps.getRuleExecutorCronExpression());
                        break;
                    case "delete":
                        RuleHolder.rules.remove(ruleName);
                        schedulerService.removeScheduler(ruleName);
                        break;
                    case "pause":
                        RuleHolder.rules.remove(ruleName);
                        schedulerService.pauseScheduler(ruleName);
                        break;
                    case "resume":
                        RuleHolder.rules.put(ruleName, rule);
                        schedulerService.resumeScheduler(ruleName);
                        break;
                    default:
                        logger.error("Rule UpdateType not supported.");
                        break;
                }
            } catch (SchedulerException e) {
                logger.error("update rule schduler failed.", e);
            }
        }
    }

    public HashMap<String, LimitTimesRule> fecthRuleData() {
        HashMap<String, LimitTimesRule> rules = new HashMap<>();
        List<RulePo> rulePos = ruleRepository.findAll();
        for (RulePo rulePo : rulePos) {
            LimitTimesRule rule = new LimitTimesRule();
            rule.setName(rulePo.getName());
            rule.setOrder(rulePo.getOrder());
            rule.setScope(ScopeEnum.valueOf(rulePo.getScope()));
            rule.setStatisticSpan(rulePo.getStatisticSpan());
            rule.setTimesCap(rulePo.getTimesCap());
            rule.setRejectionSpan(rulePo.getRejectionSpan());
            rule.setUpdateType(rule.getUpdateType());
            rule.setDimensionKeys(new HashSet<>(Arrays.asList(rulePo.getKeys().split(SEPARATOR))));
            rule.setGroupByKeys(new HashSet<>(Arrays.asList(rulePo.getGroupByKeys().split(SEPARATOR))));
            rule.setApplicableUris(new HashSet<>(Arrays.asList(rulePo.getUris().split(SEPARATOR))));
            rules.put(rulePo.getName(), rule);
        }

        return rules;
    }
}