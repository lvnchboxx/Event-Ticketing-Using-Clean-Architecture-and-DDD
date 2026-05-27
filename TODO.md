# TODO

## Application Layer (Clean Architecture + DDD) Refactor

- [x] Step 1: Refactor `CreateEventHandler` menjadi application service dan pindahkan/rapikan package.

- [x] Step 2: Tambahkan interface `CreateEventUseCase` (command side).

- [x] Step 3: Tambahkan validation untuk `CreateEventCommand`:
  - [x] Buat `CommandValidator<T>`
  - [x] Buat `CreateEventCommandValidator`

- [x] Step 4: Tambahkan exception application layer:
  - [x] `ValidationException`

- [x] Step 6: Jalankan `mvnw test` untuk memastikan semua tetap lolos.


