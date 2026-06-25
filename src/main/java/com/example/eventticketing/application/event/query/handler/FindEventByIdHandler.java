package com.example.eventticketing.application.event.query.handler;

import com.example.eventticketing.application.event.query.FindEventByIdQuery;
import com.example.eventticketing.application.event.query.FindEventByIdQueryValidator;
import com.example.eventticketing.application.event.query.service.FindEventByIdApplicationService;
import com.example.eventticketing.application.event.query.usecase.FindEventByIdUseCase;
import com.example.eventticketing.application.validation.QueryValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;

public class FindEventByIdHandler {

    private final FindEventByIdUseCase findEventByIdUseCase;

    public FindEventByIdHandler(EventRepository eventRepository,
                                 QueryValidator<FindEventByIdQuery> validator) {
        this.findEventByIdUseCase = new FindEventByIdApplicationService(eventRepository, validator);
    }

    public Event handle(FindEventByIdQuery query) {
        return findEventByIdUseCase.execute(query);
    }

    public static FindEventByIdHandler defaultHandler(EventRepository eventRepository) {
        return new FindEventByIdHandler(eventRepository, new FindEventByIdQueryValidator());
    }
}

