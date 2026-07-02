package com.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Represents a City entity in the database, which can have multiple associated airports.
@Entity
public class City {

//    Primary key for the City table.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String province;

    private int population;

//    Defines a one-to-many relationship between City and Airport.
//    One city can have multiple airports.
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference

    private List<Airport> airports = new ArrayList<>();

    public City() {
    }

    public City(String name, String province, int population) {
        this.name = name;
        this.province = province;
        this.population = population;
    }

//    Returns the city ID
    public Long getId() {
        return id;
    }

//    Sets the city ID
    public void setId(Long id) {
        this.id = id;
    }

//    Returns the city name
    public String getName() {
        return name;
    }

//    Sets the city name
    public void setName(String name) {
        this.name = name;
    }

//    Returns the province for the city.
    public String getProvince() {
        return province;
    }

//    Returns the population of the city.
    public int getPopulation() {
        return population;
    }

//    Sets the population of the city.
    public void setPopulation(int population) {
        this.population = population;
    }

//    Sets the province for the city.
    public void setProvince(String province) {
        this.province = province;
    }

//    Returns the list of airports associated with the city.
    public List<Airport> getAirports() {
        return airports;
    }

//    Sets the list of airports associated with the city.
    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
