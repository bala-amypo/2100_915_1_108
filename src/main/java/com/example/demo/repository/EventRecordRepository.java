package com.example.demo.repository;

import com.example.demo.model.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRecordRepository extends JpaRepository<EventRecord, Long> {
}
