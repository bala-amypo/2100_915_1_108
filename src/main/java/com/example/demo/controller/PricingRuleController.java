package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-rules")
@Tag(name = "Pricing Rules")
public class PricingRuleController {

    private final PricingRuleService service;

    public PricingRuleController(PricingRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PricingRule> create(@RequestBody PricingRule rule) {
        return ResponseEntity.ok(service.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingRule> update(
            @PathVariable Long id,
            @RequestBody PricingRule rule) {
        return ResponseEntity.ok(service.updateRule(id, rule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PricingRule>> active() {
        return ResponseEntity.ok(service.getActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PricingRule> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRuleById(id));
    }

    @GetMapping
    public ResponseEntity<List<PricingRule>> all() {
        return ResponseEntity.ok(service.getAllRules());
    }
}
