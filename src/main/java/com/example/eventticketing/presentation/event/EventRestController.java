package com.example.eventticketing.presentation.event;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.event.handler.CreateEventHandler;
import com.example.eventticketing.application.event.usecase.CreateEventUseCase;
import com.example.eventticketing.application.validation.CreateEventCommandValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventRepository eventRepository;

    public EventRestController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<EventCreatedResponseDto> create(@RequestBody CreateEventRequestDto request) {
        // simplified auth: organizerId comes from request
        // (presentation layer auth can be added later)
        CreateEventCommand command = new CreateEventCommand(
                request.organizerId(),
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.location(),
                request.maximumCapacity()
        );

        CreateEventHandler handler = new CreateEventHandler(eventRepository, new CreateEventCommandValidator());
        Event event = handler.handle(command);

        EventCreatedResponseDto body = new EventCreatedResponseDto(
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

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}

