package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-adjustments")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService service;

    public PriceAdjustmentLogController(PriceAdjustmentLogService service) {
        this.service = service;
    }

    @PostMapping
    public PriceAdjustmentLog create(@RequestBody PriceAdjustmentLog log) {
        return service.logAdjustment(log);
    }

    @GetMapping("/event/{eventId}")
    public List<PriceAdjustmentLog> getByEvent(@PathVariable Long eventId) {
        return service.getAdjustmentsByEvent(eventId);
    }

    @GetMapping
    public List<PriceAdjustmentLog> getAll() {
        return service.getAllAdjustments();
    }

    @GetMapping("/{id}")
    public PriceAdjustmentLog getById(@PathVariable Long id) {
        return service.getAllAdjustments()
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
