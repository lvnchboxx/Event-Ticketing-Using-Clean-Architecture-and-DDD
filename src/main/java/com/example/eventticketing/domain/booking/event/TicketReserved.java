package com.example.eventticketing.domain.booking.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.shared.DomainEvent;

public record TicketReserved(
        UUID bookingId,
        UUID eventId,
        UUID customerId,
        LocalDateTime occurredAt
) implements DomainEvent {
}