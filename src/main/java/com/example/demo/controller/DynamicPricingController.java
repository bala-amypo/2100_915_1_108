package com.example.demo.controller;

import com.example.demo.entity.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dynamic-pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService pricingService;

    public DynamicPricingController(DynamicPricingEngineService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/compute/{eventId}")
    public ResponseEntity<DynamicPriceRecord> computePrice(@PathVariable Long eventId) {
        return ResponseEntity.ok(pricingService.computeDynamicPrice(eventId));
    }

    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> getLatestPrice(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                pricingService.getLatestPrice(eventId).orElseThrow()
        );
    }

    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> getPriceHistory(@PathVariable Long eventId) {
        return ResponseEntity.ok(pricingService.getPriceHistory(eventId));
    }

    @GetMapping
    public ResponseEntity<List<DynamicPriceRecord>> getAllPrices() {
        return ResponseEntity.ok(pricingService.getAllComputedPrices());
    }
}
