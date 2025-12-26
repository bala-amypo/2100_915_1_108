package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final SeatInventoryRecordRepository seatRepo;
    private final DynamicPriceRecordRepository priceRepo;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            DynamicPriceRecordRepository priceRepo) {
        this.seatRepo = seatRepo;
        this.priceRepo = priceRepo;
    }

    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories = seatRepo.findByEventId(eventId);

        if (inventories.isEmpty()) {
            throw new RuntimeException("Seat inventory not found for event");
        }

        SeatInventoryRecord inventory = inventories.get(0);

        int remaining = inventory.getRemainingSeats();
        int total = inventory.getTotalSeats();

        double basePrice = 100.0; // default base price
        double multiplier = 1.0;

        if (remaining <= total * 0.2) {
            multiplier = 1.5;
        } else if (remaining <= total * 0.5) {
            multiplier = 1.2;
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(basePrice * multiplier);
        record.setAppliedRuleCodes("SEAT_DEMAND");

        return priceRepo.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepo.findAll();
    }
}
