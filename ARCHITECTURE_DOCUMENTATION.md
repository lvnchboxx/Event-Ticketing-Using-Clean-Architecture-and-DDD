# Arsitektur & Alur Coding (Clean Architecture + DDD)

Dokumen ini menjelaskan pembagian layer dan alur eksekusi pada project **eventticketing**.

Struktur package utama di `src/main/java/com/example/eventticketing/`:
- `domain/`  → Domain Layer
- `application/` → Application Layer (use case orchestration, validasi, error)

Di repositori ini belum terlihat folder `infrastructure/` dan `presentation/` secara eksplisit, tetapi layer-layer itu biasanya hadir melalui implementasi teknis (DB/JPA, controllers/entrypoints) yang saat ini kemungkinan besar masih belum ditambahkan/dirapikan dalam struktur folder yang terpisah.

---

## 1) Domain Layer (`domain/`)
**Lokasi:** `src/main/java/com/example/eventticketing/domain/`

**Tujuan:**
- Menyimpan *business logic* inti aplikasi.
- Mendefinisikan *entities*, *value objects*, *domain services*, *domain events*, dan *repository interfaces*.

**Komponen penting (contoh):**
- **Model/Entity & Value Objects**
  - `domain/event/model/Event.java`
  - `domain/event/model/TicketCategory.java`
  - `domain/ticket/model/Ticket.java`
  - `domain/shared/Money.java`
- **Domain Events**
  - `domain/event/event/*` (mis. `EventCreated`, `EventPublished`, dll.)
- **Domain Services**
  - `domain/booking/service/BookingDomainService.java`
  - `domain/ticket/service/TicketCheckInDomainService.java`
  - `domain/refund/service/RefundDomainService.java`
- **Repository Interface (port)**
  - `domain/event/repository/EventRepository.java`
  - `domain/booking/repository/BookingRepository.java`
  - `domain/refund/repository/RefundRepository.java`

**Aturan main:**
- Layer domain **tidak** bergantung pada Spring/MVC/JPA/HTTP.
- Domain memodelkan rule dan state bisnis.

---

## 2) Application Layer (`application/`)
**Lokasi:** `src/main/java/com/example/eventticketing/application/`

**Tujuan:**
- Mengorkestrasi use case (urutan kerja aplikasi).
- Validasi input sebelum masuk domain.
- Menghubungkan *use case* dengan repository port dari domain.

**Komponen penting:**

### 2.1 Command (DTO input use case)
- `application/event/command/CreateEventCommand.java`

### 2.2 Query (DTO input use case)
- `application/event/query/FindEventByIdQuery.java`

---
### Alur Query: `FindEventById`
1. **Query DTO**: `FindEventByIdQuery(UUID id)`
2. **Handler**: `application/event/query/handler/FindEventByIdHandler.java`
   - memanggil `findEventByIdUseCase.execute(query)`
3. **Application Service (Use Case implementation)**: `FindEventByIdApplicationService`
   - `validator.validate(query)`
   - `eventRepository.findById(query.id())`
   - jika tidak ada → `NotFoundException`
4. **Query Validator**: `FindEventByIdQueryValidator`
   - cek `query != null`
   - cek `query.id() != null`
5. **Repository Adapter (Infrastructure)**: `infrastructure/repository/EventRepositoryAdapter`
   - mendelegasikan ke `SpringDataEventJpaRepository.findById(id)`
   - mapping `EventEntity` → domain `Event`


### 2.2 Use Case Interface (boundary aplikasi)
- `application/event/usecase/CreateEventUseCase.java`
  - Mendefinisikan kontrak: `Event execute(CreateEventCommand command);`

### 2.3 Application Service (implementasi use case)
- `application/event/service/CreateEventApplicationService.java`
  - Contoh alur:
    1. `validator.validate(command)`
    2. memanggil `eventRepository.save(new Event(...))`

