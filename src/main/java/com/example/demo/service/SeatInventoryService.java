package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long id, Integer remainingSeats);

    SeatInventoryRecord findById(Long id);

    List<SeatInventoryRecord> getInventoryByEvent(Long eventId);

    List<SeatInventoryRecord> getAllInventories();
}
