package com.example.eventticketing.application.event.query.usecase;

import com.example.eventticketing.application.event.query.FindEventByIdQuery;
import com.example.eventticketing.domain.event.model.Event;

public interface FindEventByIdUseCase {

    Event execute(FindEventByIdQuery query);
}

