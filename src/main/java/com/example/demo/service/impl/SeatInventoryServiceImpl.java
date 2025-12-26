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
     * Constructor required by tests (extra parameter ignored safely)
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
}
