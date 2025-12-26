package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repository;

    public SeatInventoryServiceImpl(SeatInventoryRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {
        return repository.save(record);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long id, Integer remaining) {
        SeatInventoryRecord record = findById(id);
        record.setRemainingSeats(remaining);
        return repository.save(record);
    }

    @Override
    public SeatInventoryRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return repository.findAll();
    }

    @Override
    public List<SeatInventoryRecord> findByEventId(Long eventId) {
        return repository.findByEventId(eventId);
    }
}
