package com.example.eventticketing.domain.event.model;

import com.example.eventticketing.domain.shared.BusinessRuleException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Event {

    private final UUID id;
    private final UUID organizerId;
    private final String name;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String location;
    private final int maximumCapacity;
    private EventStatus status;

    private final List<TicketCategory> ticketCategories = new ArrayList<>();

    public Event(
            UUID organizerId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String location,
            int maximumCapacity
    ) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("Event end date cannot be earlier than start date");
        }

        if (maximumCapacity <= 0) {
            throw new BusinessRuleException("Maximum capacity must be greater than zero");
        }

        this.id = UUID.randomUUID();
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.maximumCapacity = maximumCapacity;
        this.status = EventStatus.DRAFT;
    }

    public void addTicketCategory(TicketCategory ticketCategory) {
        int totalQuota = ticketCategories.stream()
                .mapToInt(TicketCategory::getQuota)
                .sum();

        if (totalQuota + ticketCategory.getQuota() > maximumCapacity) {
            throw new BusinessRuleException("Total ticket quota cannot exceed event capacity");
        }

        ticketCategories.add(ticketCategory);
    }

    public void publish() {
        if (status == EventStatus.CANCELLED) {
            throw new BusinessRuleException("Cancelled event cannot be published");
        }

        boolean hasActiveCategory = ticketCategories.stream()
                .anyMatch(TicketCategory::isActive);

        if (!hasActiveCategory) {
            throw new BusinessRuleException("Event must have at least one active ticket category");
        }

        this.status = EventStatus.PUBLISHED;
    }

    public void cancel() {
        if (status == EventStatus.COMPLETED) {
            throw new BusinessRuleException("Completed event cannot be cancelled");
        }

        this.status = EventStatus.CANCELLED;
        ticketCategories.forEach(TicketCategory::disable);
    }
}