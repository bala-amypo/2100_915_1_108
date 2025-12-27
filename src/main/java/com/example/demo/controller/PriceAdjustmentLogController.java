package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adjustments")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService service;

    public PriceAdjustmentLogController(PriceAdjustmentLogService service) {
        this.service = service;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<PriceAdjustmentLog>> getAdjustmentsByEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getAdjustmentsByEvent(eventId));
    }
}
