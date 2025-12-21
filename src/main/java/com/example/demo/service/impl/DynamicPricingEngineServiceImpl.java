package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository priceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    public DynamicPricingEngineServiceImpl(EventRecordRepository eventRepository,
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
        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException("Event not found"));
        if (!event.isActive()) throw new BadRequestException("Event is not active");

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        List<PricingRule> rules = ruleRepository.findByActiveTrue();
        long daysToEvent = LocalDate.now().until(event.getEventDate()).getDays();

        List<PricingRule> applicable = rules.stream()
                .filter(r -> inventory.getRemainingSeats() >= r.getMinRemainingSeats()
                        && inventory.getRemainingSeats() <= r.getMaxRemainingSeats()
                        && daysToEvent <= r.getDaysBeforeEvent())
                .collect(Collectors.toList());

        double multiplier = applicable.stream()
                .map(PricingRule::getPriceMultiplier)
                .max(Double::compareTo)
                .orElse(1.0);

        double price = event.getBasePrice() * multiplier;

        String appliedRuleCodes = applicable.stream()
                .map(PricingRule::getRuleCode)
                .collect(Collectors.joining(","));

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(price);
        record.setAppliedRuleCodes(appliedRuleCodes);
        record = priceRepository.save(record);

        Optional<DynamicPriceRecord> latest = priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
        if (latest.isPresent() && latest.get().getComputedPrice() != price) {
            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(latest.get().getComputedPrice());
            log.setNewPrice(price);
            log.setReason("Dynamic price adjustment");
            logRepository.save(log);
        }

        return record;
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
