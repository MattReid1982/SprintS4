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
}
