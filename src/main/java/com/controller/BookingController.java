package com.controller;

import com.model.Booking;
import com.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing booking endpoints.
 * Supports CRUD operations, lookup by reference/flight, and check-in.
 */
@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * GET /api/bookings — returns all bookings, with optional filter by status, flight number, or gate ID.
     */
    @GetMapping
    public List<Booking> getAllBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) Long gateId) {
        return bookingService.getBookings(status, flightNumber, gateId);
    }

    /**
     * GET /api/bookings/manifest — returns checked-in passengers for a flight at a specific gate.
     */
    @GetMapping("/manifest")
    public List<Booking> getFlightGateManifest(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) Long gateId,
            @RequestParam(defaultValue = "CHECKED_IN") String status) {
        return bookingService.getBookings(status, flightNumber, gateId);
    }

    /**
     * GET /api/bookings/{id} — single booking by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/bookings/reference/{ref} — lookup by booking reference.
     */
    @GetMapping("/reference/{ref}")
    public ResponseEntity<Booking> getBookingByReference(@PathVariable String ref) {
        return bookingService.getBookingByReference(ref)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/bookings/passenger/{passengerId} — all bookings for a passenger.
     */
    @GetMapping("/passenger/{passengerId}")
    public List<Booking> getBookingsByPassenger(@PathVariable Long passengerId) {
        return bookingService.getBookingsByPassenger(passengerId);
    }

    /**
     * POST /api/bookings — create a new booking.
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking saved = bookingService.createBooking(booking);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * PUT /api/bookings/{id} — update an existing booking.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            return ResponseEntity.ok(bookingService.updateBooking(id, booking));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/bookings/{id}/checkin — perform check-in for a booking.
     */
    @PutMapping("/{id}/checkin")
    public ResponseEntity<Booking> checkIn(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.checkIn(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/bookings/{id} — delete a booking.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
