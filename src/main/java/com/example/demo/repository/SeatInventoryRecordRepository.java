package com.example.demo.repository;

import com.example.demo.model.SeatInventoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatInventoryRecordRepository
        extends JpaRepository<SeatInventoryRecord, Long> {

    // 🔥 REQUIRED BY TEST
    Optional<SeatInventoryRecord> findFirstByEventId(Long eventId);
}
