# Event-Ticketing-Booking-System-Using-Clean-Architecture-and-Domain-Driven-Design

## How to run the project

### Prerequisites
- Java 17+
- Maven
- PostgreSQL

### Steps
1. Configure PostgreSQL (see section **How to configure PostgreSQL**)
2. Run database migration (see section **How to run database migration**)
3. Start the Spring Boot application

```bash
mvn spring-boot:run
```

Service starts on:
- http://localhost:8080

## How to configure PostgreSQL

This project uses the standard Spring Boot properties defined in:
- `src/main/resources/application.properties`

Expected configuration:
- `spring.datasource.url=jdbc:postgresql://localhost:5432/eventticketing`
- `spring.datasource.username=postgres`
- `spring.datasource.password=postgres`

Flyway migrations are enabled via:
- `spring.flyway.enabled=true`

## How to run database migration

Migrations are handled by **Flyway**.
- Migration scripts are located in: `src/main/resources/db/migration/`
- Example: `V1__init.sql`

### Option A: Automatic migration on startup (recommended)
If `spring.flyway.enabled=true`, migrations run automatically when the app starts.

### Option B: Run migration manually
```bash
mvn flyway:migrate
```

## How to run tests

The test suite uses the dedicated Spring test properties:
- `src/test/resources/application-test.properties`

Run all tests:
```bash
mvn test
```

## List of implemented user stories

- **Create Event**
  - HTTP endpoint: `POST /api/events`
  - Use-case interface: `CreateEventUseCase`

- **Find Event by ID**
  - Use-case interface: `FindEventByIdUseCase`

## List of implemented domain events

Implemented under:
- `src/main/java/com/example/eventticketing/domain/event/event/`

Events:
- `EventCreated`
- `EventPublished`
- `EventCancelled`
- `TicketCategoryCreated`
- `TicketCategoryDisabled`

## List of implemented application service interfaces

Use-case (application service) interfaces are located in:
- `src/main/java/com/example/eventticketing/application/**/usecase/`

Interfaces:
- `CreateEventUseCase`
- `FindEventByIdUseCase`

