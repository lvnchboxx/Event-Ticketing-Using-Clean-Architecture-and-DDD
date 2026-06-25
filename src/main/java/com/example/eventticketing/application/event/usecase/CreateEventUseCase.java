package com.example.eventticketing.application.event.usecase;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.domain.event.model.Event;

public interface CreateEventUseCase {

    Event execute(CreateEventCommand command);

}

