package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;
import java.util.List;
import java.util.Optional;

public interface DynamicPricingEngineService {

    // calculate and store price
    DynamicPriceRecord calculateDynamicPrice(Long eventId);

    // get latest price for event
    Optional<DynamicPriceRecord> getLatestPrice(Long eventId);

    // get full price history
    List<DynamicPriceRecord> getAllComputedPrices();
}
