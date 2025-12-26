package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;
import java.util.Optional;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer seats);

    List<SeatInventoryRecord> getAllInventories();

    // 🔥 TEST EXPECTS OPTIONAL, NOT LIST
    Optional<SeatInventoryRecord> getInventoryByEvent(Long eventId);
}
