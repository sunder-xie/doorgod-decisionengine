/*
 *
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.doorgod.decisionengine.model;

import java.util.ArrayList;
import java.util.List;

import com.ymatou.doorgod.decisionengine.util.DateUtils;

/**
 * Created by tuwenjie on 2016/9/9.
 */
public class StatisticItem {

    private String uri;

    private String sample;

    private Sample newSample;

    // 请求时间:请求接收时刻的毫秒数
    private String reqTime;

    // 耗时:以毫秒为单位
    private long consumedTime;

    // http响应码
    private int statusCode;

    private boolean rejectedByFilter;

    private boolean rejectedByHystrix;

    private String hitRule;

    private long filterConsumedTime;

    private int origStatusCode;

    private List<String> matchRules = new ArrayList<String>();

    public Sample getNewSample() {
        return newSample;
    }

    public String getSample() {
        return sample;
    }

    public void setNewSample(Sample newSample) {
        this.newSample = newSample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    /**
     * 获取到的是 yyyyMMddHHmmss格式
     * 
     * @return
     */
    public String getReqTime() {
        return DateUtils.formatFromTimeMillis(reqTime);
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getConsumedTime() {
        return consumedTime;
    }

    public void setConsumedTime(long consumedTime) {
        this.consumedTime = consumedTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isRejectedByFilter() {
        return rejectedByFilter;
    }

    public void setRejectedByFilter(boolean rejectedByFilter) {
        this.rejectedByFilter = rejectedByFilter;
    }

    public boolean isRejectedByHystrix() {
        return rejectedByHystrix;
    }

    public void setRejectedByHystrix(boolean rejectedByHystrix) {
        this.rejectedByHystrix = rejectedByHystrix;
    }

    public String getHitRule() {
        return hitRule;
    }

    public void setHitRule(String hitRule) {
        this.hitRule = hitRule;
    }

    public long getFilterConsumedTime() {
        return filterConsumedTime;
    }

    public void setFilterConsumedTime(long filterConsumedTime) {
        this.filterConsumedTime = filterConsumedTime;
    }

    public int getOrigStatusCode() {
        return origStatusCode;
    }

    public void setOrigStatusCode(int origStatusCode) {
        this.origStatusCode = origStatusCode;
    }

    public List<String> getMatchRules() {
        return matchRules;
    }

    public void setMatchRules(List<String> matchRules) {
        this.matchRules = matchRules;
    }
}
