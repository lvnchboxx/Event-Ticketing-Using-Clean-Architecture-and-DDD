-- Flyway baseline schema for Event Ticketing
-- Note: Run with Flyway or any migration mechanism.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS events (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    organizer_id uuid NOT NULL,
    name varchar(255) NOT NULL,
    description text,
    start_date timestamp NOT NULL,
    end_date timestamp NOT NULL,
    location varchar(255),
    maximum_capacity integer NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id uuid NOT NULL,
    event_id uuid NOT NULL,
    status varchar(50) NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now(),
    UNIQUE(customer_id, event_id)
);

CREATE TABLE IF NOT EXISTS refunds (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id uuid NOT NULL,
    status varchar(50) NOT NULL,
    amount numeric(19,2) NOT NULL,
    created_at timestamp NOT NULL DEFAULT now()
);

