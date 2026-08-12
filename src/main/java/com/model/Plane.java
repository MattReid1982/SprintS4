package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a Plane entity in the system, associated with airports and passengers.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plane {

    /** Primary key for the Plane table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    /** Aircraft type (e.g., "Boeing 737"). */
    private String type;

    /** Airline company name operating the plane. */
    private String airlineName;

    /** Maximum passenger capacity. */
    private int numOfPassengers;

    /** The airline that operates this plane. */
    @ManyToOne
    @JoinColumn(name = "airline_id")
    @JsonIgnoreProperties({"planes"})
    private Airline airline;

    /** Many-to-many relationship with airports visited or served. */
    @ManyToMany
    @JoinTable(
        name = "plane_airport",
        joinColumns = @JoinColumn(name = "plane_id"),
        inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    private List<Airport> airports;

    /** Many-to-many relationship with passengers booked on the plane. */
    @ManyToMany
    @JoinTable(
        name = "plane_passenger",
        joinColumns = @JoinColumn(name = "plane_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private List<Passenger> passengers;

    /**
     * Default constructor for JPA.
     */
    public Plane() {
    }

    /**
     * Gets the plane ID.
     *
     * @return plane ID
     */
    public long getID() {
        return ID;
    }

    /**
     * Sets the plane ID.
     *
     * @param ID plane ID
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Gets the aircraft type.
     *
     * @return plane type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the aircraft type.
     *
     * @param type plane type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the airline name.
     *
     * @return airline name
     */
    public String getAirlineName() {
        return airlineName;
    }

    /**
     * Sets the airline name.
     *
     * @param airlineName airline name
     */
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    /**
     * Gets the maximum number of passengers.
     *
     * @return passenger capacity
     */
    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    /**
     * Sets the maximum number of passengers.
     *
     * @param numOfPassengers passenger capacity
     */
    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    /**
     * Gets the list of associated airports.
     *
     * @return list of airports
     */
    public List<Airport> getAirports() {
        return airports;
    }

    /**
     * Sets the list of associated airports.
     *
     * @param airports list of airports
     */
    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    /**
     * Gets the list of passengers on the plane.
     *
     * @return list of passengers
     */
    public List<Passenger> getPassengers() {
        return passengers;
    }

    /**
     * Sets the list of passengers on the plane.
     *
     * @param passengers list of passengers
     */
    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    /**
     * Gets the airline operating this plane.
     *
     * @return airline
     */
    public Airline getAirline() {
        return airline;
    }

    /**
     * Sets the airline operating this plane.
     *
     * @param airline airline
     */
    public void setAirline(Airline airline) {
        this.airline = airline;
    }
}
