package com.example.eventticketing.application.event.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventCommand(
        UUID organizerId,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        int maximumCapacity
) {
}