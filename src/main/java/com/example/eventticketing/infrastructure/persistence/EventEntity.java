package com.example.eventticketing.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    private UUID id;

    private UUID organizerId;
    private String name;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private String location;
    private int maximumCapacity;

    protected EventEntity() {
    }

    public EventEntity(UUID id,
                         UUID organizerId,
                         String name,
                         String description,
                         Instant startDate,
                         Instant endDate,
                         String location,
                         int maximumCapacity) {
        this.id = id;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.maximumCapacity = maximumCapacity;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizerId() {
        return organizerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }
}

