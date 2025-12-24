package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.service.EventRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventRecordServiceImpl implements EventRecordService {

    private final EventRecordRepository repository;

    public EventRecordServiceImpl(EventRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public EventRecord createEvent(EventRecord event) {

        if (repository.existsByEventCode(event.getEventCode())) {
            throw new BadRequestException("Event code already exists");
        }

        if (event.getBasePrice() == null || event.getBasePrice() <= 0) {
            throw new BadRequestException("Base price must be > 0");
        }

        return repository.save(event);
    }

    @Override
    public EventRecord getEventById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Optional<EventRecord> getEventByCode(String eventCode) {
        return repository.findByEventCode(eventCode);
    }

    @Override
    public List<EventRecord> getAllEvents() {
        return repository.findAll();
    }

    @Override
    public EventRecord updateEventStatus(Long id, boolean active) {
        EventRecord event = getEventById(id);
        event.setActive(active);
        return repository.save(event);
    }
}
