package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    @PostMapping("/compute/{eventId}")
    public ResponseEntity<DynamicPriceRecord> computePrice(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.computeDynamicPrice(eventId));
    }

    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> getLatestPrice(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getLatestPrice(eventId));
    }

    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> getPriceHistory(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getPriceHistory(eventId));
    }

    @GetMapping
    public ResponseEntity<List<DynamicPriceRecord>> getAllComputedPrices() {
        return ResponseEntity.ok(service.getAllComputedPrices());
    }
}
