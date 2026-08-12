package com.service;

import com.model.Booking;
import com.repo.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Booking business logic including check-in workflow.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    public List<Booking> getBookingsByFlightNumber(String flightNumber) {
        return bookingRepository.findByFlightNumber(flightNumber);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updated) {
        return bookingRepository.findById(id).map(existing -> {
            existing.setFlightNumber(updated.getFlightNumber());
            existing.setPassenger(updated.getPassenger());
            existing.setPlane(updated.getPlane());
            existing.setAirline(updated.getAirline());
            existing.setOriginAirport(updated.getOriginAirport());
            existing.setDestinationAirport(updated.getDestinationAirport());
            existing.setGate(updated.getGate());
            existing.setDepartureTime(updated.getDepartureTime());
            existing.setArrivalTime(updated.getArrivalTime());
            existing.setSeatNumber(updated.getSeatNumber());
            existing.setBaggageCount(updated.getBaggageCount());
            existing.setStatus(updated.getStatus());
            existing.setCheckInTime(updated.getCheckInTime());
            return bookingRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    /**
     * Performs check-in for a booking: sets status and records check-in timestamp.
     *
     * @param id booking ID
     * @return updated booking
     */
    public Booking checkIn(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus("CHECKED_IN");
            booking.setCheckInTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
