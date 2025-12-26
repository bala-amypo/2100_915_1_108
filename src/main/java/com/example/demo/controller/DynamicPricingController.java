package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
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

    // ✅ TEST-COMPATIBLE METHOD
    @PostMapping("/compute/{eventId}")
    public DynamicPriceRecord computePrice(
            @PathVariable Long eventId) {

        return service.computeDynamicPrice(eventId);
    }

    // ✅ GET PRICE HISTORY (TEST EXPECTS THIS)
    @GetMapping("/history/{eventId}")
    public List<DynamicPriceRecord> getPriceHistory(
            @PathVariable Long eventId) {

        return service.getPriceHistory(eventId);
    }

    // ✅ GET LATEST PRICE
    @GetMapping("/latest/{eventId}")
    public Optional<DynamicPriceRecord> getLatestPrice(
            @PathVariable Long eventId) {

        return service.getLatestPrice(eventId);
    }
}
