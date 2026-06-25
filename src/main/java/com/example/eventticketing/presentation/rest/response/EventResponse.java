package com.example.eventticketing.presentation.rest.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.model.EventStatus;

public record EventResponse(
        UUID id,
        UUID organizerId,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String location,
        int maximumCapacity,
        EventStatus status
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getOrganizerId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getMaximumCapacity(),
                event.getStatus()
        );
    }
}