package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;

public interface SeatInventoryService {

    // REQUIRED BY CONTROLLER + TESTS
    SeatInventoryRecord createInventory(SeatInventoryRecord inventory);

    // REQUIRED BY CONTROLLER + TESTS
    SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer remainingSeats);

    // REQUIRED BY CONTROLLER + TESTS
    SeatInventoryRecord findById(Long id);

    // REQUIRED BY CONTROLLER + TESTS
    List<SeatInventoryRecord> getAllInventories();

    // REQUIRED BY TESTS
    SeatInventoryRecord getInventoryByEvent(Long eventId);
}
