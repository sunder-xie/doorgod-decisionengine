/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.doorgod.decisionengine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ymatou.doorgod.decisionengine.model.po.UriRulePo;

/**
 * 
 * @author qianmin 2016年9月12日 下午6:22:46
 * 
 */
@Repository
public interface UriRuleRepository extends JpaRepository<UriRulePo, Long> {

    List<UriRulePo> findByRuleId(Long ruleId);

}
