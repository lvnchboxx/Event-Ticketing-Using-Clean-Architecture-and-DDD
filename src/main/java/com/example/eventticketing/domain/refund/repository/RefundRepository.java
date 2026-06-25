package com.example.eventticketing.domain.refund.repository;

import com.example.eventticketing.domain.refund.model.Refund;

import java.util.Optional;
import java.util.UUID;

public interface RefundRepository {

    Refund save(Refund refund);

    Optional<Refund> findById(UUID id);
}