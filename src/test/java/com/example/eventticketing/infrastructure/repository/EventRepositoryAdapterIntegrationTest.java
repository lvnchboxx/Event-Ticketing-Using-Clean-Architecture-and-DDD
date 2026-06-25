
import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = com.example.eventticketing.EventticketingApplication.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.test.context.TestPropertySource(locations = "classpath:application-test.properties")
class EventRepositoryAdapterIntegrationTest {

    @Autowired
    EventRepository eventRepository;


    @Test
    void save_and_findById_persists_to_database() {
        UUID organizerId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 19, 0);

        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 22, 0);

        Event event = new Event(
                organizerId,
                "Music Night 2026",
                "A fun music night",
                start,
                end,
                "Jakarta",
                100
        );

        // Event domain generates its own UUID inside the constructor,
        // so saved id must be derived from the same instance.
        UUID expectedId = event.getId();

        Event saved = eventRepository.save(event);


        Optional<Event> loaded = eventRepository.findById(expectedId);
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getName()).isEqualTo("Music Night 2026");
        assertThat(loaded.get().getLocation()).isEqualTo("Jakarta");
    }
}

