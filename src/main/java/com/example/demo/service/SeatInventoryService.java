package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long inventoryId, Integer seats);

    List<SeatInventoryRecord> getAllInventories();

    List<SeatInventoryRecord> getInventoryByEvent(Long eventId);
}
