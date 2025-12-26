package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final SeatInventoryService seatInventoryService;
    private final DynamicPriceRecordRepository priceRepository;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryService seatInventoryService,
            DynamicPriceRecordRepository priceRepository) {
        this.seatInventoryService = seatInventoryService;
        this.priceRepository = priceRepository;
    }

    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.findByEventId(eventId);

        if (inventories.isEmpty()) {
            throw new RuntimeException("No seat inventory for event");
        }

        SeatInventoryRecord inventory = inventories.get(0);

        double basePrice = 100.0;
        double multiplier = 1.0;

        int remaining = inventory.getRemainingSeats();
        int total = inventory.getTotalSeats();

        if (remaining <= total * 0.2) multiplier = 1.5;
        else if (remaining <= total * 0.5) multiplier = 1.2;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(basePrice * multiplier);
        record.setAppliedRuleCodes("DEMAND_BASED");

        return priceRepository.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepository.findAll();
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepository.findByEventIdOrderByComputedAtDesc(eventId);
    }
}
