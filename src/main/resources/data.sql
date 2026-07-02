INSERT INTO city (id, name, province, population) VALUES (1, 'Toronto', 'Ontario', 3000000);
INSERT INTO city (id, name, province, population) VALUES (2, 'Vancouver', 'British Columbia', 2300000);
INSERT INTO city (id, name, province, population) VALUES (3, 'Calgary', 'Alberta', 1400000);

INSERT INTO airport (id, name, airport_code, city_id) VALUES (1, 'Toronto Pearson International Airport', 'YYZ', 1);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (2, 'Billy Bishop Toronto City Airport', 'YTZ', 1);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (3, 'Vancouver International Airport', 'YVR', 2);
INSERT INTO airport (id, name, airport_code, city_id) VALUES (4, 'Calgary International Airport', 'YYC', 3);

INSERT INTO passenger (id, first_name, last_name, phone_number) VALUES (1, 'Alice', 'Nguyen', '555-0101');
INSERT INTO passenger (id, first_name, last_name, phone_number) VALUES (2, 'Brandon', 'Lee', '555-0202');
INSERT INTO passenger (id, first_name, last_name, phone_number) VALUES (3, 'Carla', 'Patel', '555-0303');
INSERT INTO passenger (id, first_name, last_name, phone_number) VALUES (4, 'David', 'Smith', '555-0404');

INSERT INTO plane (id, type, airline_name, num_of_passengers) VALUES (1, 'Boeing 737', 'Air Canada', 160);
INSERT INTO plane (id, type, airline_name, num_of_passengers) VALUES (2, 'Airbus A320', 'WestJet', 150);
INSERT INTO plane (id, type, airline_name, num_of_passengers) VALUES (3, 'Boeing 777', 'Air Transat', 300);

INSERT INTO plane_airport (plane_id, airport_id) VALUES (1, 1);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (1, 3);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (2, 2);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (2, 4);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (3, 1);
INSERT INTO plane_airport (plane_id, airport_id) VALUES (3, 4);

INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (1, 1);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (1, 2);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (2, 3);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (3, 1);
INSERT INTO plane_passenger (plane_id, passenger_id) VALUES (3, 4);
