package com.example.eventticketing.domain.booking.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.shared.DomainEvent;

public record BookingPaid(
        UUID bookingId,
        LocalDateTime occurredAt
) implements DomainEvent {
}