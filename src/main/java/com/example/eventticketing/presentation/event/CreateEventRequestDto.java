package com.example.eventticketing.presentation.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventRequestDto(
        UUID organizerId,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        int maximumCapacity
) {}

