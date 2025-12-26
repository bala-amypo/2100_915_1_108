package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long id, Integer remaining);

    SeatInventoryRecord findById(Long id);

    List<SeatInventoryRecord> getAllInventories();

    List<SeatInventoryRecord> findByEventId(Long eventId);
}
