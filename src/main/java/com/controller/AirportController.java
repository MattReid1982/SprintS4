package com.example.airportapi.controller;

import com.model.Airport;
import com.service.AirportService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

//    Service used to perform Airport CRUD operations.
    private final AirportService airportService;

//    Constructor for AirportController that initializes the airportService field.
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

//    Returns a list of all airports in the database.
//    @return A list of Airport objects.
    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

//    Returns a single airport using its ID.
//    @param id The ID of the airport to retrieve.
//    @return The Airport object with the specified ID.
    @GetMapping("/{id}")
    public Airport getAirport(@PathVariable Long id) {
        return airportService.getAirport(id);
    }

//    Creates a new airport in the database.
//    @param airport The Airport object to create.
//    @return The newly created Airport object.
    @PostMapping
    public Airport createAirport(
            @RequestBody Airport airport) {
        return airportService.saveAirport(airport);
    }

//    Updates an existing airport in the database using its ID.
//    @param id The ID of the airport to update.
//    @param airport The Airport object containing the updated information.
//    @return The updated Airport object.
    @PutMapping("/{id}")
    public Airport updateAirport(
            @PathVariable Long id,
            @RequestBody Airport airport) {
        return airportService.updateAirport(id, airport);
    }


//    Deletes an airport from the database using its ID.
//    @param id The ID of the airport to delete.
    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
    }
}
