package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pricing")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    // calculate price
    @PostMapping("/calculate/{eventId}")
    public DynamicPriceRecord calculate(@PathVariable Long eventId) {
        return service.calculateDynamicPrice(eventId);
    }

    // latest price
    @GetMapping("/latest/{eventId}")
    public Optional<DynamicPriceRecord> latest(@PathVariable Long eventId) {
        return service.getLatestPrice(eventId);
    }

    // all prices
    @GetMapping("/all")
    public List<DynamicPriceRecord> all() {
        return service.getAllComputedPrices();
    }
}
