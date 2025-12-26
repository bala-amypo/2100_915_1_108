package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl
        implements SeatInventoryService {

    private final SeatInventoryRecordRepository seatRepo;
    private final EventRecordRepository eventRepo;

    // ✅ TEST EXPECTS THIS CONSTRUCTOR
    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            EventRecordRepository eventRepo) {
        this.seatRepo = seatRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(
            SeatInventoryRecord record) {
        return seatRepo.save(record);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(
            Long id, Integer seats) {

        SeatInventoryRecord record =
                seatRepo.findById(id).orElseThrow();

        record.setRemainingSeats(seats);
        return seatRepo.save(record);
    }

    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return seatRepo.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return seatRepo.findAll();
    }

    @Override
    public SeatInventoryRecord findById(Long id) {
        return seatRepo.findById(id).orElseThrow();
    }
}
