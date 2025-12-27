package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository seatRepo;
    private final EventRecordRepository eventRepo;

    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            EventRecordRepository eventRepo) {
        this.seatRepo = seatRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {

        EventRecord event = eventRepo.findById(inventory.getEventId())
                .orElseThrow(() -> new BadRequestException("Event not found"));

        if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }

        return seatRepo.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer remainingSeats) {

        SeatInventoryRecord inv = findById(inventoryId);

        if (remainingSeats > inv.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }

        inv.setRemainingSeats(remainingSeats);
        return seatRepo.save(inv);
    }

    @Override
    public SeatInventoryRecord findById(Long id) {
        return seatRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return seatRepo.findAll();
    }

    @Override
    public SeatInventoryRecord getInventoryByEvent(Long eventId) {
        return seatRepo.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }
}
