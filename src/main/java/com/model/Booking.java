package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Represents a Booking entity mapping to the bookings table.
 * Links a passenger to a flight with gate, seat, and status information.
 */
@Entity
@Table(name = "bookings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String bookingReference;

    @Column(nullable = false)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    @JsonIgnoreProperties({"airports", "passengers", "airline"})
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    @JsonIgnoreProperties({"planes"})
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "origin_airport_id", nullable = false)
    @JsonIgnoreProperties({"gates", "city"})
    private Airport originAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id", nullable = false)
    @JsonIgnoreProperties({"gates", "city"})
    private Airport destinationAirport;

    @ManyToOne
    @JoinColumn(name = "gate_id")
    @JsonIgnoreProperties({"airport"})
    private Gate gate;

    private String departureTime;

    private String arrivalTime;

    @Column(length = 20)
    private String seatNumber;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int baggageCount;

    @Column(length = 50)
    private String status;

    private String checkInTime;

    public Booking() {
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Airport getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(Airport originAirport) {
        this.originAirport = originAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getBaggageCount() {
        return baggageCount;
    }

    public void setBaggageCount(int baggageCount) {
        this.baggageCount = baggageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }
}
