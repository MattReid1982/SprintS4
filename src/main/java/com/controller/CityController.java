package com.controller;

import com.model.Airport;
import com.model.City;
import com.service.AirportService;
import com.service.CityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing city endpoints.
 */
@RestController
@RequestMapping("/cities")
public class CityController {

    /** Service layer dependency for city business logic. */
    private final CityService cityService;

    /** Service layer dependency for airport queries related to cities. */
    private final AirportService airportService;

    /**
     * Constructs a CityController with required dependencies.
     *
     * @param cityService    city service
     * @param airportService airport service
     */
    public CityController(CityService cityService, AirportService airportService) {
        this.cityService = cityService;
        this.airportService = airportService;
    }

    /**
     * GET /cities : Retrieves a paginated list of cities.
     *
     * @param pageable pagination parameters (defaults to page 0, size 20)
     * @return a {@link Page} containing the requested cities
     */
    @GetMapping
    public Page<City> getCities(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return cityService.getAllCities(pageable);
    }

    /**
     * GET /cities/{id} : Returns a single city by ID.
     *
     * @param id city ID
     * @return matching city
     */
    @GetMapping("/{id}")
    public City getCity(@PathVariable Long id) {
        return cityService.getCity(id);
    }

    /**
     * POST /cities : Creates a new city.
     *
     * @param city city payload
     * @return newly created city
     */
    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    /**
     * PUT /cities/{id} : Updates an existing city.
     *
     * @param id   city ID
     * @param city updated city payload
     * @return updated city
     */
    @PutMapping("/{id}")
    public City updateCity(@PathVariable Long id, @RequestBody City city) {
        return cityService.updateCity(id, city);
    }

    /**
     * DELETE /cities/{id} : Deletes a city by ID.
     *
     * @param id city ID
     */
    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }

    /**
     * GET /cities/{id}/airports : Retrieves all airports located in a specific city.
     * Answers the query: "What airports are in each city?"
     *
     * @param id city ID
     * @return list of airports in the city
     */
    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsInCity(@PathVariable Long id) {
        return airportService.getAirportByCity(id);
    }
}