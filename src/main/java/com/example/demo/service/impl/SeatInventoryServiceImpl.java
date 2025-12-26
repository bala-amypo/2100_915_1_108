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
     * Constructor matches tests (extra parameter allowed)
     */
    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository repository,
            Object eventRepository
    ) {
        this.repository = repository;
    }

    @Override
    public SeatInventoryRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat inventory not found with id " + id));
    }

    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return repository.findAll();
    }

    @Override
    public SeatInventoryRecord save(SeatInventoryRecord record) {
        return repository.save(record);
    }

    // ✅ THIS METHOD WAS MISSING — NOW FIXED
    @Override
    public SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer remainingSeats) {
        SeatInventoryRecord record = repository.findById(inventoryId)
                .orElseThrow(() ->
                        new RuntimeException("Seat inventory not found with id " + inventoryId));

        record.setRemainingSeats(remainingSeats);
        return repository.save(record);
    }
}
