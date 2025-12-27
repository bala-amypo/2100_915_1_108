package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;

import java.util.List;

public interface DynamicPricingEngineService {

    // REQUIRED BY CONTROLLER + TESTS
    DynamicPriceRecord calculateDynamicPrice(Long eventId);

    // REQUIRED BY TESTS
    DynamicPriceRecord computeDynamicPrice(Long eventId);

    // REQUIRED BY TESTS
    List<DynamicPriceRecord> getPriceHistory(Long eventId);

    // REQUIRED BY CONTROLLER + TESTS
    List<DynamicPriceRecord> getAllComputedPrices();
}
