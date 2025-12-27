package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class SeatInventoryController {

    private final SeatInventoryService service;

    public SeatInventoryController(SeatInventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> createInventory(
            @RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(service.createInventory(record));
    }

    @PutMapping("/{id}/remaining/{count}")
    public ResponseEntity<SeatInventoryRecord> updateRemainingSeats(
            @PathVariable Long id,
            @PathVariable Integer count) {
        return ResponseEntity.ok(service.updateRemainingSeats(id, count));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<SeatInventoryRecord> getInventoryByEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getInventoryByEvent(eventId));
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAllInventories() {
        return ResponseEntity.ok(service.getAllInventories());
    }
}
