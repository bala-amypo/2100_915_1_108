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

    // ✅ Compute new dynamic price
    @PostMapping("/compute/{eventId}")
    public ResponseEntity<DynamicPriceRecord> computePrice(
            @PathVariable Long eventId) {

        DynamicPriceRecord price =
                service.computeDynamicPrice(eventId);

        return ResponseEntity.ok(price);
    }

    // ✅ Get latest price (FIXED TYPE)
    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> getLatestPrice(
            @PathVariable Long eventId) {

        DynamicPriceRecord price =
                service.getLatestPrice(eventId);

        return ResponseEntity.ok(price);
    }

    // ✅ Get price history
    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> getPriceHistory(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                service.getPriceHistory(eventId));
    }

    // ✅ Get all computed prices
    @GetMapping("/all")
    public ResponseEntity<List<DynamicPriceRecord>> getAllPrices() {

        return ResponseEntity.ok(
                service.getAllComputedPrices());
    }
}
