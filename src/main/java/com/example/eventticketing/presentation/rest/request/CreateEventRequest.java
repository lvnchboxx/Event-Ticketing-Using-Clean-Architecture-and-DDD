package com.example.eventticketing.presentation.rest.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventRequest(
        UUID organizerId,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        int maximumCapacity
) {
}