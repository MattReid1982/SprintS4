package com.model;

/**
 * Represents the current status of a flight.
 */
public enum FlightStatus {

    /**
     * Flight is on schedule and expected to depart and arrive on time.
     */
    ON_TIME,

    /**
     * Flight has been delayed.
     */
    DELAYED,

    /**
     * Flight has been cancelled.
     */
    CANCELLED,

    /**
     * Flight has landed.
     */
    LANDED
}