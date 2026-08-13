-- =============================================================================
-- Stored Procedure: sp_seed_user_test_data
-- =============================================================================
-- PURPOSE:
--   After a new user_account is created via the admin portal, call this SP
--   with the user_account.id to populate realistic end-to-end test data:
--     • A passenger profile linked conceptually to the user
--     • Flights (via the flight table) across multiple airports
--     • Bookings in varying statuses (BOOKED, CHECKED_IN, COMPLETED, CANCELLED)
--       with baggage, gates, seats — everything needed to test check-in,
--       baggage tracking, and travel history features
--     • plane_passenger join-table entries linking the passenger to aircraft
--
-- USAGE:
--   CALL sp_seed_user_test_data(<user_account_id>);
--
-- EXAMPLE:
--   -- After creating user "JSmith" on the portal:
--   CALL sp_seed_user_test_data(5);
--
-- NOTES:
--   • Uses existing seeded reference data (airports, planes, gates, airlines).
--   • Safe to call multiple times — generates new unique booking references
--     each invocation using UUID_SHORT() to guarantee uniqueness.
--   • All IDs are resolved dynamically by looking up existing reference data,
--     so it works against any environment with the standard seed data.
-- =============================================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_seed_user_test_data$$

CREATE PROCEDURE sp_seed_user_test_data(IN p_user_id BIGINT)
BEGIN
    -- =========================================================================
    -- Variable declarations
    -- =========================================================================
    DECLARE v_username      VARCHAR(255);
    DECLARE v_email         VARCHAR(255);
    DECLARE v_passenger_id  BIGINT;

    -- Airport IDs (resolved dynamically from seeded data)
    DECLARE v_yyz_id  BIGINT;   -- Toronto Pearson
    DECLARE v_ytz_id  BIGINT;   -- Billy Bishop
    DECLARE v_yvr_id  BIGINT;   -- Vancouver
    DECLARE v_yyc_id  BIGINT;   -- Calgary
    DECLARE v_yul_id  BIGINT;   -- Montreal

    -- Plane IDs
    DECLARE v_plane_737_id  BIGINT;  -- Boeing 737 MAX 8
    DECLARE v_plane_320_id  BIGINT;  -- Airbus A320-200
    DECLARE v_plane_e195_id BIGINT;  -- Embraer E195-E2
    DECLARE v_plane_330_id  BIGINT;  -- Airbus A330-300

    -- Airline IDs
    DECLARE v_ac_id   BIGINT;  -- Air Canada
    DECLARE v_ws_id   BIGINT;  -- WestJet
    DECLARE v_pd_id   BIGINT;  -- Porter Airlines
    DECLARE v_ts_id   BIGINT;  -- Air Transat

    -- Gate IDs (grab first few from seeded gates)
    DECLARE v_gate1_id BIGINT;
    DECLARE v_gate2_id BIGINT;
    DECLARE v_gate3_id BIGINT;
    DECLARE v_gate4_id BIGINT;

    -- Booking IDs (for reference after insert)
    DECLARE v_booking1_id BIGINT;
    DECLARE v_booking2_id BIGINT;
    DECLARE v_booking3_id BIGINT;
    DECLARE v_booking4_id BIGINT;
    DECLARE v_booking5_id BIGINT;
    DECLARE v_booking6_id BIGINT;

    -- Unique reference prefix
    DECLARE v_ref_prefix VARCHAR(20);

    -- =========================================================================
    -- 1. Validate the user exists
    -- =========================================================================
    SELECT username, email
      INTO v_username, v_email
      FROM user_account
     WHERE id = p_user_id;

    IF v_username IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'ERROR: user_account not found for the given ID.';
    END IF;

    -- Generate a short unique prefix for booking references
    SET v_ref_prefix = UPPER(LEFT(v_username, 3));

    -- =========================================================================
    -- 2. Resolve existing reference data IDs
    -- =========================================================================

    -- Airports
    SELECT id INTO v_yyz_id FROM airport WHERE airport_code = 'YYZ' LIMIT 1;
    SELECT id INTO v_ytz_id FROM airport WHERE airport_code = 'YTZ' LIMIT 1;
    SELECT id INTO v_yvr_id FROM airport WHERE airport_code = 'YVR' LIMIT 1;
    SELECT id INTO v_yyc_id FROM airport WHERE airport_code = 'YYC' LIMIT 1;
    SELECT id INTO v_yul_id FROM airport WHERE airport_code = 'YUL' LIMIT 1;

    -- Planes (by type)
    SELECT id INTO v_plane_737_id  FROM plane WHERE type LIKE '%737%'  LIMIT 1;
    SELECT id INTO v_plane_320_id  FROM plane WHERE type LIKE '%A320%' LIMIT 1;
    SELECT id INTO v_plane_e195_id FROM plane WHERE type LIKE '%E195%' LIMIT 1;
    SELECT id INTO v_plane_330_id  FROM plane WHERE type LIKE '%A330%' LIMIT 1;

    -- Airlines (by code)
    SELECT id INTO v_ac_id FROM airline WHERE code = 'AC' LIMIT 1;
    SELECT id INTO v_ws_id FROM airline WHERE code = 'WS' LIMIT 1;
    SELECT id INTO v_pd_id FROM airline WHERE code = 'PD' LIMIT 1;
    SELECT id INTO v_ts_id FROM airline WHERE code = 'TS' LIMIT 1;

    -- Gates (grab first 4 in order)
    SELECT id INTO v_gate1_id FROM gate ORDER BY id ASC LIMIT 1;
    SELECT id INTO v_gate2_id FROM gate ORDER BY id ASC LIMIT 1 OFFSET 1;
    SELECT id INTO v_gate3_id FROM gate ORDER BY id ASC LIMIT 1 OFFSET 2;
    SELECT id INTO v_gate4_id FROM gate ORDER BY id ASC LIMIT 1 OFFSET 3;

    -- =========================================================================
    -- 3. Create a passenger profile for this user
    -- =========================================================================
    INSERT INTO passenger (first_name, last_name, phone_number, email, passport_number)
    VALUES (
        v_username,
        CONCAT('TestUser-', p_user_id),
        CONCAT('709-555-', LPAD(p_user_id, 4, '0')),
        COALESCE(v_email, CONCAT(LOWER(v_username), '@airport.com')),
        CONCAT('PP', LPAD(p_user_id, 7, '0'))
    );
    SET v_passenger_id = LAST_INSERT_ID();

    -- =========================================================================
    -- 4. Create flights in the flight table
    --    (6 flights across different routes, statuses, and timeframes)
    -- =========================================================================

    -- Flight 1: Tomorrow morning, YYZ → YVR (ON_TIME)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('AC', 100 + p_user_id),
        DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR,
        DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 13 HOUR,
        'ON_TIME',
        v_yyz_id, v_yvr_id, v_plane_737_id
    );

    -- Flight 2: Tomorrow afternoon, YVR → YYC (DELAYED)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('WS', 200 + p_user_id),
        DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 15 HOUR,
        DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 16 HOUR + INTERVAL 30 MINUTE,
        'DELAYED',
        v_yvr_id, v_yyc_id, v_plane_320_id
    );

    -- Flight 3: 3 days ago, YUL → YYZ (LANDED — past travel)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('PD', 300 + p_user_id),
        DATE_SUB(CURDATE(), INTERVAL 3 DAY) + INTERVAL 10 HOUR,
        DATE_SUB(CURDATE(), INTERVAL 3 DAY) + INTERVAL 11 HOUR + INTERVAL 15 MINUTE,
        'LANDED',
        v_yul_id, v_yyz_id, v_plane_e195_id
    );

    -- Flight 4: Last week, YYZ → YUL (LANDED — past travel)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('AC', 400 + p_user_id),
        DATE_SUB(CURDATE(), INTERVAL 7 DAY) + INTERVAL 14 HOUR,
        DATE_SUB(CURDATE(), INTERVAL 7 DAY) + INTERVAL 15 HOUR + INTERVAL 20 MINUTE,
        'LANDED',
        v_yyz_id, v_yul_id, v_plane_737_id
    );

    -- Flight 5: Day after tomorrow, YYC → YTZ (ON_TIME)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('WS', 500 + p_user_id),
        DATE_ADD(CURDATE(), INTERVAL 2 DAY) + INTERVAL 9 HOUR,
        DATE_ADD(CURDATE(), INTERVAL 2 DAY) + INTERVAL 13 HOUR + INTERVAL 45 MINUTE,
        'ON_TIME',
        v_yyc_id, v_ytz_id, v_plane_330_id
    );

    -- Flight 6: Cancelled flight (was for next week)
    INSERT INTO flight (flight_number, departure_time, arrival_time, status,
                        departure_airport_id, arrival_airport_id, plane_id)
    VALUES (
        CONCAT('TS', 600 + p_user_id),
        DATE_ADD(CURDATE(), INTERVAL 5 DAY) + INTERVAL 7 HOUR,
        DATE_ADD(CURDATE(), INTERVAL 5 DAY) + INTERVAL 12 HOUR,
        'CANCELLED',
        v_yyz_id, v_yvr_id, v_plane_330_id
    );

    -- =========================================================================
    -- 5. Create bookings (linked to passenger, planes, airlines, gates)
    --    These are the records the portal uses for check-in, baggage, etc.
    -- =========================================================================

    -- Booking 1: Upcoming flight — BOOKED (ready for check-in)
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('AC', 100 + p_user_id),
        v_passenger_id, v_plane_737_id, v_ac_id,
        v_yyz_id, v_yvr_id,
        v_gate1_id,
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 13 HOUR, '%Y-%m-%d %H:%i'),
        '14A', 2, 'BOOKED', NULL
    );
    SET v_booking1_id = LAST_INSERT_ID();

    -- Booking 2: Upcoming flight — BOOKED (delayed, ready for check-in)
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('WS', 200 + p_user_id),
        v_passenger_id, v_plane_320_id, v_ws_id,
        v_yvr_id, v_yyc_id,
        v_gate3_id,
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 15 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY) + INTERVAL 16 HOUR + INTERVAL 30 MINUTE, '%Y-%m-%d %H:%i'),
        '22C', 1, 'BOOKED', NULL
    );
    SET v_booking2_id = LAST_INSERT_ID();

    -- Booking 3: Past travel — CHECKED_IN (completed trip, checked in)
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('PD', 300 + p_user_id),
        v_passenger_id, v_plane_e195_id, v_pd_id,
        v_yul_id, v_yyz_id,
        v_gate2_id,
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY) + INTERVAL 10 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY) + INTERVAL 11 HOUR + INTERVAL 15 MINUTE, '%Y-%m-%d %H:%i'),
        '8F', 3, 'CHECKED_IN',
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 DAY) + INTERVAL 7 HOUR, '%Y-%m-%d %H:%i')
    );
    SET v_booking3_id = LAST_INSERT_ID();

    -- Booking 4: Past travel — COMPLETED (last week trip)
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('AC', 400 + p_user_id),
        v_passenger_id, v_plane_737_id, v_ac_id,
        v_yyz_id, v_yul_id,
        v_gate1_id,
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY) + INTERVAL 14 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY) + INTERVAL 15 HOUR + INTERVAL 20 MINUTE, '%Y-%m-%d %H:%i'),
        '3B', 1, 'COMPLETED',
        DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY) + INTERVAL 12 HOUR, '%Y-%m-%d %H:%i')
    );
    SET v_booking4_id = LAST_INSERT_ID();

    -- Booking 5: Future trip — BOOKED (day after tomorrow)
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('WS', 500 + p_user_id),
        v_passenger_id, v_plane_330_id, v_ws_id,
        v_yyc_id, v_ytz_id,
        v_gate4_id,
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 2 DAY) + INTERVAL 9 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 2 DAY) + INTERVAL 13 HOUR + INTERVAL 45 MINUTE, '%Y-%m-%d %H:%i'),
        '31A', 2, 'BOOKED', NULL
    );
    SET v_booking5_id = LAST_INSERT_ID();

    -- Booking 6: Cancelled booking
    INSERT INTO bookings (booking_reference, flight_number, passenger_id, plane_id,
                          airline_id, origin_airport_id, destination_airport_id,
                          gate_id, departure_time, arrival_time,
                          seat_number, baggage_count, status, check_in_time)
    VALUES (
        CONCAT(v_ref_prefix, '-', UUID_SHORT()),
        CONCAT('TS', 600 + p_user_id),
        v_passenger_id, v_plane_330_id, v_ts_id,
        v_yyz_id, v_yvr_id,
        v_gate1_id,
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 5 DAY) + INTERVAL 7 HOUR, '%Y-%m-%d %H:%i'),
        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL 5 DAY) + INTERVAL 12 HOUR, '%Y-%m-%d %H:%i'),
        '19D', 0, 'CANCELLED', NULL
    );
    SET v_booking6_id = LAST_INSERT_ID();

    -- =========================================================================
    -- 6. Link passenger to planes via plane_passenger join table
    -- =========================================================================
    INSERT IGNORE INTO plane_passenger (plane_id, passenger_id) VALUES (v_plane_737_id,  v_passenger_id);
    INSERT IGNORE INTO plane_passenger (plane_id, passenger_id) VALUES (v_plane_320_id,  v_passenger_id);
    INSERT IGNORE INTO plane_passenger (plane_id, passenger_id) VALUES (v_plane_e195_id, v_passenger_id);
    INSERT IGNORE INTO plane_passenger (plane_id, passenger_id) VALUES (v_plane_330_id,  v_passenger_id);

    -- =========================================================================
    -- 7. Output summary
    -- =========================================================================
    SELECT
        CONCAT('✅ Test data seeded for user "', v_username, '" (user_account.id = ', p_user_id, ')') AS result,
        v_passenger_id  AS passenger_id,
        6               AS flights_created,
        6               AS bookings_created,
        4               AS plane_links_created;

    -- Detail view of created bookings
    SELECT
        b.id              AS booking_id,
        b.booking_reference,
        b.flight_number,
        b.status,
        b.seat_number,
        b.baggage_count,
        o.airport_code    AS origin,
        d.airport_code    AS destination,
        b.departure_time,
        b.arrival_time,
        al.name           AS airline,
        p.type            AS aircraft
    FROM bookings b
        JOIN airport o  ON b.origin_airport_id      = o.id
        JOIN airport d  ON b.destination_airport_id  = d.id
        LEFT JOIN airline al ON b.airline_id         = al.id
        LEFT JOIN plane  p   ON b.plane_id           = p.id
    WHERE b.passenger_id = v_passenger_id
    ORDER BY b.departure_time;

END$$

DELIMITER ;
