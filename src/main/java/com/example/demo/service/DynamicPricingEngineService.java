package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;

import java.util.List;
import java.util.Optional;

public interface DynamicPricingEngineService {

    DynamicPriceRecord calculateDynamicPrice(Long eventId);

    List<DynamicPriceRecord> getAllComputedPrices();

    Optional<DynamicPriceRecord> getLatestPrice(Long eventId);
}
