package com.example.demo.service;

import com.example.demo.model.DynamicPriceRecord;
import java.util.List;
import java.util.Optional;

public interface DynamicPricingEngineService {

    DynamicPriceRecord computeDynamicPrice(long eventId);

    Optional<DynamicPriceRecord> getLatestPrice(long eventId);

    List<DynamicPriceRecord> getPriceHistory(long eventId);
}
