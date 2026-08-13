package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airline;
import com.repo.AirlineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Airline business logic.
 */
@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    public Optional<Airline> getAirlineById(Long id) {
        return airlineRepository.findById(id);
    }

    public Optional<Airline> getAirlineByCode(String code) {
        return airlineRepository.findByCode(code);
    }

    public Airline saveAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    public Airline updateAirline(Long id, Airline updated) {
        return airlineRepository.findById(id).map(existing -> {
            if (updated.getName() != null) {
                existing.setName(updated.getName());
            }
            if (updated.getCode() != null) {
                existing.setCode(updated.getCode());
            }
            return airlineRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
    }

    public void deleteAirline(Long id) {
        if (!airlineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airline not found with id: " + id);
        }
        airlineRepository.deleteById(id);
    }
}
