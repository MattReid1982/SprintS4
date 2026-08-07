-- Schema DDL Script for MySQL Database Extension

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS gate;
DROP TABLE IF EXISTS plane_passenger;
DROP TABLE IF EXISTS plane_airport;
DROP TABLE IF EXISTS plane;
DROP TABLE IF EXISTS passenger;
DROP TABLE IF EXISTS airline;
DROP TABLE IF EXISTS airport;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS user_account;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. City table
CREATE TABLE city (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    province VARCHAR(255),
    population INT
);

-- 2. Airport table
CREATE TABLE airport (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    airport_code VARCHAR(10) NOT NULL UNIQUE,
    city_id BIGINT,
    CONSTRAINT fk_airport_city FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE SET NULL
);

-- 3. Passenger table
CREATE TABLE passenger (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    email VARCHAR(255),
    passport_number VARCHAR(255)
);

-- 4. Airline table (New Table)
CREATE TABLE airline (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE
);

-- 5. Plane table (Extended with airline_id foreign key)
CREATE TABLE plane (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    airline_name VARCHAR(255),
    airline_id BIGINT,
    num_of_passengers INT,
    CONSTRAINT fk_plane_airline FOREIGN KEY (airline_id) REFERENCES airline(id) ON DELETE SET NULL
);

-- 6. Gate table (New Table)
CREATE TABLE gate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gate_code VARCHAR(50) NOT NULL,
    airport_id BIGINT NOT NULL,
    CONSTRAINT fk_gate_airport FOREIGN KEY (airport_id) REFERENCES airport(id) ON DELETE CASCADE
);

-- 7. Bookings / Flights table (New Extended Table)
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_reference VARCHAR(255) UNIQUE,
    flight_number VARCHAR(50) NOT NULL,
    passenger_id BIGINT,
    plane_id BIGINT,
    airline_id BIGINT,
    origin_airport_id BIGINT NOT NULL,
    destination_airport_id BIGINT NOT NULL,
    gate_id BIGINT,
    departure_time VARCHAR(255),
    arrival_time VARCHAR(255),
    seat_number VARCHAR(20),
    baggage_count INT DEFAULT 0,
    status VARCHAR(50) DEFAULT 'BOOKED',
    check_in_time VARCHAR(255),
    CONSTRAINT fk_booking_passenger FOREIGN KEY (passenger_id) REFERENCES passenger(id) ON DELETE SET NULL,
    CONSTRAINT fk_booking_plane FOREIGN KEY (plane_id) REFERENCES plane(id) ON DELETE SET NULL,
    CONSTRAINT fk_booking_airline FOREIGN KEY (airline_id) REFERENCES airline(id) ON DELETE SET NULL,
    CONSTRAINT fk_booking_origin_airport FOREIGN KEY (origin_airport_id) REFERENCES airport(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_dest_airport FOREIGN KEY (destination_airport_id) REFERENCES airport(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_gate FOREIGN KEY (gate_id) REFERENCES gate(id) ON DELETE SET NULL
);

-- 8. Plane - Airport join table
CREATE TABLE plane_airport (
    plane_id BIGINT NOT NULL,
    airport_id BIGINT NOT NULL,
    PRIMARY KEY (plane_id, airport_id),
    CONSTRAINT fk_pa_plane FOREIGN KEY (plane_id) REFERENCES plane(id) ON DELETE CASCADE,
    CONSTRAINT fk_pa_airport FOREIGN KEY (airport_id) REFERENCES airport(id) ON DELETE CASCADE
);

-- 9. Plane - Passenger join table
CREATE TABLE plane_passenger (
    plane_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    PRIMARY KEY (plane_id, passenger_id),
    CONSTRAINT fk_pp_plane FOREIGN KEY (plane_id) REFERENCES plane(id) ON DELETE CASCADE,
    CONSTRAINT fk_pp_passenger FOREIGN KEY (passenger_id) REFERENCES passenger(id) ON DELETE CASCADE
);

-- 10. User Account table (New Table for authentication)
CREATE TABLE user_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);
