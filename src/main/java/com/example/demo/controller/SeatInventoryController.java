package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat-inventory")
public class SeatInventoryController {

    private final SeatInventoryService service;

    public SeatInventoryController(SeatInventoryService service) {
        this.service = service;
    }

    // ✅ CREATE INVENTORY
    @PostMapping
    public SeatInventoryRecord createInventory(
            @RequestBody SeatInventoryRecord inventory) {
        return service.createInventory(inventory);
    }

    // ✅ UPDATE REMAINING SEATS
    @PutMapping("/{id}/remaining/{count}")
    public SeatInventoryRecord updateRemainingSeats(
            @PathVariable Long id,
            @PathVariable Integer count) {
        return service.updateRemainingSeats(id, count);
    }

    // ✅ GET INVENTORY BY EVENT (FIXED — NO orElseThrow)
    @GetMapping("/event/{eventId}")
    public List<SeatInventoryRecord> getInventoryByEvent(
            @PathVariable Long eventId) {

        List<SeatInventoryRecord> list =
                service.getInventoryByEvent(eventId);

        if (list.isEmpty()) {
            throw new RuntimeException("No seat inventory found for event");
        }

        return list;
    }

    // ✅ GET ALL
    @GetMapping
    public List<SeatInventoryRecord> getAllInventories() {
        return service.getAllInventories();
    }
}
