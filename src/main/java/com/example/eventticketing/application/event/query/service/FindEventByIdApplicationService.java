package com.example.eventticketing.application.event.query.service;

import com.example.eventticketing.application.event.query.FindEventByIdQuery;
import com.example.eventticketing.application.event.query.FindEventByIdQueryValidator;
import com.example.eventticketing.application.event.query.usecase.FindEventByIdUseCase;
import com.example.eventticketing.application.exception.NotFoundException;
import com.example.eventticketing.application.validation.QueryValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;

import java.util.Objects;

public class FindEventByIdApplicationService implements FindEventByIdUseCase {

    private final EventRepository eventRepository;
    private final QueryValidator<FindEventByIdQuery> validator;

    public FindEventByIdApplicationService(EventRepository eventRepository,
                                           QueryValidator<FindEventByIdQuery> validator) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public Event execute(FindEventByIdQuery query) {
        validator.validate(query);

        return eventRepository.findById(query.id())
                .orElseThrow(() -> new NotFoundException("Event not found. id=" + query.id()));
    }
}

