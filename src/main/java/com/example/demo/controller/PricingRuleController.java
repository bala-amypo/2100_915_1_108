package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-rules")
public class PricingRuleController {

    private final PricingRuleService ruleService;

    public PricingRuleController(PricingRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<PricingRule> createRule(@RequestBody PricingRule rule) {
        return ResponseEntity.ok(ruleService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingRule> updateRule(@PathVariable Long id, @RequestBody PricingRule rule) {
        return ResponseEntity.ok(ruleService.updateRule(id, rule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PricingRule>> getActiveRules() {
        return ResponseEntity.ok(ruleService.getActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PricingRule> getRuleById(@PathVariable Long id) {
        return ResponseEntity.ok(ruleService.getAllRules().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow());
    }

    @GetMapping
    public ResponseEntity<List<PricingRule>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }
}
