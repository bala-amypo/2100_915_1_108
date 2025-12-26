package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository seatRepo;
    private final EventRecordRepository eventRepo;

    // ✅ constructor exactly as test expects
    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            EventRecordRepository eventRepo) {
        this.seatRepo = seatRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {
        return seatRepo.save(record);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer seats) {
        SeatInventoryRecord record = seatRepo.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        record.setRemainingSeats(seats);
        return seatRepo.save(record);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return seatRepo.findAll();
    }

    // 🔥 OPTIONAL — THIS FIXES 5 MOCKITO ERRORS
    @Override
    public Optional<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return seatRepo.findFirstByEventId(eventId);
    }
}
