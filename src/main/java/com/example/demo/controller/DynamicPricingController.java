package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    /**
     * Calculate and store dynamic price for an event
     */
    @PostMapping("/calculate/{eventId}")
    public ResponseEntity<DynamicPriceRecord> calculatePrice(
            @PathVariable Long eventId) {

        DynamicPriceRecord record =
                service.calculateDynamicPrice(eventId);

        return ResponseEntity.ok(record);
    }

    /**
     * Get latest price for an event
     */
    @GetMapping("/latest/{eventId}")
    public ResponseEntity<DynamicPriceRecord> getLatestPrice(
            @PathVariable Long eventId) {

        Optional<DynamicPriceRecord> record =
                service.getLatestPrice(eventId);

        return record
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all computed price records
     */
    @GetMapping("/all")
    public ResponseEntity<List<DynamicPriceRecord>> getAllPrices() {
        return ResponseEntity.ok(service.getAllComputedPrices());
    }
}
