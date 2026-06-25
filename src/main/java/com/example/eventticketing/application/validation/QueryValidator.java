package com.example.eventticketing.application.validation;

public interface QueryValidator<T> {

    void validate(T query);
}

