package com.example.airportapi.controller;

import com.model.Airport;
import com.model.City;
import com.service.AirportService;
import com.service.CityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

//  Service used to perform City CRUD operations
    private final CityService cityService;

//  Service used to perform Airport CRUD operations
    private final AirportService airportService;

//  Constructor for CityController that initializes the cityService and airportService fields
    public CityController(CityService cityService, AirportService airportService) {
        this.cityService = cityService;
        this.airportService = airportService;
    }

    //  Endpoint to retrieve a paginated list of cities. Accepts optional query parameters for page number and page size.
//    @param page Page number (starts at 0)
//    @param size Number of cities per page
//    @return A Page object containing the requested cities

    @GetMapping
    public Page<City> getCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return cityService.getAllCities(PageRequest.of(page, size));
    }

    // Returns a paginated list of all cities
//    @param id City ID to get
//    @return The City object with the specified ID

    @GetMapping("/{id}")
    public City getCity(@PathVariable Long id) {
        return cityService.getCity(id);
    }

//  Creates a new city using the provided City object in the request body
//    @param city City information recieved from the request body
//    @return the newly created city

    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

//  Updates an existing city with the specified ID using the provided City object in the request body
//    @param id City ID to update
//    @param city Updated city information recieved from the request body
//    @return the updated city

    @PutMapping("/{id}")
    public City updateCity(
            @PathVariable Long id,
            @RequestBody City city) {
        return cityService.updateCity(id, city);
    }

//  Deletes the city with the specified ID
//    @param id City ID to delete
    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }

//  Retrieves a list of airports located in the city with the specified ID
//    @param id City ID to get airports for
//    @return A list of Airport objects located in the specified city
//    this endpoint answers the assignment question"
//    "What airports are in each city?"
//    @param id City ID to get airports for
//    @return A list of Airport objects located in the specified city

    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsInCity(
            @PathVariable Long id) {
        return airportService.getAirportByCity(id);
    }
}