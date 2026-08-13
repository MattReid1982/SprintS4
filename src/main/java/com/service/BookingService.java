package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Booking;
import com.repo.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    /**
     * Creates a new booking. Auto-generates a unique booking reference if omitted.
     * Also sets check-in timestamp if created in CHECKED_IN status.
     *
     * @param booking booking payload
     * @return saved Booking entity
     */
    public Booking createBooking(Booking booking) {
        if (booking.getBookingReference() == null || booking.getBookingReference().isBlank()) {
            String flightTag = booking.getFlightNumber() != null ? booking.getFlightNumber().replaceAll("\\s+", "") : "FL";
            String uniqueShort = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            booking.setBookingReference("BK-" + flightTag + "-" + uniqueShort);
        }
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("BOOKED");
        }
        if ("CHECKED_IN".equalsIgnoreCase(booking.getStatus()) && (booking.getCheckInTime() == null || booking.getCheckInTime().isBlank())) {
            booking.setCheckInTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        return bookingRepository.save(booking);
    }

    /**
     * Updates an existing booking's attributes including gate, seat, baggage, and status.
     *
     * @param id      booking ID
     * @param updated updated booking payload
     * @return updated Booking entity
     */
    public Booking updateBooking(Long id, Booking updated) {
        return bookingRepository.findById(id).map(existing -> {
            if (updated.getFlightNumber() != null) existing.setFlightNumber(updated.getFlightNumber());
            if (updated.getPassenger() != null) existing.setPassenger(updated.getPassenger());
            if (updated.getPlane() != null) existing.setPlane(updated.getPlane());
            if (updated.getAirline() != null) existing.setAirline(updated.getAirline());
            if (updated.getOriginAirport() != null) existing.setOriginAirport(updated.getOriginAirport());
            if (updated.getDestinationAirport() != null) existing.setDestinationAirport(updated.getDestinationAirport());
            if (updated.getGate() != null) existing.setGate(updated.getGate());
            if (updated.getDepartureTime() != null) existing.setDepartureTime(updated.getDepartureTime());
            if (updated.getArrivalTime() != null) existing.setArrivalTime(updated.getArrivalTime());
            if (updated.getSeatNumber() != null) existing.setSeatNumber(updated.getSeatNumber());
            existing.setBaggageCount(updated.getBaggageCount());
            if (updated.getStatus() != null) {
                existing.setStatus(updated.getStatus());
                if ("CHECKED_IN".equalsIgnoreCase(updated.getStatus()) && (existing.getCheckInTime() == null || existing.getCheckInTime().isBlank())) {
                    existing.setCheckInTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
            if (updated.getCheckInTime() != null) existing.setCheckInTime(updated.getCheckInTime());
            return bookingRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    /**
     * Performs check-in for a booking: sets status to CHECKED_IN and records timestamp.
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
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
}
