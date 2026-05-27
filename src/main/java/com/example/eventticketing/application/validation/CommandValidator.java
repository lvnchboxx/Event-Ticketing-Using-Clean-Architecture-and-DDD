package com.example.eventticketing.application.validation;

public interface CommandValidator<T> {

    void validate(T command);

}

