package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;

import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long id, Integer seats);

    SeatInventoryRecord getInventoryByEvent(Long eventId); // ❗ NOT Optional

    List<SeatInventoryRecord> getAllInventories();
}
