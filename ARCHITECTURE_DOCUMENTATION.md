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

## 3) Infrastructure Layer (dibuatkan/diupayakan sesuai kriteria)
Infrastructure layer pada project ini berisi adaptor teknis untuk:
- skema/migrations PostgreSQL
- implementasi repository (port dari Domain)
- adaptor/potongan teknis yang dibutuhkan oleh Application
- konfigurasi koneksi database

(Contoh implementasi sudah ditambahkan di repo untuk bagian Event.)

Kriteria Infrastructure layer untuk project ini adalah sebagai berikut:


### 3.1 PostgreSQL schema or migrations
Yang dibutuhkan:
- Mekanisme pembuatan struktur tabel untuk entity domain (contoh: `events`, `bookings`, `refunds`).
- Implementasi dapat berupa:
  - **Flyway** (`db/migration/V1__init.sql`, dst.), atau
  - **Liquibase**, atau
  - Skrip SQL manual.

### 3.2 Repository implementations (implementasi port dari Domain)
Yang dibutuhkan:
- class yang **mengimplementasikan interface port** di domain:
  - `domain/event/repository/EventRepository`
  - `domain/booking/repository/BookingRepository`
  - `domain/refund/repository/RefundRepository`

Contoh bentuk implementasi:
- `EventRepositoryImpl implements EventRepository`
  - melakukan operasi persistensi ke PostgreSQL melalui JPA/JdbcTemplate

### 3.3 Application service implementations
Di project ini, implementasi use case untuk `CreateEvent` sudah ada di `application/event/service/CreateEventApplicationService.java` (jadi secara current repo, bagian ini belum berpindah ke Infrastructure).

Kriteria yang diinginkan:
- Infrastructure layer dapat berisi adaptor teknis yang membantu application service (misal transaction boundary, repository impl, client HTTP, dsb.)
- Bila ada use case yang benar-benar bergantung pada detail teknis, maka implementasinya bisa dipisah menjadi adapter (sesuai gaya proyek).

### 3.4 Database connection configuration
Yang dibutuhkan:
- konfigurasi koneksi PostgreSQL (host, port, db-name, username, password)
- konfigurasi JPA (DDL strategy, dialect, show-sql, dll.)

Saat ini yang terlihat baru:
- `src/main/resources/application.properties` hanya berisi `spring.application.name=eventticketing` (belum ada konfigurasi PostgreSQL lengkap).



## 3) Infrastructure Layer (Target yang disarankan, dengan 4 kriteria)
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







## 4) Presentation Layer 

## ) Ringkasan Mapping Layer → Folder
- **Domain Layer**
  - `src/main/java/.../domain/**`
- **Application Layer**
  - `src/main/java/.../application/**`
- **Infrastructure Layer (belum terlihat eksplisit)**
  - ideal: `src/main/java/.../infrastructure/**`




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



