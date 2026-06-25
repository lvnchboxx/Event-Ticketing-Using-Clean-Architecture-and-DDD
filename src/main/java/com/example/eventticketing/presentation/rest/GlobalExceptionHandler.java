package com.example.eventticketing.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.eventticketing.application.exception.NotFoundException;
import com.example.eventticketing.domain.shared.BusinessRuleException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(exception.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        exception.printStackTrace();

        return ResponseEntity.status(500).body(
                new ErrorResponse(exception.getClass().getSimpleName() + ": " + exception.getMessage())
        );
    }

    public record ErrorResponse(String message) {
    }
}