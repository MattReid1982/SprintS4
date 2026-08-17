package com.service;

import com.exception.ResourceNotFoundException;
import com.model.City;
import com.repo.CityRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Service class for managing {@link City} entities.
 * Provides business logic for CRUD operations and paginated queries.
 */
@Service
public class CityService {

    /** Repository used to perform City database operations. */
    private final CityRepository cityRepository;

    /**
     * Constructs a CityService with the required CityRepository dependency.
     *
     * @param cityRepository repository for city persistence
     */
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Retrieves a paginated list of cities from the database.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} containing the requested cities
     */

    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    /**
     * Retrieves a single city by its unique ID.
     *
     * @param id city ID
     * @return the matching {@link City}
     * @throws ResourceNotFoundException if no city is found with the specified ID
     */

    public City getCity(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    /**
     * Creates and saves a new city entity in the database.
     *
     * @param city the city to create
     * @return the newly saved {@link City}
     */

    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    /**
     * Updates an existing city entity using its ID.
     *
     * @param id   city ID to update
     * @param city city object containing updated details
     * @return the updated {@link City}
     * @throws ResourceNotFoundException if no city is found with the specified ID
     */

    public City updateCity(Long id, City city) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        if (city.getName() != null) {
            existingCity.setName(city.getName());
        }
        if (city.getProvince() != null) {
            existingCity.setProvince(city.getProvince());
        }
        if (city.getPopulation() > 0) {
            existingCity.setPopulation(city.getPopulation());
        }

        return cityRepository.save(existingCity);
    }

    /**
     * Deletes a city from the database by its ID.
     *
     * @param id city ID to delete
     * @throws ResourceNotFoundException if no city is found with the specified ID
     */

    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }
}
