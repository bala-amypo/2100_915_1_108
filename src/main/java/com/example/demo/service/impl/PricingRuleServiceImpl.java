package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.service.PricingRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingRuleServiceImpl implements PricingRuleService {

    private final PricingRuleRepository repository;

    public PricingRuleServiceImpl(PricingRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public PricingRule createRule(PricingRule rule) {

        if (repository.existsByRuleCode(rule.getRuleCode())) {
            throw new BadRequestException("Price multiplier must be > 0");
        }

        if (rule.getPriceMultiplier() == null || rule.getPriceMultiplier() <= 0) {
            throw new BadRequestException("Price multiplier must be > 0");
        }

        return repository.save(rule);
    }

    @Override
    public PricingRule updateRule(Long id, PricingRule updatedRule) {
        updatedRule.setId(id);
        return repository.save(updatedRule);
    }

    @Override
    public List<PricingRule> getActiveRules() {
        return repository.findByActiveTrue();
    }

    @Override
    public Optional<PricingRule> getRuleByCode(String ruleCode) {
        return repository.findAll()
                .stream()
                .filter(r -> ruleCode.equals(r.getRuleCode()))
                .findFirst();
    }

    @Override
    public List<PricingRule> getAllRules() {
        return repository.findAll();
    }
}
