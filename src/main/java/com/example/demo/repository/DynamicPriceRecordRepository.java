package com.example.demo.repository;

import com.example.demo.model.DynamicPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DynamicPriceRecordRepository
        extends JpaRepository<DynamicPriceRecord, Long> {

    Optional<DynamicPriceRecord>
    findFirstByEventIdOrderByComputedAtDesc(Long eventId);

    // REQUIRED BY TESTS
    List<DynamicPriceRecord>
    findByEventIdOrderByComputedAtDesc(Long eventId);
}
