package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DynamicPricingEngineServiceImpl
        implements DynamicPricingEngineService {

    private final SeatInventoryService seatInventoryService;
    private final DynamicPriceRecordRepository dynamicPriceRecordRepository;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryService seatInventoryService,
            DynamicPriceRecordRepository dynamicPriceRecordRepository) {
        this.seatInventoryService = seatInventoryService;
        this.dynamicPriceRecordRepository = dynamicPriceRecordRepository;
    }

    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.getInventoryByEvent(eventId);

        if (inventories == null || inventories.isEmpty()) {
            throw new RuntimeException("Seat inventory not found for event " + eventId);
        }

        SeatInventoryRecord inventory = inventories.get(0);

        int totalSeats = inventory.getTotalSeats();
        int remainingSeats = inventory.getRemainingSeats();

        double price;
        String rule;

        if (remainingSeats <= totalSeats * 0.2) {
            price = 150.0;
            rule = "HIGH_DEMAND";
        } else if (remainingSeats <= totalSeats * 0.5) {
            price = 120.0;
            rule = "MEDIUM_DEMAND";
        } else {
            price = 100.0;
            rule = "NORMAL";
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(rule);

        return dynamicPriceRecordRepository.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRecordRepository.findAll();
    }

    @Override
    public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
        return dynamicPriceRecordRepository
                .findFirstByEventIdOrderByComputedAtDesc(eventId);
    }
}
