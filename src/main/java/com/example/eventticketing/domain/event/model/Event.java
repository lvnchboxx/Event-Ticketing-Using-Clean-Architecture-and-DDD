package com.example.eventticketing.domain.event.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.eventticketing.domain.event.event.EventCancelled;
import com.example.eventticketing.domain.event.event.EventCreated;
import com.example.eventticketing.domain.event.event.EventPublished;
import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.DomainEvent;

import lombok.Getter;

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
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Event(
            UUID organizerId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String location,
            int maximumCapacity
    ) {
        this(
                UUID.randomUUID(),
                organizerId,
                name,
                description,
                startDate,
                endDate,
                location,
                maximumCapacity,
                true
        );
    }

    public Event(
            UUID id,
            UUID organizerId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String location,
            int maximumCapacity
    ) {
        this(
                id,
                organizerId,
                name,
                description,
                startDate,
                endDate,
                location,
                maximumCapacity,
                false
        );
    }

    private Event(
            UUID id,
            UUID organizerId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String location,
            int maximumCapacity,
            boolean raiseCreatedEvent
    ) {
        validate(id, organizerId, startDate, endDate, maximumCapacity);

        this.id = id;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.maximumCapacity = maximumCapacity;
        this.status = EventStatus.DRAFT;

        if (raiseCreatedEvent) {
            domainEvents.add(new EventCreated(this.id, organizerId, LocalDateTime.now()));
        }
    }

    private void validate(
            UUID id,
            UUID organizerId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int maximumCapacity
    ) {
        if (id == null) {
            throw new BusinessRuleException("Event id cannot be null");
        }

        if (organizerId == null) {
            throw new BusinessRuleException("Organizer id cannot be null");
        }

        if (startDate == null || endDate == null) {
            throw new BusinessRuleException("Event start date and end date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("Event end date cannot be earlier than start date");
        }

        if (maximumCapacity <= 0) {
            throw new BusinessRuleException("Maximum capacity must be greater than zero");
        }
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

        if (status != EventStatus.DRAFT) {
            throw new BusinessRuleException("Only draft event can be published");
        }

        boolean hasActiveCategory = ticketCategories.stream()
                .anyMatch(TicketCategory::isActive);

        if (!hasActiveCategory) {
            throw new BusinessRuleException("Event must have at least one active ticket category");
        }

        this.status = EventStatus.PUBLISHED;
        domainEvents.add(new EventPublished(this.id, LocalDateTime.now()));
    }

    public void cancel() {
        if (status == EventStatus.COMPLETED) {
            throw new BusinessRuleException("Completed event cannot be cancelled");
        }

        if (status != EventStatus.PUBLISHED) {
            throw new BusinessRuleException("Only published event can be cancelled");
        }

        this.status = EventStatus.CANCELLED;
        ticketCategories.forEach(TicketCategory::disable);

        domainEvents.add(new EventCancelled(this.id, LocalDateTime.now()));
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}