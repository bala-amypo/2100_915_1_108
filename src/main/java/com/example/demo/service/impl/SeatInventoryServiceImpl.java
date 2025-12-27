package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;

public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository repo;
    private final EventRecordRepository eventRepo;

    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository repo,
            EventRecordRepository eventRepo) {

        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord record) {

        eventRepo.findById(record.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (record.getRemainingSeats() > record.getTotalSeats()) {
            throw new BadRequestException(
                    "Remaining seats cannot exceed total seats");
        }

        return repo.save(record);
    }

    @Override
    public SeatInventoryRecord getInventoryByEvent(Long eventId) {
        return repo.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }
}
