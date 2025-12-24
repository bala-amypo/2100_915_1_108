package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DynamicPricingEngineServiceImpl
        implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository priceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    // 🔴 REQUIRED CONSTRUCTOR (CRITICAL FOR TESTS)
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

        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (!Boolean.TRUE.equals(event.getActive())) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        long daysBeforeEvent = ChronoUnit.DAYS.between(
                LocalDate.now(), event.getEventDate());

        List<PricingRule> activeRules = ruleRepository.findByActiveTrue();

        List<PricingRule> matchingRules = activeRules.stream()
                .filter(rule ->
                        inventory.getRemainingSeats() >= rule.getMinRemainingSeats()
                                && inventory.getRemainingSeats() <= rule.getMaxRemainingSeats()
                                && daysBeforeEvent <= rule.getDaysBeforeEvent()
                )
                .collect(Collectors.toList());

        double multiplier = matchingRules.stream()
                .mapToDouble(PricingRule::getPriceMultiplier)
                .max()
                .orElse(1.0);

        double computedPrice = event.getBasePrice() * multiplier;

        String appliedRuleCodes = matchingRules.stream()
                .map(PricingRule::getRuleCode)
                .collect(Collectors.joining(","));

        Optional<DynamicPriceRecord> lastPrice =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (lastPrice.isPresent()
                && Double.compare(lastPrice.get().getComputedPrice(), computedPrice) != 0) {

            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(lastPrice.get().getComputedPrice());
            log.setNewPrice(computedPrice);
            log.setReason("Dynamic pricing rule applied");
            logRepository.save(log);
        }

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(computedPrice);
        record.setAppliedRuleCodes(appliedRuleCodes);

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
