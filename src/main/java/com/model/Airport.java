package com.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

// Represents an airport entity in the database.
//Each airport belongs to one city.
@Entity
public class Airport {

//    Primary key for the Airport table.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Name of the airport.
    private String name;

//    IATA airport code (e.g., "JFK" for John F. Kennedy International Airport).
    private String airportCode;

//    Defines the many-to-one relationship between Airport and City.
//    Multiple airports can belong to a single city. The foreign key column in the Airport table is "city_id".
    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;

//    Default constructor for the Airport class.
    public Airport() {
    }

//    Constructor for the Airport class that initializes the name and airportCode fields.
    public Airport(String name, String airportCode) {
        this.name = name;
        this.airportCode = airportCode;
    }

//    Returns the ID of the airport.
    public Long getId() {
        return id;
    }

//    Returns the city associated with the airport.
    public String getName() {
        return name;
    }

//    Returns the name of the airport.
    public String getAirportCode() {
        return airportCode;
    }

    public City getCity() {
        return city;
    }

//    Returns the city associated with the airport.
    public void setId(Long id) {
        this.id = id;
    }

//    Sets the code of the airport.
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public void setName(String name) {
        this.name = name;
    }

//    Sets the name of the airport.
    public void setCity(City city) {
        this.city = city;
    }
}
