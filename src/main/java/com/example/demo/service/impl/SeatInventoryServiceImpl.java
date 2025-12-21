package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        // check if event exists
        eventRepository.findById(inventory.getEventId())
                .orElseThrow(() -> new BadRequestException("Event not found"));

        // validate seat count
        if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
            throw new BadRequestException(
                    "Remaining seats cannot exceed total seats");
        }

        return inventoryRepository.save(inventory);
    }

    @Override
    public SeatInventoryRecord updateRemainingSeats(
            Long eventId, Integer remainingSeats) {

        SeatInventoryRecord inventory =
                inventoryRepository.findByEventId(eventId)
                        .orElseThrow(() ->
                                new BadRequestException("Seat inventory not found"));

        if (remainingSeats > inventory.getTotalSeats()) {
            throw new BadRequestException(
                    "Remaining seats cannot exceed total seats");
        }

        inventory.setRemainingSeats(remainingSeats);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return inventoryRepository.findByEventId(eventId);
    }

    @Override
    public List<SeatInventoryRecord> getAllInventories() {
        return inventoryRepository.findAll();
    }
}
