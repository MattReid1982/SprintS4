package com.service;

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
    @SuppressWarnings("null")
    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    /**
     * Retrieves a single city by its unique ID.
     *
     * @param id city ID
     * @return the matching {@link City}
     * @throws RuntimeException if no city is found with the specified ID
     */
    @SuppressWarnings("null")
    public City getCity(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
    }

    /**
     * Creates and saves a new city entity in the database.
     *
     * @param city the city to create
     * @return the newly saved {@link City}
     */
    @SuppressWarnings("null")
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    /**
     * Updates an existing city entity using its ID.
     *
     * @param id   city ID to update
     * @param city city object containing updated details
     * @return the updated {@link City}
     */
    @SuppressWarnings("null")
    public City updateCity(Long id, City city) {
        city.setId(id);
        return cityRepository.save(city);
    }

    /**
     * Deletes a city from the database by its ID.
     *
     * @param id city ID to delete
     */
    @SuppressWarnings("null")
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
