package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat-inventory")
public class SeatInventoryController {

    private final SeatInventoryService service;

    public SeatInventoryController(SeatInventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SeatInventoryRecord> create(@RequestBody SeatInventoryRecord record) {
        return ResponseEntity.ok(service.createInventory(record));
    }

    @PutMapping("/{id}/remaining/{remaining}")
    public ResponseEntity<SeatInventoryRecord> updateRemaining(
            @PathVariable Long id,
            @PathVariable Integer remaining) {
        return ResponseEntity.ok(service.updateRemainingSeats(id, remaining));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryRecord> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryRecord>> getAll() {
        return ResponseEntity.ok(service.getAllInventories());
    }
}
