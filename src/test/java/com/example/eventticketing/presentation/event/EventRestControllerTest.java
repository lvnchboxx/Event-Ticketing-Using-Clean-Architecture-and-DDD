package com.example.eventticketing.presentation.event;

import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class EventRestControllerTest {

    @Test
    void postCreateEvent_returnsCreated() {
        UUID organizerId = UUID.randomUUID();

        EventRepository eventRepository = Mockito.mock(EventRepository.class);
        EventRestController controller = new EventRestController(eventRepository);

        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 19, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 22, 0);

        Mockito.when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateEventRequestDto req = new CreateEventRequestDto(
                organizerId,
                "Music Night 2026",
                "A fun music night",
                start,
                end,
                "Jakarta",
                100
        );

        var res = controller.create(req);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().organizerId()).isEqualTo(organizerId);
        assertThat(res.getBody().name()).isEqualTo("Music Night 2026");
    }

    @Test
    void postCreateEvent_whenEndDateBeforeStartDate_throwsValidationException() {
        UUID organizerId = UUID.randomUUID();

        EventRepository eventRepository = Mockito.mock(EventRepository.class);
        EventRestController controller = new EventRestController(eventRepository);

        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 22, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 19, 0);

        CreateEventRequestDto req = new CreateEventRequestDto(
                organizerId,
                "Music Night 2026",
                "A fun music night",
                start,
                end,
                "Jakarta",
                100
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                com.example.eventticketing.application.exception.ValidationException.class,
                () -> controller.create(req)
        );
    }
}

