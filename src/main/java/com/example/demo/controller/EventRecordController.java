package com.example.demo.controller;

import com.example.demo.model.EventRecord;
import com.example.demo.service.EventRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRecordController {

    private final EventRecordService eventService;

    public EventRecordController(EventRecordService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventRecord> createEvent(@RequestBody EventRecord event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRecord> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventRecord>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EventRecord> updateEventStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(eventService.updateEventStatus(id, active));
    }

    @GetMapping("/lookup/{eventCode}")
    public ResponseEntity<EventRecord> getEventByCode(@PathVariable String eventCode) {
        return ResponseEntity.ok(eventService.getEventByCode(eventCode).orElseThrow());
    }
}
