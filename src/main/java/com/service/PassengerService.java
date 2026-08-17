package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.Passenger;
import com.model.Plane;
import com.repo.PassengerRepository;
import com.repo.PlaneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing {@link Passenger} entities.
 * Provides business logic for managing passengers and querying related planes and airports.
 */
@Service
@Transactional(readOnly = true)
public class PassengerService {

    /** Repository for passenger database operations. */
    private final PassengerRepository passengerRepository;

    /** Repository for plane database operations. */
    private final PlaneRepository planeRepository;

    /**
     * Constructs a PassengerService with required repository dependencies.
     *
     * @param passengerRepository repository for passenger persistence
     * @param planeRepository     repository for plane queries
     */
    @Autowired
    public PassengerService(PassengerRepository passengerRepository, PlaneRepository planeRepository) {
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
    }

    /**
     * Retrieves all passengers stored in the database.
     *
     * @return list of all {@link Passenger} entities
     */
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    /**
     * Retrieves a paginated list of passengers from the database.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} containing the requested passengers
     */

    public Page<Passenger> getAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable);
    }

    /**
     * Retrieves a passenger by ID wrapped in an {@link Optional}.
     *
     * @param id passenger ID
     * @return optional containing matching passenger if found
     */

    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    /**
     * Creates and saves a new passenger entity in the database.
     *
     * @param passenger passenger to create
     * @return newly saved {@link Passenger}
     */
    @Transactional(readOnly = false)
    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    /**
     * Updates an existing passenger's details.
     *
     * @param id               ID of passenger to update
     * @param updatedPassenger passenger object with updated fields
     * @return updated {@link Passenger}
     * @throws ResourceNotFoundException if passenger is not found
     */
    @Transactional(readOnly = false)
    public Passenger updatePassenger(Long id, Passenger updatedPassenger) {
        return passengerRepository.findById(id)
                .map(passenger -> {
                    if (updatedPassenger.getFirstName() != null) {
                        passenger.setFirstName(updatedPassenger.getFirstName());
                    }
                    if (updatedPassenger.getLastName() != null) {
                        passenger.setLastName(updatedPassenger.getLastName());
                    }
                    if (updatedPassenger.getPhoneNumber() != null) {
                        passenger.setPhoneNumber(updatedPassenger.getPhoneNumber());
                    }
                    return passengerRepository.save(passenger);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
    }

    /**
     * Deletes a passenger from the database by ID.
     *
     * @param id passenger ID to delete
     * @throws ResourceNotFoundException if passenger is not found
     */
    @Transactional(readOnly = false)
    public void deletePassenger(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
    }

    /**
     * Retrieves all planes taken by a specific passenger.
     *
     * @param passengerId passenger ID
     * @return list of {@link Plane} entities
     */

    public List<Plane> getPlanesForPassenger(Long passengerId) {
        return planeRepository.findByPassengersId(passengerId);
    }

    /**
     * Retrieves all distinct airports used by a specific passenger through their flights.
     *
     * @param passengerId passenger ID
     * @return list of distinct {@link Airport} entities used by the passenger
     */
    public List<Airport> getAirportsUsedByPassenger(Long passengerId) {
        return planeRepository.findByPassengersId(passengerId).stream()
                .flatMap(plane -> plane.getAirports() != null ? plane.getAirports().stream() : Stream.empty())
                .distinct()
                .collect(Collectors.toList());
    }
}
