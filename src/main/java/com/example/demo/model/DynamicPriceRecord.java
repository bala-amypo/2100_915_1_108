package com.example.demo.model;

import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;

public class DynamicPriceRecord {

    private Long eventId;
    private Double computedPrice;
    private String appliedRuleCodes;
    private LocalDateTime computedAt;

    @PrePersist
    public void prePersist() {
        computedAt = LocalDateTime.now();
    }

    // getters & setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Double getComputedPrice() { return computedPrice; }
    public void setComputedPrice(Double computedPrice) { this.computedPrice = computedPrice; }

    public String getAppliedRuleCodes() { return appliedRuleCodes; }
    public void setAppliedRuleCodes(String appliedRuleCodes) { this.appliedRuleCodes = appliedRuleCodes; }

    public LocalDateTime getComputedAt() { return computedAt; }
}
