-- Extended Database Seed Script for MySQL

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE bookings;
TRUNCATE TABLE gate;
TRUNCATE TABLE plane_passenger;
TRUNCATE TABLE plane_airport;
TRUNCATE TABLE plane;
TRUNCATE TABLE passenger;
TRUNCATE TABLE airline;
TRUNCATE TABLE airport;
TRUNCATE TABLE city;
TRUNCATE TABLE user_account;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Seed Cities
INSERT INTO city (id, name, province, population) VALUES (1, 'Toronto', 'Ontario', 3000000);
INSERT INTO city (id, name, province, population) VALUES (2, 'Vancouver', 'British Columbia', 2300000);
INSERT INTO city (id, name, province, population) VALUES (3, 'Calgary', 'Alberta', 1400000);
INSERT INTO city (id, name, province, population) VALUES (4, 'Montreal', 'Quebec', 1800000);
INSERT INTO city (id, name, province, population) VALUES (5, 'St. Johns', 'Newfoundland', 110000);

-- 2. Seed Airports
INSERT INTO airport (id, name, airport_code, city_id) VALUES (1, 'Toronto Pearson International Airport', 'YYZ', 1);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (2, 'Billy Bishop Toronto City Airport', 'YTZ', 1);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (3, 'Vancouver International Airport', 'YVR', 2);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (4, 'Calgary International Airport', 'YYC', 3);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (5, 'Montreal-Trudeau International Airport', 'YUL', 4);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (6, 'St. Johns International Airport', 'YYT', 5);

-- 3. Seed Airlines
INSERT INTO airline (id, name, code) VALUES (1, 'Air Canada', 'AC');
INSERT INTO airline (id, name, code) VALUES (2, 'WestJet', 'WS');
INSERT INTO airline (id, name, code) VALUES (3, 'Porter Airlines', 'PD');
INSERT INTO airline (id, name, code) VALUES (4, 'Air Transat', 'TS');
INSERT INTO airline (id, name, code) VALUES (5, 'Delta Air Lines', 'DL');

-- 4. Seed Passengers
INSERT INTO passenger (id, first_name, last_name, phone_number, email, passport_number) VALUES (1, 'Alice', 'Nguyen', '555-0101', 'alice.nguyen@example.com', 'CAN123456');
INSERT INTO passenger (id, first_name, last_name, phone_number, email, passport_number) VALUES (2, 'Brandon', 'Lee', '555-0202', 'brandon.lee@example.com', 'CAN234567');
INSERT INTO passenger (id, first_name, last_name, phone_number, email, passport_number) VALUES (3, 'Carla', 'Patel', '555-0303', 'carla.patel@example.com', 'CAN345678');
INSERT INTO passenger (id, first_name, last_name, phone_number, email, passport_number) VALUES (4, 'David', 'Smith', '555-0404', 'david.smith@example.com', 'CAN456789');
INSERT INTO passenger (id, first_name, last_name, phone_number, email, passport_number) VALUES (5, 'Keith', 'Bishop', '709-786-5464', 'keith.bishop@example.com', 'CAN567890');

-- 5. Seed Planes
INSERT INTO plane (id, type, airline_name, airline_id, num_of_passengers) VALUES (1, 'Boeing 737 MAX 8', 'Air Canada', 1, 169);
INSERT INTO plane (id, type, airline_name, airline_id, num_of_passengers) VALUES (2, 'Airbus A320-200', 'WestJet', 2, 174);
INSERT INTO plane (id, type, airline_name, airline_id, num_of_passengers) VALUES (3, 'Embraer E195-E2', 'Porter Airlines', 3, 132);
INSERT INTO plane (id, type, airline_name, airline_id, num_of_passengers) VALUES (4, 'Airbus A330-300', 'Air Transat', 4, 345);

