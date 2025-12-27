package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class PricingRuleController {

    private final PricingRuleService service;

    public PricingRuleController(PricingRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PricingRule> createRule(@RequestBody PricingRule rule) {
        return ResponseEntity.ok(service.createRule(rule));
    }

    @GetMapping
    public ResponseEntity<List<PricingRule>> getAllRules() {
        return ResponseEntity.ok(service.getAllRules());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PricingRule>> getActiveRules() {
        return ResponseEntity.ok(service.getActiveRules());
    }
}
