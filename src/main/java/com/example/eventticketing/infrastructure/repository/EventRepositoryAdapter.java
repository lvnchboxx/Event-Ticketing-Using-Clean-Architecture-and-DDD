package com.example.eventticketing.infrastructure.repository;

import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import com.example.eventticketing.infrastructure.persistence.EventEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;


@Repository
public class EventRepositoryAdapter implements EventRepository {

    private final SpringDataEventJpaRepository jpaRepository;

    public EventRepositoryAdapter(SpringDataEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = new EventEntity(
                event.getId(),
                event.getOrganizerId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate().atZone(java.time.ZoneId.systemDefault()).toInstant(),
                event.getEndDate().atZone(java.time.ZoneId.systemDefault()).toInstant(),
                event.getLocation(),
                event.getMaximumCapacity()
        );

        EventEntity saved = jpaRepository.save(entity);


        return new Event(
                saved.getOrganizerId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStartDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                saved.getEndDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                saved.getLocation(),
                saved.getMaximumCapacity()
        );

    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new Event(
                        e.getOrganizerId(),
                        e.getName(),
                        e.getDescription(),
                        e.getStartDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        e.getEndDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                        e.getLocation(),
                        e.getMaximumCapacity()
                ));
    }
}


