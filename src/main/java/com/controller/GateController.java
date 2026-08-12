package com.controller;

import com.model.Gate;
import com.service.GateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing gate endpoints.
 * Provides CRUD operations and airport-specific query capabilities.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/gates")
public class GateController {

    /** Service layer dependency for gate business logic. */
    private final GateService gateService;

    /**
     * Constructs a GateController with the required service dependency.
     *
     * @param gateService gate service dependency
     */
    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    /**
     * GET /api/gates : Returns a list of all gates, or filters gates by airport ID if query parameter is provided.
     *
     * @param airportId optional airport ID filter
     * @return list of matching gates
     */
    @GetMapping
    public List<Gate> getAllGates(@RequestParam(required = false) Long airportId) {
        if (airportId != null) {
            return gateService.getGatesByAirport(airportId);
        }
        return gateService.getAllGates();
    }

    /**
     * GET /api/gates/{id} : Returns a single gate by ID.
     *
     * @param id gate ID
     * @return matching gate
     */
    @GetMapping("/{id}")
    public Gate getGateById(@PathVariable Long id) {
        return gateService.getGate(id);
    }

    /**
     * GET /api/gates/airport/{airportId} : Returns all gates associated with a specific airport ID.
     *
     * @param airportId airport ID
     * @return list of gates for the specified airport
     */
    @GetMapping("/airport/{airportId}")
    public List<Gate> getGatesByAirport(@PathVariable Long airportId) {
        return gateService.getGatesByAirport(airportId);
    }

    /**
     * POST /api/gates : Creates a new gate.
     *
     * @param gate gate payload
     * @return ResponseEntity with created Gate and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Gate> createGate(@RequestBody Gate gate) {
        Gate savedGate = gateService.saveGate(gate);
        return new ResponseEntity<>(savedGate, HttpStatus.CREATED);
    }

    /**
     * PUT /api/gates/{id} : Updates an existing gate.
     *
     * @param id   gate ID
     * @param gate updated gate payload
     * @return updated gate
     */
    @PutMapping("/{id}")
    public Gate updateGate(@PathVariable Long id, @RequestBody Gate gate) {
        return gateService.updateGate(id, gate);
    }

    /**
     * DELETE /api/gates/{id} : Deletes a gate by ID.
     *
     * @param id gate ID
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGate(@PathVariable Long id) {
        gateService.deleteGate(id);
        return ResponseEntity.noContent().build();
    }
}
