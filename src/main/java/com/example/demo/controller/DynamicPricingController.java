package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    @PostMapping("/calculate/{eventId}")
    public DynamicPriceRecord calculate(@PathVariable Long eventId) {
        return service.calculateDynamicPrice(eventId);
    }

    @GetMapping("/latest/{eventId}")
    public DynamicPriceRecord getLatest(@PathVariable Long eventId) {
        return service.getLatestPrice(eventId)
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }

    @GetMapping("/all")
    public List<DynamicPriceRecord> getAll() {
        return service.getAllComputedPrices();
    }

    @GetMapping("/history/{eventId}")
    public List<DynamicPriceRecord> getHistory(@PathVariable Long eventId) {
        return service.getPriceHistory(eventId);
    }
}
