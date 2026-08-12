package com.controller;

import com.model.Airport;
import com.service.AirportService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing airport endpoints.
 */
@RestController
@RequestMapping("/api/airports")
public class AirportController {

    /** Service layer dependency for airport business logic. */
    private final AirportService airportService;

    /**
     * Constructs an AirportController with the required service dependency.
     *
     * @param airportService airport service
     */
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    /**
     * GET /airports : Returns a list of all airports.
     *
     * @return list of all airports
     */
    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    /**
     * GET /airports/{id} : Returns a single airport by ID.
     *
     * @param id airport ID
     * @return matching airport
     */
    @GetMapping("/{id}")
    public Airport getAirport(@PathVariable Long id) {
        return airportService.getAirport(id);
    }

    /**
     * POST /airports : Creates a new airport.
     *
     * @param airport airport payload
     * @return newly created airport
     */
    @PostMapping
    public Airport createAirport(@RequestBody Airport airport) {
        return airportService.saveAirport(airport);
    }

    /**
     * PUT /airports/{id} : Updates an existing airport.
     *
     * @param id      airport ID
     * @param airport updated airport payload
     * @return updated airport
     */
    @PutMapping("/{id}")
    public Airport updateAirport(@PathVariable Long id, @RequestBody Airport airport) {
        return airportService.updateAirport(id, airport);
    }

    /**
     * DELETE /airports/{id} : Deletes an airport by ID.
     *
     * @param id airport ID
     */
    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
    }
}
