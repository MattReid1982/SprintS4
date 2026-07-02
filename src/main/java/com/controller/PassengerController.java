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

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    public Page<Passenger> getAllPassengers(
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        return passengerService.getAllPassengers(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        return passengerService.getPassengerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Passenger createPassenger(@RequestBody Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
        try {
            return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/planes")
    public List<Plane> getPlanesForPassenger(@PathVariable Long id) {
        return passengerService.getPlanesForPassenger(id);
    }

    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsForPassenger(@PathVariable Long id) {
        return passengerService.getAirportsUsedByPassenger(id);
    }
}
