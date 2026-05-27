package com.example.eventticketing.application.event.handler;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.event.service.CreateEventApplicationService;
import com.example.eventticketing.application.event.usecase.CreateEventUseCase;
import com.example.eventticketing.application.validation.CommandValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;

public class CreateEventHandler {

    private final CreateEventUseCase createEventUseCase;

    public CreateEventHandler(EventRepository eventRepository,
                                CommandValidator<CreateEventCommand> validator) {
        this.createEventUseCase = new CreateEventApplicationService(eventRepository, validator);
    }

    public Event handle(CreateEventCommand command) {
        return createEventUseCase.execute(command);
    }
}

