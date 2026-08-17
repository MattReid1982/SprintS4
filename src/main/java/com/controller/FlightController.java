package com.controller;

import com.model.Flight;
import com.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link Flight} endpoints.
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
     * GET /api/flights — retrieves all scheduled flights.
     *
     * @return list of flights
     */
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    /**
     * GET /api/flights/{id} — retrieves a specific flight by ID.
     *
     * @param id flight ID
     * @return matching flight entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.findFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/flights — creates and persists a new flight.
     *
     * @param flight flight payload
     * @return created flight entity
     */
    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight saved = flightService.createFlight(flight);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * PUT /api/flights/{id} — updates an existing flight.
     *
     * @param id     flight ID
     * @param flight updated flight payload
     * @return updated flight entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        try {
            return ResponseEntity.ok(flightService.updateFlight(id, flight));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/flights/{id} — deletes a flight by ID.
     *
     * @param id flight ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
