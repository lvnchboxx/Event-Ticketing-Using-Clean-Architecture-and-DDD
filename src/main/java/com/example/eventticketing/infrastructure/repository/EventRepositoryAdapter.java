package com.example.eventticketing.infrastructure.repository;

import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import com.example.eventticketing.infrastructure.persistence.EventEntity;

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
                event.getStartDate().atZone(ZoneId.systemDefault()).toInstant(),
                event.getEndDate().atZone(ZoneId.systemDefault()).toInstant(),
                event.getLocation(),
                event.getMaximumCapacity()
        );

        EventEntity saved = jpaRepository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    private Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getOrganizerId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                entity.getEndDate().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                entity.getLocation(),
                entity.getMaximumCapacity()
        );
    }
}