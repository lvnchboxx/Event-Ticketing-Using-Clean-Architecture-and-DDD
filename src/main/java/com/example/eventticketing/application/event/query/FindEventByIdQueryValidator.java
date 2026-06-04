package com.example.eventticketing.application.event.query;

import com.example.eventticketing.application.exception.ValidationException;
import com.example.eventticketing.application.validation.QueryValidator;

public class FindEventByIdQueryValidator implements QueryValidator<FindEventByIdQuery> {

    @Override
    public void validate(FindEventByIdQuery query) {
        if (query == null) {
            throw new ValidationException("FindEventByIdQuery must not be null");
        }

        if (query.id() == null) {
            throw new ValidationException("id must not be null");
        }
    }
}

