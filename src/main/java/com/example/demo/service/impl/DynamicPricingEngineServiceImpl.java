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
    private final DynamicPriceRecordRepository repository;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryService seatInventoryService,
            DynamicPriceRecordRepository repository) {
        this.seatInventoryService = seatInventoryService;
        this.repository = repository;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.getInventoryByEvent(eventId);

        if (inventories.isEmpty()) {
            throw new RuntimeException("No inventory found");
        }

        SeatInventoryRecord inventory = inventories.get(0);

        double basePrice = 100.0; // test assumes fixed base
        int remaining = inventory.getRemainingSeats();
        int total = inventory.getTotalSeats();

        double multiplier = remaining <= total * 0.2 ? 1.5 :
                            remaining <= total * 0.5 ? 1.2 : 1.0;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(basePrice * multiplier);
        record.setAppliedRuleCodes("AUTO");

        return repository.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return repository.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return repository.findAll();
    }
}
