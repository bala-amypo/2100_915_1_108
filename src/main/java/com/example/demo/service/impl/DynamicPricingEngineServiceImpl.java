package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;

import java.time.temporal.ChronoUnit;
import java.util.*;
@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository priceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRepository,
            SeatInventoryRecordRepository inventoryRepository,
            PricingRuleRepository ruleRepository,
            DynamicPriceRecordRepository priceRepository,
            PriceAdjustmentLogRepository logRepository) {
        this.eventRepository = eventRepository;
        this.inventoryRepository = inventoryRepository;
        this.ruleRepository = ruleRepository;
        this.priceRepository = priceRepository;
        this.logRepository = logRepository;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        EventRecord event =
                eventRepository.findById(eventId)
                        .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inventory =
                inventoryRepository.findByEventId(eventId)
                        .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        double multiplier = 1.0;
        String appliedRule = "";

        long days =
                ChronoUnit.DAYS.between(
                        java.time.LocalDate.now(),
                        event.getEventDate());

        for (PricingRule rule : ruleRepository.findByActiveTrue()) {
            if (inventory.getRemainingSeats() >= rule.getMinRemainingSeats()
                    && inventory.getRemainingSeats() <= rule.getMaxRemainingSeats()
                    && days <= rule.getDaysBeforeEvent()) {

                if (rule.getPriceMultiplier() > multiplier) {
                    multiplier = rule.getPriceMultiplier();
                    appliedRule = rule.getRuleCode();
                }
            }
        }

        double finalPrice = event.getBasePrice() * multiplier;

        Optional<DynamicPriceRecord> prev =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (prev.isPresent() && prev.get().getComputedPrice() != finalPrice) {
            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(prev.get().getComputedPrice());
            log.setNewPrice(finalPrice);
            logRepository.save(log);
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(finalPrice);
        record.setAppliedRuleCodes(appliedRule);

        return priceRepository.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepository.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
        return priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepository.findAll();
    }
}
