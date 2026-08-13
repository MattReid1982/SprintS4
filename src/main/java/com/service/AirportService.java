package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.repo.AirportRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    /**
     * Retrieves all airports.
     */
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    /**
     * Retrieves one airport by ID.
     */
    public Airport getAirport(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airport not found with id: " + id
                        )
                );
    }

    /**
     * Creates a new airport.
     */
    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    /**
     * Updates an existing airport.
     */
    public Airport updateAirport(Long id, Airport airport) {

        // Make sure the airport actually exists
        Airport existingAirport = airportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airport not found with id: " + id
                        )
                );

        if (airport.getName() != null) {
            existingAirport.setName(airport.getName());
        }
        if (airport.getAirportCode() != null) {
            existingAirport.setAirportCode(airport.getAirportCode());
        }

        if (airport.getCity() != null) {
            existingAirport.setCity(airport.getCity());
        }

        return airportRepository.save(existingAirport);
    }

    /**
     * Deletes an airport.
     */
    public void deleteAirport(Long id) {

        if (!airportRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Airport not found with id: " + id
            );
        }

        airportRepository.deleteById(id);
    }

    /**
     * Retrieves airports belonging to a city.
     */
    public List<Airport> getAirportByCity(Long cityId) {
        return airportRepository.findByCityId(cityId);
    }
}