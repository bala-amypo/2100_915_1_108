package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dynamic-pricing")
@Tag(name = "Dynamic Pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    @PostMapping("/compute/{eventId}")
    public ResponseEntity<DynamicPriceRecord> compute(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                service.computeDynamicPrice(eventId)
        );
    }

    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> latest(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                service.getLatestPrice(eventId)
        );
    }

    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> history(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                service.getPriceHistory(eventId)
        );
    }

    @GetMapping
    public ResponseEntity<List<DynamicPriceRecord>> all() {
        return ResponseEntity.ok(service.getAllComputedPrices());
    }
}
