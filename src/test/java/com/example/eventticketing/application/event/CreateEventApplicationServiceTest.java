package com.example.eventticketing.application.event;

import com.example.eventticketing.application.event.command.CreateEventCommand;
import com.example.eventticketing.application.event.service.CreateEventApplicationService;
import com.example.eventticketing.application.validation.CreateEventCommandValidator;
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class CreateEventApplicationServiceTest {

    @Test
    void execute_createsEvent_withDraftStatus() {
        EventRepository repo = Mockito.mock(EventRepository.class);
        Mockito.when(repo.save(any(Event.class))).thenAnswer(inv -> {
            Event e = inv.getArgument(0);
            return e;
        });

        CreateEventApplicationService service = new CreateEventApplicationService(repo, new CreateEventCommandValidator());

        UUID organizerId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 19, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 22, 0);

        CreateEventCommand cmd = new CreateEventCommand(
                organizerId,
                "Music Night 2026",
                "A fun music night",
                start,
                end,
                "Jakarta",
                100
        );

        Event created = service.execute(cmd);
        assertThat(created.getName()).isEqualTo("Music Night 2026");
        assertThat(created.getOrganizerId()).isEqualTo(organizerId);
    }
}

