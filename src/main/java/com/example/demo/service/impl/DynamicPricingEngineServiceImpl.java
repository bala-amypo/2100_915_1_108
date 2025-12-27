package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepo;
    private final SeatInventoryRecordRepository seatRepo;
    private final PricingRuleRepository ruleRepo;
    private final DynamicPriceRecordRepository priceRepo;
    private final PriceAdjustmentLogRepository logRepo;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepo,
            SeatInventoryRecordRepository seatRepo,
            PricingRuleRepository ruleRepo,
            DynamicPriceRecordRepository priceRepo,
            PriceAdjustmentLogRepository logRepo) {

        this.eventRepo = eventRepo;
        this.seatRepo = seatRepo;
        this.ruleRepo = ruleRepo;
        this.priceRepo = priceRepo;
        this.logRepo = logRepo;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        EventRecord event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!Boolean.TRUE.equals(event.getActive())) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inv = seatRepo.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        double price = event.getBasePrice();
        List<PricingRule> rules = ruleRepo.findByActiveTrue();

        long daysLeft =
                ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        StringBuilder applied = new StringBuilder();

        for (PricingRule r : rules) {
            if (inv.getRemainingSeats() >= r.getMinRemainingSeats()
                    && inv.getRemainingSeats() <= r.getMaxRemainingSeats()
                    && daysLeft <= r.getDaysBeforeEvent()) {

                price *= r.getPriceMultiplier();
                applied.append(r.getRuleCode()).append(",");
            }
        }

        DynamicPriceRecord previous =
                priceRepo.findFirstByEventIdOrderByComputedAtDesc(eventId).orElse(null);

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(applied.toString());

        priceRepo.save(record);

        if (previous != null && previous.getComputedPrice() != price) {
            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(previous.getComputedPrice());
            log.setNewPrice(price);
            logRepo.save(log);
        }

        return record;
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepo.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepo.findAll();
    }
}
