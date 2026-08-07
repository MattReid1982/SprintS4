package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a City entity in the database.
 * A city can have multiple associated airports.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

    /** Primary key for the City table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the city. */
    private String name;

    /** Province or region where the city is located. */
    private String province;

    /** Population count of the city. */
    private int population;

    /**
     * One-to-many relationship mapping airports located within this city.
     */
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Airport> airports = new ArrayList<>();

    /**
     * Default constructor for JPA.
     */
    public City() {
    }

    /**
     * Constructs a City with name, province, and population.
     *
     * @param name       city name
     * @param province   province name
     * @param population population count
     */
    public City(String name, String province, int population) {
        this.name = name;
        this.province = province;
        this.population = population;
    }

    /**
     * Gets the city ID.
     *
     * @return city ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the city ID.
     *
     * @param id city ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the city name.
     *
     * @return city name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the city name.
     *
     * @param name city name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the province of the city.
     *
     * @return province name
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the province of the city.
     *
     * @param province province name
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Gets the population of the city.
     *
     * @return population count
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the population of the city.
     *
     * @param population population count
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Gets the list of airports associated with the city.
     *
     * @return list of airports
     */
    public List<Airport> getAirports() {
        return airports;
    }

    /**
     * Sets the list of airports associated with the city.
     *
     * @param airports list of airports
     */
    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