### 2.4 Handler (entry/orchestrator yang lebih spesifik)
- `application/event/handler/CreateEventHandler.java`
  - Mengambil dependency `EventRepository` + `CommandValidator`
  - Membuat/menjalankan `CreateEventApplicationService`
  - Memanggil `handle(command)` → `createEventUseCase.execute(command)`

### 2.5 Validasi & Exception (support aplikasi)
- `application/validation/CommandValidator.java`
- `application/validation/CreateEventCommandValidator.java`
- `application/exception/ValidationException.java`

**Aturan main:**
- Application layer boleh bergantung pada domain.
- Application layer mengatur *workflow*, bukan menyimpan rule bisnis inti.

---

## 3) Infrastructure Layer 
Di bawah ini adalah “kerangka” Infrastructure yang memenuhi kriteria yang kamu minta:

### 3.0 Tujuan Infrastructure
Infrastructure berisi adaptor teknis untuk menjalankan kebutuhan aplikasi:
- mengakses database (PostgreSQL)
- mengimplementasikan port repository dari Domain
- menyediakan konfigurasi data/JPA



### 3.1 PostgreSQL schema or migrations
Target:
- Tambahkan folder migrasi, misalnya:
  - `src/main/resources/db/migration/`
    - `V1__init.sql`
    - `V2__add_bookings.sql`

Contoh isi `V1__init.sql` (konsep):
- tabel `events`
- tabel `bookings`
- tabel `refunds`

Alternatif:
- Flyway/Liquibase (sesuai preferensi). Idealnya pilih salah satu.



### 3.2 Repository implementations
Target implementasi class yang meng-*implement* port domain:
- `EventRepository` → implementasi teknis (mis. JPA repository / JDBC)
  - contoh: `infrastructure.persistence.EventJpaRepositoryAdapter implements EventRepository`
- `BookingRepository` → implementasi teknis
- `RefundRepository` → implementasi teknis

Catatan:
- Adapter ini bertugas mengubah antara:
  - model domain (mis. `domain/event/model/Event`)
  - model database/JPA entity (mis. entity `EventEntity` bila memakai JPA)



### 3.3 Infrastructure application service implementations
Dalam Clean Architecture, “application service” inti biasanya tetap di `application/` (contoh di repo ini: `CreateEventApplicationService`).

Agar memenuhi kriteria, Infrastructure dapat berisi:
- adaptor untuk kebutuhan teknis dari application layer, misalnya:
  - *transaction boundary* (bila mau dipisahkan)
  - komponen yang membantu repository persistence
  - pemanggilan external services (jika ada)

Bentuk yang direkomendasikan:
- `application/service/*` tetap mengatur workflow
- Infrastructure menyediakan “support teknis” yang dipakai application service, misalnya repository adapter



### 3.4 Database connection configuration
Target:
Tambahkan konfigurasi PostgreSQL di:
- `src/main/resources/application.properties` (atau `application.yml`)

Konfigurasi yang umumnya diperlukan:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.datasource.driver-class-name`
- `spring.jpa.database-platform` (dialect)
- opsi JPA: `ddl-auto` (jika tidak memakai migration), `show-sql`, dll.

Saat ini, file `application.properties` belum berisi konfigurasi PostgreSQL tersebut (hanya ada `spring.application.name`).



## Struktur folder Infrastructure (contoh)
- `src/main/java/com/example/eventticketing/infrastructure/`
  - `config/`
    - DataSource/JPA config (jika tidak sepenuhnya mengandalkan auto-config)
  - `migration/` (opsional; jika bukan di resources)
  - `persistence/`
    - entity JPA untuk mapping domain ⇄ database
  - `repository/`
    - adapter implementasi `EventRepository/BookingRepository/RefundRepository`


## 4) Presentation Layer (`presentation/`)

**Lokasi:**
`src/main/java/com/example/eventticketing/presentation/`

**Tujuan:**

* Menjadi entry point dari luar sistem melalui REST API.
* Menerima HTTP request dari client seperti Postman/Thunder Client.
* Mengubah request JSON menjadi Command atau Query untuk Application Layer.
* Mengembalikan response JSON kepada client.
* Menangani error agar response API lebih jelas.

Presentation Layer tidak berisi business logic utama. Business logic tetap berada di Domain Layer, sedangkan alur use case berada di Application Layer.

---

### 4.1 Struktur Folder Presentation

Struktur yang digunakan:

```text
presentation/
└── rest/
    ├── EventController.java
    ├── GlobalExceptionHandler.java
    ├── request/
    │   └── CreateEventRequest.java
    └── response/
        └── EventResponse.java
