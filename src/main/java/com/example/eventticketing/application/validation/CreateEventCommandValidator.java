package com.example.eventticketing.application.validation;

import org.springframework.stereotype.Component;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.exception.ValidationException;

@Component
public class CreateEventCommandValidator implements CommandValidator<CreateEventCommand> {

    @Override
    public void validate(CreateEventCommand command) {
        if (command == null) {
            throw new ValidationException("CreateEventCommand must not be null");
        }

        if (command.organizerId() == null) {
            throw new ValidationException("organizerId must not be null");
        }

        if (isBlank(command.name())) {
            throw new ValidationException("name must not be blank");
        }

        if (command.startDate() == null) {
            throw new ValidationException("startDate must not be null");
        }

        if (command.endDate() == null) {
            throw new ValidationException("endDate must not be null");
        }

        if (command.endDate().isBefore(command.startDate())) {
            throw new ValidationException("endDate must be after or equal to startDate");
        }

        if (isBlank(command.location())) {
            throw new ValidationException("location must not be blank");
        }

        if (command.maximumCapacity() <= 0) {
            throw new ValidationException("maximumCapacity must be greater than 0");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}

