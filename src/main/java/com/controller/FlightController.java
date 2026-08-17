package com.controller;

import com.model.Flight;
import com.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing flight endpoints.
 * Provides CRUD operations for scheduled flights with pagination support.
 */
@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * GET /api/flights : Retrieves a paginated list of flights.
     *
     * @param pageable pagination parameters (defaults to page 0, size 10, sorted by id ascending)
     * @return a {@link Page} of flights
     */
    @GetMapping
    public Page<Flight> getAllFlights(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return flightService.getAllFlights(pageable);
    }

    /** Returns a single flight by ID, or 404 if not found. */
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.findFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Creates a new flight and returns it. */
    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }

    /** Updates an existing flight by ID. Returns 404 if not found. */
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(
            @PathVariable Long id, @RequestBody Flight flight) {
        try {
            return ResponseEntity.ok(flightService.updateFlight(id, flight));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Deletes a flight by ID. Returns 404 if not found. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
