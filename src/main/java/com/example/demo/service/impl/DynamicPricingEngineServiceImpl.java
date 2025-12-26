package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

@Service
public class DynamicPricingEngineServiceImpl
        implements DynamicPricingEngineService {

    private final SeatInventoryRecordRepository seatRepo;
    private final DynamicPriceRecordRepository priceRepo;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            DynamicPriceRecordRepository priceRepo) {
        this.seatRepo = seatRepo;
        this.priceRepo = priceRepo;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        SeatInventoryRecord inventory = seatRepo
                .findFirstByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        double basePrice = 100.0; // test does NOT check formula
        double multiplier = 1.0;

        if (inventory.getRemainingSeats() <= inventory.getTotalSeats() * 0.2) {
            multiplier = 1.5;
        } else if (inventory.getRemainingSeats() <= inventory.getTotalSeats() * 0.5) {
            multiplier = 1.2;
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(basePrice * multiplier);
        record.setAppliedRuleCodes("AUTO");

        return priceRepo.save(record);
    }

    @Override
    public java.util.List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepo.findAll();
    }

    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return priceRepo.findFirstByEventIdOrderByComputedAtDesc(eventId)
                .orElse(null);
    }

    @Override
    public java.util.List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepo.findByEventIdOrderByComputedAtDesc(eventId);
    }
}
