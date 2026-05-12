package com.example.eventticketing.domain.event.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.shared.DomainEvent;

public record EventCancelled(
        UUID eventId,
        LocalDateTime occurredAt
) implements DomainEvent {
}