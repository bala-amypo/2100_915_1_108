package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Calculate dynamic price based on remaining seats
     */
    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.getInventoryByEvent(eventId);

        if (inventories == null || inventories.isEmpty()) {
            throw new RuntimeException("Seat inventory not found for event: " + eventId);
        }

        SeatInventoryRecord inventory = inventories.get(0);

        int totalSeats = inventory.getTotalSeats();
        int remainingSeats = inventory.getRemainingSeats();

        double computedPrice;
        String appliedRule;

        if (remainingSeats <= totalSeats * 0.2) {
            computedPrice = 150.0;
            appliedRule = "HIGH_DEMAND";
        } else if (remainingSeats <= totalSeats * 0.5) {
            computedPrice = 120.0;
            appliedRule = "MEDIUM_DEMAND";
        } else {
            computedPrice = 100.0;
            appliedRule = "NORMAL";
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(computedPrice);
        record.setAppliedRuleCodes(appliedRule);

        return dynamicPriceRecordRepository.save(record);
    }

    /**
     * Fetch all computed prices
     */
    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRecordRepository.findAll();
    }

    /**
     * Fetch latest price for an event
     */
    @Override
    public DynamicPriceRecord getLatestPrice(Long eventId) {
        return dynamicPriceRecordRepository
                .findTopByEventIdOrderByComputedAtDesc(eventId)
                .orElseThrow(() ->
                        new RuntimeException("No dynamic price found for event: " + eventId));
    }
}