```

---

### 4.2 REST Controller

File utama:

```text
presentation/rest/EventController.java
```

`EventController` bertugas menyediakan endpoint REST untuk fitur Event.

Endpoint yang sudah dibuat:

```text
POST /api/events
GET  /api/events/{eventId}
```

Controller tidak langsung mengakses database. Controller hanya memanggil Application Service:

```text
EventController
→ CreateEventApplicationService / FindEventByIdApplicationService
→ EventRepository interface
→ EventRepositoryAdapter
→ SpringDataEventJpaRepository
→ PostgreSQL
```

Dengan alur ini, Presentation Layer tetap terpisah dari Infrastructure Layer.

---

### 4.3 Request DTO

File:

```text
presentation/rest/request/CreateEventRequest.java
```

`CreateEventRequest` digunakan untuk menerima body JSON dari client ketika membuat event baru.

Contoh request JSON:

```json
{
  "organizerId": "12311111-1111-1111-1111-111111111111",
  "name": "Javajazz",
  "description": "Outdoor music event",
  "startDate": "2026-06-10T10:00:00",
  "endDate": "2026-06-10T12:00:00",
  "location": "bekasi",
  "maximumCapacity": 10
}
```

Request DTO ini kemudian diubah menjadi `CreateEventCommand` sebelum dikirim ke Application Layer.

---

### 4.4 Response DTO

File:

```text
presentation/rest/response/EventResponse.java
```

`EventResponse` digunakan untuk mengembalikan data event ke client dalam bentuk JSON.

Contoh response:

```json
{
  "id": "9e7c9a88-6827-4f7c-bc95-a1d05dcb7631",
  "organizerId": "12311111-1111-1111-1111-111111111111",
  "name": "Javajazz",
  "description": "Outdoor music event",
  "startDate": "2026-06-10T10:00:00",
  "endDate": "2026-06-10T12:00:00",
  "location": "bekasi",
  "maximumCapacity": 10,
  "status": "DRAFT"
}
```

Response DTO membantu agar data yang dikirim ke client lebih terkontrol dan tidak langsung mengekspos seluruh object domain.

---

### 4.5 Global Exception Handler

File:

```text
presentation/rest/GlobalExceptionHandler.java
```

`GlobalExceptionHandler` digunakan untuk menangani error dari aplikasi dan mengubahnya menjadi response HTTP yang jelas.

Contoh error yang ditangani:

```text
BusinessRuleException → 400 Bad Request
NotFoundException     → 404 Not Found
Exception umum        → 500 Internal Server Error
```

Contoh response ketika event tidak ditemukan:

```json
{
  "message": "Event not found. id=..."
}
```

Dengan ini, API tidak hanya mengembalikan error default dari Spring, tetapi memberikan pesan error yang lebih mudah dipahami.

---

### 4.6 Alur Endpoint: Create Event

Endpoint:

```text
POST /api/events
```

Alur eksekusi:

```text
Client/Postman
→ EventController.createEvent()
→ CreateEventRequest
→ CreateEventCommand
→ CreateEventApplicationService.execute(command)
→ CreateEventCommandValidator.validate(command)
→ new Event(...)
→ EventRepository.save(event)
→ EventRepositoryAdapter
→ SpringDataEventJpaRepository
→ PostgreSQL table events
→ EventResponse
→ JSON response
```

Penjelasan:

1. Client mengirim JSON request.
2. Controller menerima request sebagai `CreateEventRequest`.
3. Controller membuat `CreateEventCommand`.
4. Application Service menjalankan use case pembuatan event.
5. Domain Entity `Event` dibuat dengan status awal `DRAFT`.
6. Repository menyimpan event ke PostgreSQL.
7. Controller mengembalikan `EventResponse`.

---

### 4.7 Alur Endpoint: Find Event By ID

Endpoint:

```text
GET /api/events/{eventId}
```

Alur eksekusi:

```text
Client/Postman
→ EventController.findEventById()
→ FindEventByIdQuery
→ FindEventByIdApplicationService.execute(query)
→ FindEventByIdQueryValidator.validate(query)
→ EventRepository.findById(id)
→ EventRepositoryAdapter
→ SpringDataEventJpaRepository
→ PostgreSQL table events
→ EventResponse
→ JSON response
```

Penjelasan:

1. Client mengirim request dengan `eventId` pada URL.
2. Controller membuat `FindEventByIdQuery`.
3. Application Service memvalidasi query.
4. Repository mencari data event berdasarkan ID.
5. Jika data ditemukan, event dikembalikan sebagai `EventResponse`.
6. Jika data tidak ditemukan, aplikasi mengembalikan `404 Not Found`.

---

### 4.8 Bukti Integrasi

Presentation Layer sudah terhubung dengan layer lain melalui endpoint berikut:

```text
POST /api/events       → berhasil membuat event dan menyimpan ke PostgreSQL
GET /api/events/{id}   → berhasil mengambil event dari PostgreSQL
```

Bukti pengujian:

* `POST /api/events` menghasilkan status `200 OK`.
<img width="885" height="852" alt="image" src="https://github.com/user-attachments/assets/a2fe9a9b-154c-4de5-86be-0368575e1eaa" />

* Response mengembalikan data event dengan UUID.
  <img width="837" height="867" alt="image" src="https://github.com/user-attachments/assets/dafbccc3-6633-49f3-9ffa-41d9a2d86c97" />

* Data event muncul di tabel `events` pada PostgreSQL.
<img width="852" height="843" alt="image" src="https://github.com/user-attachments/assets/4aadb33a-cdd8-4c18-bb09-ed8b2042d16b" />

* `GET /api/events/{id}` berhasil mengambil data menggunakan UUID yang sama.
  <img width="832" height="862" alt="image" src="https://github.com/user-attachments/assets/2a317650-6890-4b10-9a57-98200a3da9f2" />


Ini membuktikan bahwa alur Clean Architecture berjalan:

```text
Presentation Layer
→ Application Layer
→ Domain Layer
→ Infrastructure Layer
→ PostgreSQL Database

```

---

### 4.9 Aturan Main Presentation Layer

* Controller hanya menerima request dan mengembalikan response.
* Controller tidak menyimpan business logic.
* Controller tidak mengakses database secara langsung.
* Request dari client dipetakan menjadi Command atau Query.
* Response dari domain dipetakan menjadi Response DTO.
* Error ditangani melalui `GlobalExceptionHandler`.


## ) Ringkasan Mapping Layer → Folder
- **Domain Layer**
  - `src/main/java/.../domain/**`
- **Application Layer**
  - `src/main/java/.../application/**`
- **Infrastructure Layer**
  - `src/main/java/.../infrastructure/**`
  - `src/main/resources/db/migration/**`
- **Presentation Layer**
  - `src/main/java/.../presentation/**`




## ) Checklist Gaya Coding (yang disarankan)
- Domain:
  - hindari dependensi Spring/JPA/HTTP
  - buat entity/value object domain yang “pure”
- Application:
  - gunakan Command/UseCase pattern
  - validasi input di application
  - simpan orchestrasi workflow di application service/handler
- Infrastructure:
  - implementasikan interface repository dari domain



