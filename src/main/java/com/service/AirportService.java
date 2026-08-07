package com.service;

import com.model.Airport;
import com.repo.AirportRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link Airport} entities.
 * Provides business logic for CRUD operations and city-based queries.
 */
@Service
public class AirportService {

    /** Repository used to perform Airport database operations. */
    private final AirportRepository airportRepository;

    /**
     * Constructs an AirportService with the required AirportRepository dependency.
     *
     * @param airportRepository repository for airport persistence
     */
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /**
     * Retrieves all airports stored in the database.
     *
     * @return list of all {@link Airport} entities
     */
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    /**
     * Retrieves a single airport by its unique ID.
     *
     * @param id airport ID
     * @return the matching {@link Airport}
     * @throws RuntimeException if no airport is found with the specified ID
     */
    @SuppressWarnings("null")
    public Airport getAirport(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    /**
     * Creates and saves a new airport entity in the database.
     *
     * @param airport the airport to create
     * @return the newly saved {@link Airport}
     */
    @SuppressWarnings("null")
    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    /**
     * Updates an existing airport entity using its ID.
     *
     * @param id      airport ID to update
     * @param airport airport object containing updated details
     * @return the updated {@link Airport}
     */
    @SuppressWarnings("null")
    public Airport updateAirport(Long id, Airport airport) {
        airport.setId(id);
        return airportRepository.save(airport);
    }

    /**
     * Deletes an airport from the database by its ID.
     *
     * @param id airport ID to delete
     */
    @SuppressWarnings("null")
    public void deleteAirport(Long id) {
        airportRepository.deleteById(id);
    }

    /**
     * Retrieves all airports associated with a specific city ID.
     *
     * @param cityId ID of the city
     * @return list of {@link Airport} entities in the specified city
     */
    public List<Airport> getAirportByCity(Long cityId) {
        return airportRepository.findByCityId(cityId);
    }
}
