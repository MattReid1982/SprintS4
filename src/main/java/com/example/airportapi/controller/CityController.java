package com.example.airportapi.controller;

import com.example.airportapi.model.Airport;
import com.example.airportapi.model.City;
import com.example.airportapi.service.AirportService;
import com.example.airportapi.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;
    private final AirportService airportService;

    public CityController(CityService cityService, AirportService airportService) {
        this.cityService = cityService;
        this.airportService = airportService;
    }

    @GetMapping
    public Page<City> getCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return cityService.getAllCities(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public City getCity(@PathVariable Long id) {
        return cityService.getCity(id);
    }

    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    @PutMapping("/{id}")
    public City updateCity(
            @PathVariable Long id,
            @RequestBody City city) {
        return cityService.updateCity(id, city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }

    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsInCity(
            @PathVariable Long id) {
        return airportService.getAirportByCity(id);
    }
}