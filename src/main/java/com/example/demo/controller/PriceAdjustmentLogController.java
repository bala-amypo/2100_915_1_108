package com.example.demo.controller;

import com.example.demo.entity.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-adjustments")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService logService;

    public PriceAdjustmentLogController(PriceAdjustmentLogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<PriceAdjustmentLog> createLog(@RequestBody PriceAdjustmentLog log) {
        return ResponseEntity.ok(logService.logAdjustment(log));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<PriceAdjustmentLog>> getLogsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(logService.getAdjustmentsByEvent(eventId));
    }

    @GetMapping
    public ResponseEntity<List<PriceAdjustmentLog>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllAdjustments());
    }
}
