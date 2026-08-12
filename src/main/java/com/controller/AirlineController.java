package com.controller;

import com.model.Airline;
import com.service.AirlineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing airline endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping
    public List<Airline> getAllAirlines() {
        return airlineService.getAllAirlines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirlineById(@PathVariable Long id) {
        return airlineService.getAirlineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Airline> getAirlineByCode(@PathVariable String code) {
        return airlineService.getAirlineByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Airline createAirline(@RequestBody Airline airline) {
        return airlineService.saveAirline(airline);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airline> updateAirline(@PathVariable Long id, @RequestBody Airline airline) {
        try {
            return ResponseEntity.ok(airlineService.updateAirline(id, airline));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