-- 6. Seed Gates
INSERT INTO gate (id, gate_code, airport_id) VALUES (1, 'A12', 1);
INSERT INTO gate (id, gate_code, airport_id) VALUES (2, 'A14', 1);
INSERT INTO gate (id, gate_code, airport_id) VALUES (3, 'B20', 1);
INSERT INTO gate (id, gate_code, airport_id) VALUES (4, 'Gate 1', 2);
INSERT INTO gate (id, gate_code, airport_id) VALUES (5, 'C42', 3);
INSERT INTO gate (id, gate_code, airport_id) VALUES (6, 'C44', 3);
INSERT INTO gate (id, gate_code, airport_id) VALUES (7, 'D10', 4);
INSERT INTO gate (id, gate_code, airport_id) VALUES (8, 'E05', 5);
INSERT INTO gate (id, gate_code, airport_id) VALUES (9, 'Gate 3', 6);

-- 7. Seed Plane-Airport relationships
INSERT INTO plane_airport (plane_id, airport_id) VALUES (1, 1);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (1, 3);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (2, 2);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (2, 4);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (3, 2);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (3, 5);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (4, 1);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (4, 5);

-- 8. Seed Plane-Passenger relationships
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (1, 1);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (1, 2);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (2, 3);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (3, 4);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (4, 5);

-- 9. Seed Bookings / Flights (Arrivals & Departures)
INSERT INTO bookings (id, booking_reference, flight_number, passenger_id, plane_id, airline_id, origin_airport_id, destination_airport_id, gate_id, departure_time, arrival_time, seat_number, baggage_count, status, check_in_time) 
VALUES (1, 'BK-AC101-001', 'AC101', 1, 1, 1, 1, 3, 1, '2026-08-10 08:00', '2026-08-10 10:15', '12A', 1, 'ON_TIME', '2026-08-10 06:30');

INSERT INTO bookings (id, booking_reference, flight_number, passenger_id, plane_id, airline_id, origin_airport_id, destination_airport_id, gate_id, departure_time, arrival_time, seat_number, baggage_count, status, check_in_time) 
VALUES (2, 'BK-WS204-002', 'WS204', 2, 2, 2, 3, 1, 5, '2026-08-10 09:30', '2026-08-10 17:15', '15C', 2, 'DELAYED', '2026-08-10 08:00');

INSERT INTO bookings (id, booking_reference, flight_number, passenger_id, plane_id, airline_id, origin_airport_id, destination_airport_id, gate_id, departure_time, arrival_time, seat_number, baggage_count, status, check_in_time) 
VALUES (3, 'BK-PD305-003', 'PD305', 3, 3, 3, 2, 5, 4, '2026-08-10 11:00', '2026-08-10 12:20', '04F', 1, 'BOARDING', '2026-08-10 10:15');

INSERT INTO bookings (id, booking_reference, flight_number, passenger_id, plane_id, airline_id, origin_airport_id, destination_airport_id, gate_id, departure_time, arrival_time, seat_number, baggage_count, status, check_in_time) 
VALUES (4, 'BK-TS401-004', 'TS401', 4, 4, 4, 5, 1, 8, '2026-08-10 13:45', '2026-08-10 15:10', '22D', 2, 'SCHEDULED', NULL);

INSERT INTO bookings (id, booking_reference, flight_number, passenger_id, plane_id, airline_id, origin_airport_id, destination_airport_id, gate_id, departure_time, arrival_time, seat_number, baggage_count, status, check_in_time) 
VALUES (5, 'BK-AC602-005', 'AC602', 5, 1, 1, 6, 1, 9, '2026-08-10 16:20', '2026-08-10 18:45', '08B', 1, 'LANDED', '2026-08-10 15:00');

-- 10. Seed User Accounts (For authentication)
INSERT INTO user_account (id, username, password_hash, email) VALUES (1, 'admin', '$2a$10$9zT7zR/8Xg9wY8u.z9Y9U.Z.1z9z9z9z9z9z9z9z9z9z9', 'admin@airport.com');
