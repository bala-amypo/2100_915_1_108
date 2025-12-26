package com.example.demo.service;

import com.example.demo.model.SeatInventoryRecord;
import java.util.List;

public interface SeatInventoryService {

    SeatInventoryRecord createInventory(SeatInventoryRecord record);

    SeatInventoryRecord updateRemainingSeats(Long id, Integer remainingSeats);

    List<SeatInventoryRecord> getInventoryByEvent(Long eventId);

    List<SeatInventoryRecord> getAllInventories();

    SeatInventoryRecord findById(Long id);
}
