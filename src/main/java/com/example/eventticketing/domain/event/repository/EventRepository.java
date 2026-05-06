package com.example.eventticketing.domain.event.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.eventticketing.domain.event.model.Event;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(UUID id);
}