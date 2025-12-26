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

    // ✅ Calculate dynamic price for an event
    @PostMapping("/calculate/{eventId}")
    public ResponseEntity<DynamicPriceRecord> calculatePrice(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                service.calculateDynamicPrice(eventId)
        );
    }

    // ✅ Get all computed prices
    @GetMapping("/all")
    public ResponseEntity<List<DynamicPriceRecord>> getAllPrices() {
        return ResponseEntity.ok(
                service.getAllComputedPrices()
        );
    }

    // ✅ Get price history for an event
    @GetMapping("/history/{eventId}")
    public ResponseEntity<List<DynamicPriceRecord>> getPriceHistory(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                service.getPriceHistory(eventId)
        );
    }
}
