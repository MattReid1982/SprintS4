package com.example.airportapi.controller;

import com.example.airportapi.model.Airport;
import com.example.airportapi.service.AirportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    @GetMapping("/{id}")
    public Airport getAirport(@PathVariable Long id) {
        return airportService.getAirport(id);
    }

    @PostMapping
    public Airport createAirport(
            @RequestBody Airport airport) {
        return airportService.saveAirport(airport);
    }

    @PutMapping("/{id}")
    public Airport updateAirport(
            @PathVariable Long id,
            @RequestBody Airport airport) {
        return airportService.updateAirport(id, airport);
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
    }
}
