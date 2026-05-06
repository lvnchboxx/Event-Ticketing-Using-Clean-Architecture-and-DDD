package com.example.eventticketing.domain.shared;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredAt();
}