package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Seat Inventory")
public class SeatInventoryController {

    private final SeatInventoryService service;

    public SeatInventoryController(SeatInventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> create(
            @RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(service.createInventory(record));
    }

    @PutMapping("/{eventId}/remaining")
    public ResponseEntity<SeatInventoryRecord> updateSeats(
            @PathVariable Long eventId,
            @RequestParam Integer remainingSeats) {
        return ResponseEntity.ok(
                service.updateRemainingSeats(eventId, remainingSeats)
        );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<SeatInventoryRecord> getByEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                service.getInventoryByEvent(eventId)
        );
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> all() {
        return ResponseEntity.ok(service.getAllInventories());
    }
}
