package com.example.demo.service;

import com.example.demo.model.PricingRule;

import java.util.List;
import java.util.Optional;

public interface PricingRuleService {

    PricingRule createRule(PricingRule rule);

    PricingRule updateRule(Long id, PricingRule rule);

    List<PricingRule> getActiveRules();

    Optional<PricingRule> getRuleByCode(String ruleCode);

    List<PricingRule> getAllRules();
}
