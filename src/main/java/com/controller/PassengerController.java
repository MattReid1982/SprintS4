package com.controller;

import com.model.Airport;
import com.model.Passenger;
import com.model.Plane;
import com.service.PassengerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

/**
 * REST controller for managing passenger endpoints.
 */
@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    /** Service layer dependency for passenger operations. */
    private final PassengerService passengerService;

    /**
     * Constructs a PassengerController with required service dependency.
     *
     * @param passengerService passenger service
     */
    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * GET /api/passengers : Retrieves a paginated list of passengers.
     *
     * @param pageable pagination parameters (defaults to page 0, size 20)
     * @return a {@link Page} of passengers
     */
    @GetMapping
    public Page<Passenger> getAllPassengers(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return passengerService.getAllPassengers(pageable);
    }

    /**
     * GET /api/passengers/{id} : Retrieves a passenger by ID.
     *
     * @param id passenger ID
     * @return 200 OK with passenger if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        return passengerService.getPassengerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/passengers : Creates a new passenger.
     *
     * @param passenger passenger payload
     * @return newly created passenger
     */
    @PostMapping
    public Passenger createPassenger(@RequestBody Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    /**
     * PUT /api/passengers/{id} : Updates an existing passenger.
     *
     * @param id        passenger ID
     * @param passenger updated passenger payload
     * @return 200 OK with updated passenger, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
        try {
            return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/passengers/{id} : Deletes a passenger by ID.
     *
     * @param id passenger ID
     * @return 24 No Content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/passengers/{id}/planes : Retrieves all aircraft taken by a specific passenger.
     *
     * @param id passenger ID
     * @return list of planes taken by the passenger
     */
    @GetMapping("/{id}/planes")
    public List<Plane> getPlanesForPassenger(@PathVariable Long id) {
        return passengerService.getPlanesForPassenger(id);
    }

    /**
     * GET /api/passengers/{id}/airports : Retrieves all airports used by a passenger.
     *
     * @param id passenger ID
     * @return list of distinct airports used by the passenger
     */
    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsForPassenger(@PathVariable Long id) {
        return passengerService.getAirportsUsedByPassenger(id);
    }
}
