package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord save(SeatInventoryRecord seatInventory);

    SeatInventoryRecord update(SeatInventoryRecord seatInventory);

    
    SeatInventoryRecord findById(Long id);

    List<SeatInventoryRecord> findByEventId(Long eventId);

    List<SeatInventoryRecord> findAll();

    void deleteById(Long id);
}
