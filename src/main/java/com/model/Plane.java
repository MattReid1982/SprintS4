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

    /** Tail / registration number (e.g., "C-FJZU"). */
    private String tailNumber;

    /** Aircraft type/model (e.g., "Boeing 737 MAX 8"). */
    private String type;

    /** Manufacturer (e.g., "Boeing", "Airbus"). */
    private String manufacturer;

    /** Airline company name operating the plane. */
    private String airlineName;

    /** Maximum passenger capacity. */
    private int numOfPassengers;

    /** Operational status (e.g., "ACTIVE", "MAINTENANCE"). */
    @Column(length = 50)
    private String status = "ACTIVE";

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
     * Constructs a Plane with airline name, type, and capacity.
     *
     * @param airlineName     airline name
     * @param type            aircraft type
     * @param numOfPassengers passenger capacity
     */
    public Plane(String airlineName, String type, int numOfPassengers) {
        this.airlineName = airlineName;
        this.type = type;
        this.numOfPassengers = numOfPassengers;
    }

    /**
     * Gets the plane ID.
     *
     * @return plane ID
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public long getID() {
        return ID;
    }

    /**
     * Sets the plane ID.
     *
     * @param ID plane ID
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Alias method for getID matching Java camelCase naming conventions.
     *
     * @return plane ID
     */
    public Long getId() {
        return ID;
    }

    /**
     * Alias method for setID matching Java camelCase naming conventions.
     *
     * @param id plane ID
     */
    public void setId(Long id) {
        this.ID = (id != null) ? id : 0L;
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

    public String getTailNumber() {
        return tailNumber != null ? tailNumber : ("C-F" + (100 + (ID > 0 ? (int)(ID % 800) : 100)));
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getManufacturer() {
        if (manufacturer != null) return manufacturer;
        if (type != null && type.contains("Airbus")) return "Airbus";
        if (type != null && type.contains("Boeing")) return "Boeing";
        if (type != null && type.contains("Embraer")) return "Embraer";
        if (type != null && type.contains("Dash")) return "De Havilland";
        return "Boeing";
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return status != null ? status : "ACTIVE";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModel() {
        return getType();
    }

    public int getCapacity() {
        return getNumOfPassengers();
    }
}
