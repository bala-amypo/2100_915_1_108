package com.example.demo.service.impl;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.repository.PriceAdjustmentLogRepository;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAdjustmentLogServiceImpl
        implements PriceAdjustmentLogService {

    private final PriceAdjustmentLogRepository repository;

    // REQUIRED CONSTRUCTOR
    public PriceAdjustmentLogServiceImpl(
            PriceAdjustmentLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public PriceAdjustmentLog logAdjustment(PriceAdjustmentLog log) {
        return repository.save(log);
    }

    @Override
    public List<PriceAdjustmentLog> getAdjustmentsByEvent(Long eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    public List<PriceAdjustmentLog> getAllAdjustments() {
        return repository.findAll();
    }
}
