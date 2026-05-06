package com.example.eventticketing.application.event.handler;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;

public class CreateEventHandler {

    private final EventRepository eventRepository;

    public CreateEventHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event handle(CreateEventCommand command) {
        Event event = new Event(
                command.organizerId(),
                command.name(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.location(),
                command.maximumCapacity()
        );

        return eventRepository.save(event);
    }
}