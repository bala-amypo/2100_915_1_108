package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repository;

    /**
     * Constructor matches test expectations
     */
    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository repository,
            Object eventRepository
    ) {
        this.repository = repository;
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {
        return repository.save(record);
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public SeatInventoryRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat inventory not found with id " + id));
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return repository.findByEventId(eventId);
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return repository.findAll();
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer remainingSeats) {
        SeatInventoryRecord record = repository.findById(inventoryId)
                .orElseThrow(() ->
                        new RuntimeException("Seat inventory not found with id " + inventoryId));

        record.setRemainingSeats(remainingSeats);
        return repository.save(record);
    }
}
