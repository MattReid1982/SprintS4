package com.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Represents an airport entity in the database.
 * Each airport belongs to one city.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {

    /** Primary key for the Airport table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the airport. */
    private String name;

    /** IATA airport code (e.g., "YYZ"). */
    private String airportCode;

    /**
     * Defines the many-to-one relationship between Airport and City.
     * Multiple airports can belong to a single city.
     */
    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;

    /**
     * Default constructor for JPA.
     */
    public Airport() {
    }

    /**
     * Constructs an Airport with specified name and code.
     *
     * @param name        the airport name
     * @param airportCode the IATA airport code
     */
    public Airport(String name, String airportCode) {
        this.name = name;
        this.airportCode = airportCode;
    }

    /**
     * Gets the ID of the airport.
     *
     * @return airport ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the airport.
     *
     * @param id airport ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the airport.
     *
     * @return airport name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the airport.
     *
     * @param name airport name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the IATA code of the airport.
     *
     * @return airport code
     */
    public String getAirportCode() {
        return airportCode;
    }

    /**
     * Sets the IATA code of the airport.
     *
     * @param airportCode airport code
     */
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    /**
     * Gets the city associated with the airport.
     *
     * @return associated City
     */
    public City getCity() {
        return city;
    }

    /**
     * Sets the city associated with the airport.
     *
     * @param city associated City
     */
    public void setCity(City city) {
        this.city = city;
    }
}
