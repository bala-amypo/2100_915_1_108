package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;

import java.util.List;
import java.util.Optional;

public interface DynamicPricingEngineService {

    // USED BY CONTROLLER & TESTS
    DynamicPriceRecord calculateDynamicPrice(Long eventId);

    Optional<DynamicPriceRecord> getLatestPrice(Long eventId);

    List<DynamicPriceRecord> getAllComputedPrices();

    List<DynamicPriceRecord> getPriceHistory(Long eventId);
}
