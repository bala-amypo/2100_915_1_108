package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-adjustments")
@Tag(name = "Price Adjustments")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService service;

    public PriceAdjustmentLogController(PriceAdjustmentLogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PriceAdjustmentLog> log(
            @RequestBody PriceAdjustmentLog log) {
        return ResponseEntity.ok(service.logAdjustment(log));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<PriceAdjustmentLog>> byEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                service.getAdjustmentsByEvent(eventId)
        );
    }

    @GetMapping
    public ResponseEntity<List<PriceAdjustmentLog>> all() {
        return ResponseEntity.ok(service.getAllAdjustments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceAdjustmentLog> get(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
