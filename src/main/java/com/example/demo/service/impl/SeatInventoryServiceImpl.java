package com.example.demo.service.impl;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRecordRepository seatRepo;
    private final EventRecordRepository eventRepo;

    // ✅ REQUIRED BY TEST
    public SeatInventoryServiceImpl(
            SeatInventoryRecordRepository seatRepo,
            EventRecordRepository eventRepo) {
        this.seatRepo = seatRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public List<SeatInventoryRecord> getInventoryByEvent(Long eventId) {
        return seatRepo.findByEventId(eventId);
    }
}
