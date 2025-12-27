package com.example.demo.controller;

import com.example.demo.model.EventRecord;
import com.example.demo.service.EventRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events")
public class EventRecordController {

    private final EventRecordService service;

    public EventRecordController(EventRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventRecord> create(@RequestBody EventRecord event) {
        return ResponseEntity.ok(service.createEvent(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRecord> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventRecord>> all() {
        return ResponseEntity.ok(service.getAllEvents());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EventRecord> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(service.updateEventStatus(id, active));
    }

    @GetMapping("/lookup/{code}")
    public ResponseEntity<EventRecord> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(
                service.getEventByCode(code).orElse(null)
        );
    }
}
