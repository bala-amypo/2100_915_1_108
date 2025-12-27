package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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

        EventRecord event = eventRepository.findById(eventId).orElseThrow();

        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inventory =
                inventoryRepository.findByEventId(eventId)
                        .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        long daysBeforeEvent =
                ChronoUnit.DAYS.between(java.time.LocalDate.now(), event.getEventDate());

        List<PricingRule> rules = ruleRepository.findByActiveTrue();

        double multiplier = rules.stream()
                .filter(r ->
                        inventory.getRemainingSeats() >= r.getMinRemainingSeats() &&
                        inventory.getRemainingSeats() <= r.getMaxRemainingSeats() &&
                        daysBeforeEvent <= r.getDaysBeforeEvent())
                .map(PricingRule::getPriceMultiplier)
                .max(Double::compare)
                .orElse(1.0);

        double computedPrice = event.getBasePrice() * multiplier;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(computedPrice);
        record.setAppliedRuleCodes(
                rules.stream().map(PricingRule::getRuleCode)
                        .collect(Collectors.joining(",")));

        Optional<DynamicPriceRecord> previous =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (previous.isPresent() &&
                Double.compare(previous.get().getComputedPrice(), computedPrice) != 0) {

            PriceAdjustmentLog log = new PriceAdjustmentLog();
            log.setEventId(eventId);
            log.setOldPrice(previous.get().getComputedPrice());
            log.setNewPrice(computedPrice);
            log.setReason("Dynamic pricing recalculation");

            logRepository.save(log);
        }

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
