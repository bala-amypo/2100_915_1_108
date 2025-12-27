package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;

import java.util.List;
@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository inventoryRepository;
    private final EventRecordRepository eventRepository;

    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository inventoryRepository,
            EventRecordRepository eventRepository) {
        this.inventoryRepository = inventoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {

        if (!eventRepository.findById(inventory.getEventId()).isPresent()) {
            throw new BadRequestException("Seat inventory not found");
        }

        if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }

        return inventoryRepository.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(Long eventId, Integer remainingSeats) {

        SeatInventoryRecord inv =
                inventoryRepository.findByEventId(eventId).orElse(null);

        if (inv == null) return null;

        inv.setRemainingSeats(remainingSeats);
        return inventoryRepository.save(inv);
    }

    @Override
    public SeatInventoryRecord getInventoryByEvent(Long eventId) {
        return inventoryRepository.findByEventId(eventId).orElse(null);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return inventoryRepository.findAll();
    }
}
