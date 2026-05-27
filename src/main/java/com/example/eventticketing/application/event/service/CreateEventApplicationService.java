package com.example.eventticketing.application.event.service;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.event.usecase.CreateEventUseCase;
import com.example.eventticketing.application.validation.CommandValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;

import java.util.Objects;

public class CreateEventApplicationService implements CreateEventUseCase {

    private final EventRepository eventRepository;
    private final CommandValidator<CreateEventCommand> validator;

    public CreateEventApplicationService(EventRepository eventRepository,
                                         CommandValidator<CreateEventCommand> validator) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public Event execute(CreateEventCommand command) {
        validator.validate(command);

        return eventRepository.save(new Event(
                command.organizerId(),
                command.name(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.location(),
                command.maximumCapacity()
        ));
    }
}

