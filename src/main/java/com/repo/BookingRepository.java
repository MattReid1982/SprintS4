package com.repo;

import com.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Booking entity CRUD and custom query operations.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds a booking by its unique reference code.
     *
     * @param bookingReference booking reference string
     * @return optional booking
     */
    Optional<Booking> findByBookingReference(String bookingReference);

    /**
     * Finds all bookings for a given flight number.
     *
     * @param flightNumber flight number
     * @return list of bookings
     */
    List<Booking> findByFlightNumber(String flightNumber);

    /**
     * Finds all bookings by status.
     *
     * @param status booking status
     * @return list of bookings
     */
    List<Booking> findByStatus(String status);

    /**
     * Finds all bookings for a specific passenger.
     *
     * @param passengerId passenger ID
     * @return list of bookings
     */
    List<Booking> findByPassengerId(Long passengerId);

    /**
     * Finds bookings filtered by flight number, gate ID, and status.
     *
     * @param flightNumber flight number
     * @param gateId       gate ID
     * @param status       booking status
     * @return list of bookings
     */
    List<Booking> findByFlightNumberAndGateIdAndStatus(String flightNumber, Long gateId, String status);

    /**
     * Finds bookings for a specific flight at a specific gate.
     *
     * @param flightNumber flight number
     * @param gateId       gate ID
     * @return list of bookings
     */
    List<Booking> findByFlightNumberAndGateId(String flightNumber, Long gateId);

    /**
     * Finds bookings for a specific gate filtered by status.
     *
     * @param gateId gate ID
     * @param status booking status
     * @return list of bookings
     */
    List<Booking> findByGateIdAndStatus(Long gateId, String status);

    /**
     * Finds all bookings assigned to a specific gate.
     *
     * @param gateId gate ID
     * @return list of bookings
     */
    List<Booking> findByGateId(Long gateId);

    /**
     * Finds bookings for a specific flight filtered by status.
     *
     * @param flightNumber flight number
     * @param status       booking status
     * @return list of bookings
     */
    List<Booking> findByFlightNumberAndStatus(String flightNumber, String status);
}
