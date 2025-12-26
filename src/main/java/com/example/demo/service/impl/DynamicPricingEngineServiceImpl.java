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
    private final DynamicPriceRecordRepository priceRepository;

    public DynamicPricingEngineServiceImpl(
            SeatInventoryService seatInventoryService,
            DynamicPriceRecordRepository priceRepository) {
        this.seatInventoryService = seatInventoryService;
        this.priceRepository = priceRepository;
    }

    // ✅ calculate price
    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.getInventoryByEvent(eventId);

        if (inventories.isEmpty()) {
            throw new RuntimeException("Seat inventory not found");
        }

        SeatInventoryRecord inventory = inventories.get(0);

        int total = inventory.getTotalSeats();
        int remaining = inventory.getRemainingSeats();

        double basePrice = 100.0; // fixed base price
        double multiplier = 1.0;

        if (remaining <= total * 0.2) {
            multiplier = 1.5;
        } else if (remaining <= total * 0.5) {
            multiplier = 1.2;
        }

        double finalPrice = basePrice * multiplier;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setComputedPrice(finalPrice);
        record.setAppliedRuleCodes("DEMAND_BASED");

        return priceRepository.save(record);
    }

    // ✅ latest price
    @Override
    public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
        return priceRepository.findAll()
                .stream()
                .filter(r -> r.getEventId().equals(eventId))
                .reduce((first, second) -> second);
    }

    // ✅ history
    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepository.findAll();
    }
}
