package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;

import java.util.List;
import java.util.Optional;

public interface DynamicPricingEngineService {

    // NEW (for tests)
    DynamicPriceRecord computeDynamicPrice(Long eventId);

    // Existing
    DynamicPriceRecord calculateDynamicPrice(Long eventId);

    // NEW (for tests)
    List<DynamicPriceRecord> getPriceHistory(Long eventId);

    List<DynamicPriceRecord> getAllComputedPrices();

    Optional<DynamicPriceRecord> getLatestPrice(Long eventId);
}
