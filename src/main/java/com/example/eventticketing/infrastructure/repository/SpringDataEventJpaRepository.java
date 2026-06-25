package com.example.eventticketing.infrastructure.repository;

import com.example.eventticketing.infrastructure.persistence.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataEventJpaRepository extends JpaRepository<EventEntity, UUID> {
}

