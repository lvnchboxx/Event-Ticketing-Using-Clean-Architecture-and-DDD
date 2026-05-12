package com.example.eventticketing.domain.event.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.Money;

public class TicketCategory {

    private final UUID id;
    private final String name;
    private final Money price;
    private final int quota;
    private final LocalDateTime salesStartDate;
    private final LocalDateTime salesEndDate;
    private boolean active;

    public TicketCategory(
            String name,
            Money price,
            int quota,
            LocalDateTime salesStartDate,
            LocalDateTime salesEndDate,
            LocalDateTime eventStartDate
    ) {
        if (quota <= 0) {
            throw new BusinessRuleException("Ticket quota must be greater than zero");
        }

        if (salesEndDate.isAfter(eventStartDate)) {
            throw new BusinessRuleException("Ticket sales period must end before or at event start date");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.quota = quota;
        this.salesStartDate = salesStartDate;
        this.salesEndDate = salesEndDate;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuota() {
        return quota;
    }

    public LocalDateTime getSalesStartDate() {
        return salesStartDate;
    }

    public LocalDateTime getSalesEndDate() {
        return salesEndDate;
    }

    public boolean isActive() {
        return active;
    }

    public void disable() {
        this.active = false;
    }
}