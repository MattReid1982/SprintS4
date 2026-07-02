package com.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Plane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String type;
    private String airlineName;
    private int numOfPassengers;

    @ManyToMany
    @JoinTable(
        name = "plane_airport",
        joinColumns = @JoinColumn(name = "plane_id"),
        inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    private List<Airport> airports;

    @ManyToMany
    @JoinTable(
        name = "plane_passenger",
        joinColumns = @JoinColumn(name = "plane_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private List<Passenger> passengers;


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
