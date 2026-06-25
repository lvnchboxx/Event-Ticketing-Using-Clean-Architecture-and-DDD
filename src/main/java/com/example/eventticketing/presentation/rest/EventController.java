package com.example.eventticketing.presentation.rest;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.event.query.FindEventByIdQuery;
import com.example.eventticketing.application.event.query.service.FindEventByIdApplicationService;
import com.example.eventticketing.application.event.service.CreateEventApplicationService;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.presentation.rest.request.CreateEventRequest;
import com.example.eventticketing.presentation.rest.response.EventResponse;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final CreateEventApplicationService createEventApplicationService;
    private final FindEventByIdApplicationService findEventByIdApplicationService;

    public EventController(
            CreateEventApplicationService createEventApplicationService,
            FindEventByIdApplicationService findEventByIdApplicationService
    ) {
        this.createEventApplicationService = createEventApplicationService;
        this.findEventByIdApplicationService = findEventByIdApplicationService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        CreateEventCommand command = new CreateEventCommand(
                request.organizerId(),
                request.name(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.location(),
                request.maximumCapacity()
        );

        Event event = createEventApplicationService.execute(command);

        return ResponseEntity.ok(EventResponse.from(event));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> findEventById(@PathVariable UUID eventId) {
        FindEventByIdQuery query = new FindEventByIdQuery(eventId);

        Event event = findEventByIdApplicationService.execute(query);

        return ResponseEntity.ok(EventResponse.from(event));
    }
}