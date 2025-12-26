package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.SeatInventoryRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;

    // ✅ Constructor Injection (MANDATORY as per rules)
    public SeatInventoryServiceImpl(SeatInventoryRepository seatInventoryRepository) {
        this.seatInventoryRepository = seatInventoryRepository;
    }

    // ✅ SAVE
    @Override
    public SeatInventoryRecord save(SeatInventoryRecord seatInventory) {
        seatInventory.setUpdatedAt(LocalDateTime.now());
        return seatInventoryRepository.save(seatInventory);
    }

    // ✅ UPDATE
    @Override
    public SeatInventoryRecord update(SeatInventoryRecord seatInventory) {
        seatInventory.setUpdatedAt(LocalDateTime.now());
        return seatInventoryRepository.save(seatInventory);
    }

    // ✅ FIND BY ID (IMPORTANT FIX FOR TEST LINE 832)
    @Override
    public SeatInventoryRecord findById(Long id) {
        return seatInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat inventory not found"));
    }

    // ✅ FIND BY EVENT ID
    @Override
    public List<SeatInventoryRecord> findByEventId(Long eventId) {
        return seatInventoryRepository.findByEventId(eventId);
    }

    // ✅ DELETE
    @Override
    public void deleteById(Long id) {
        seatInventoryRepository.deleteById(id);
    }

    // ✅ FIND ALL
    @Override
    public List<SeatInventoryRecord> findAll() {
        return seatInventoryRepository.findAll();
    }
}
