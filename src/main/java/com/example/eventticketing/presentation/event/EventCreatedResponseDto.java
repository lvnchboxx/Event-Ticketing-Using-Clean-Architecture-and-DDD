package com.example.eventticketing.presentation.event;

import com.example.eventticketing.domain.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventCreatedResponseDto(
        UUID eventId,
        UUID organizerId,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        int maximumCapacity,
        EventStatus status
) {}

