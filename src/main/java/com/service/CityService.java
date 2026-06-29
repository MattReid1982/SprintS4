package com.service;

import com.model.City;
import com.repo.CityRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

// Service class for managing City entities.
// Provides methods for CRUD operations and pagination.
@Service
public class CityService {

//    Repository used to perform City CRUD operations.
    private final CityRepository cityRepository;

//    Constructor for CityService that initializes the cityRepository field.
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

//    Returns a paginated list of cities from the database.
//    @param pageable object containing pagination information.
//    @return A Page object containing the requested cities.
    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

//    Returns a single city using its ID.
//    @param id The ID of the city to retrieve.
//    @return The City object with the specified ID.
    public City getCity(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
    }
//   Creates a new city in the database.
//    @param city The City object to create.
//    @return The newly created City object.
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

//    Updates an existing city in the database using its ID.
//    @param id The ID of the city to update.
//    @param city The City object containing the updated information.
//    @return The updated City object.
    public City updateCity(Long id, City city) {
        city.setId(id);
        return cityRepository.save(city);
    }

//    Deletes a city from the database using its ID.
//    @param id The ID of the city to delete.
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
