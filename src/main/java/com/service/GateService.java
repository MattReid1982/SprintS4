package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Gate;
import com.repo.AirportRepository;
import com.repo.GateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Gate business logic.
 */
@Service
@Transactional
public class GateService {

    private final GateRepository gateRepository;
    private final AirportRepository airportRepository;

    /**
     * Constructs a GateService with required dependencies.
     *
     * @param gateRepository    gate repository
     * @param airportRepository airport repository
     */
    public GateService(GateRepository gateRepository, AirportRepository airportRepository) {
        this.gateRepository = gateRepository;
        this.airportRepository = airportRepository;
    }

    /**
     * Retrieves all gates.
     *
     * @return list of all gates
     */
    @Transactional(readOnly = true)
    public List<Gate> getAllGates() {
        return gateRepository.findAll();
    }

    /**
     * Retrieves a gate by its ID.
     *
     * @param id gate ID
     * @return matching Gate entity
     * @throws ResourceNotFoundException if gate with given ID is not found
     */
    public Gate getGate(Long id) {
        return gateRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gate not found with id: " + id
                        )
                );
    }

    /**
     * Retrieves all gates belonging to a specific airport.
     *
     * @param airportId airport ID
     * @return list of gates for the specified airport
     */
    public List<Gate> getGatesByAirport(Long airportId) {
        return gateRepository.findByAirportId(airportId);
    }

    /**
     * Creates and persists a new gate.
     *
     * @param gate gate entity payload
     * @return saved Gate entity
     */
    public Gate saveGate(Gate gate) {
        if (gate.getAirport() != null && gate.getAirport().getId() != null) {
            airportRepository.findById(gate.getAirport().getId()).ifPresent(gate::setAirport);
        }
        return gateRepository.save(gate);
    }

    /**
     * Updates an existing gate entity by ID.
     *
     * @param id   gate ID
     * @param gate updated gate payload
     * @return updated Gate entity
     * @throws ResourceNotFoundException if gate with given ID is not found
     */
    public Gate updateGate(Long id, Gate gate) {
        Gate existingGate = gateRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gate not found with id: " + id
                        )
                );

        if (gate.getGateNumber() != null) {
            existingGate.setGateNumber(gate.getGateNumber());
        }
        if (gate.getTerminal() != null) {
            existingGate.setTerminal(gate.getTerminal());
        }
        if (gate.getAirport() != null && gate.getAirport().getId() != null) {
            airportRepository.findById(gate.getAirport().getId()).ifPresent(existingGate::setAirport);
        }
        if (gate.getStatus() != null) {
            existingGate.setStatus(gate.getStatus());
        }
        if (gate.getCurrentFlight() != null) {
            existingGate.setCurrentFlight(gate.getCurrentFlight());
        }

        return gateRepository.save(existingGate);
    }

    /**
     * Deletes a gate by its ID.
     *
     * @param id gate ID
     * @throws ResourceNotFoundException if gate with given ID is not found
     */
    public void deleteGate(Long id) {
        if (!gateRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Gate not found with id: " + id
            );
        }
        gateRepository.deleteById(id);
    }
}
