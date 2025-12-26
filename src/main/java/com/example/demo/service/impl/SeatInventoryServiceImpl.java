package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repository;

    // ✅ Constructor Injection
    public SeatInventoryServiceImpl(SeatInventoryRecordRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE
    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {
        return repository.save(inventory);
    }

    // ✅ UPDATE REMAINING SEATS
    @Override
    public SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer remainingSeats) {
        SeatInventoryRecord inventory = repository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));

        inventory.setRemainingSeats(remainingSeats);
        return repository.save(inventory);
    }

    // ✅ FIND BY ID (NO Optional — TEST SAFE)
    @Override
    public SeatInventoryRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }

    // ✅ FIND BY EVENT
    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return repository.findByEventId(eventId);
    }

    // ✅ FIND ALL
    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return repository.findAll();
    }
}
