package com.example.demo.controller;

import com.example.demo.entity.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class SeatInventoryController {

    private final SeatInventoryService inventoryService;

    public SeatInventoryController(SeatInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> createInventory(@RequestBody SeatInventoryRecord inventory) {
        return ResponseEntity.ok(inventoryService.createInventory(inventory));
    }

    @PutMapping("/{eventId}/remaining")
    public ResponseEntity<SeatInventoryRecord> updateRemainingSeats(@PathVariable Long eventId,
                                                                    @RequestParam Integer remainingSeats) {
        return ResponseEntity.ok(inventoryService.updateRemainingSeats(eventId, remainingSeats));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<SeatInventoryRecord> getInventoryByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(inventoryService.getInventoryByEvent(eventId).orElseThrow());
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }
}
