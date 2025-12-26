package com.example.demo.service.impl;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.DynamicPriceRecordRepository;
import com.example.demo.service.DynamicPricingEngineService;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    // ✅ REQUIRED BY INTERFACE
    @Override
    public DynamicPriceRecord calculateDynamicPrice(Long eventId) {

        List<SeatInventoryRecord> inventories =
                seatInventoryService.getInventoryByEvent(eventId);

        if (inventories.isEmpty()) {
            throw new RuntimeException("Seat inventory not found for event");
        }

        SeatInventoryRecord inventory = inventories.get(0);

        double basePrice = inventory.getBaseSeatPrice();
        int remaining = inventory.getRemainingSeats();
        int total = inventory.getTotalSeats();

        double multiplier = 1.0;

        if (remaining <= total * 0.2) {
            multiplier = 1.5;
        } else if (remaining <= total * 0.5) {
            multiplier = 1.2;
        }

        double finalPrice = basePrice * multiplier;

        DynamicPriceRecord record = new DynamicPriceRecord();
        record.setEventId(eventId);
        record.setFinalPrice(finalPrice);
        record.setCreatedAt(LocalDateTime.now());

        return priceRepository.save(record);
    }

    // ✅ REQUIRED BY INTERFACE (THIS WAS MISSING)
    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepository.findAll();
    }
}
