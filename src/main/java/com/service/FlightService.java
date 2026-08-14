package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Flight;
import com.repo.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Flight business logic.
 *
 * Provides CRUD operations for managing scheduled flights
 * and querying flight information.
 */
@Service
public class FlightService {

    private final FlightRepository flightRepository;

    /**
     * Constructs a FlightService with the required repository.
     *
     * @param flightRepository repository for Flight persistence
     */
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Retrieves all flights.
     *
     * @return list of all flights
     */
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    /**
     * Retrieves a flight by ID.
     *
     * @param id flight ID
     * @return the matching Flight entity
     * @throws ResourceNotFoundException if no flight is found with the given ID
     */
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Flight not found with id: " + id
                        )
                );
    }

    /**
     * Retrieves a flight by ID wrapped in an Optional.
     *
     * @param id flight ID
     * @return Optional containing the matching Flight if found
     */
    public Optional<Flight> findFlightById(Long id) {
        return flightRepository.findById(id);
    }

    /**
     * Creates and persists a new flight.
     *
     * @param flight flight entity payload
     * @return saved Flight entity
     */
    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    /**
     * Updates an existing flight by ID.
     *
     * @param id      flight ID
     * @param updated updated flight payload
     * @return updated Flight entity
     * @throws ResourceNotFoundException if no flight is found with the given ID
     */
    public Flight updateFlight(Long id, Flight updated) {
        return flightRepository.findById(id)
                .map(existing -> {
                    if (updated.getFlightNumber() != null) {
                        existing.setFlightNumber(updated.getFlightNumber());
                    }
                    if (updated.getDepartureTime() != null) {
                        existing.setDepartureTime(updated.getDepartureTime());
                    }
                    if (updated.getArrivalTime() != null) {
                        existing.setArrivalTime(updated.getArrivalTime());
                    }
                    if (updated.getStatus() != null) {
                        existing.setStatus(updated.getStatus());
                    }
                    if (updated.getDepartureAirport() != null) {
                        existing.setDepartureAirport(updated.getDepartureAirport());
                    }
                    if (updated.getArrivalAirport() != null) {
                        existing.setArrivalAirport(updated.getArrivalAirport());
                    }
                    if (updated.getPlane() != null) {
                        existing.setPlane(updated.getPlane());
                    }
                    return flightRepository.save(existing);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Flight not found with id: " + id
                        )
                );
    }

    /**
     * Deletes a flight by ID.
     *
     * @param id flight ID
     * @throws ResourceNotFoundException if no flight is found with the given ID
     */
    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Flight not found with id: " + id
            );
        }
        flightRepository.deleteById(id);
    }
}