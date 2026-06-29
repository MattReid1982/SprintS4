package com.example.airportapi.service;

import com.example.airportapi.model.Airport;
import com.example.airportapi.repo.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service class for managing Airport entities.
// Provides methods for CRUD operations and custom queries.
@Service
public class AirportService {

//  Repository used to perform Airport CRUD operations.
    private final AirportRepository airportRepository;

//    Constructor for AirportService that initializes the airportRepository field.
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

//    Returns a list of all airports in the database.
//    @return A list of Airport objects.
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

//    Returns a single airport using its ID.
//    @param id The ID of the airport to retrieve.
//    @return The Airport object with the specified ID.
    public Airport getAirport(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

//    Creates a new airport in the database.
//    @param airport The Airport object to create.
//    @return The newly created Airport object.
    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

//    Updates an existing airport in the database using its ID.
//    @param id The ID of the airport to update.
//    @param airport The Airport object containing the updated information.
//    @return The updated Airport object.
    public Airport updateAirport(Long id, Airport airport) {
        airport.setId(id);
        return airportRepository.save(airport);
    }

//    Deletes an airport from the database using its ID.
//    @param id The ID of the airport to delete.
    public void deleteAirport(Long id) {
        airportRepository.deleteById(id);
    }

//    Returns a list of airports associated with a specific city ID.
//    @param cityId The ID of the city to retrieve airports for.
//    @return A list of Airport objects associated with the specified city ID.
    public List<Airport> getAirportByCity(Long cityId) {
        return airportRepository.findByCityId(cityId);
    }
}
