package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Represents a Gate entity at an airport.
 * Each gate belongs to one airport.
 */
@Entity
@Table(name = "gate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gate {

    /** Primary key for the Gate table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Gate number/designation (e.g., "A1", "B2"). */
    private String gateNumber;

    /** Terminal designation (e.g., "Terminal 1", "International"). */
    private String terminal;

    /**
     * Defines the many-to-one relationship between Gate and Airport.
     * Multiple gates can belong to a single airport.
     */
    @ManyToOne
    @JoinColumn(name = "airport_id")
    @JsonIgnoreProperties({"gates", "city"})
    private Airport airport;

    /**
     * Default constructor for JPA.
     */
    public Gate() {
    }

    /**
     * Constructs a Gate with specified gate number and terminal.
     *
     * @param gateNumber the gate number/designation
     * @param terminal   the terminal designation
     */
    public Gate(String gateNumber, String terminal) {
        this.gateNumber = gateNumber;
        this.terminal = terminal;
    }

    /**
     * Constructs a Gate with specified gate number, terminal, and airport.
     *
     * @param gateNumber the gate number/designation
     * @param terminal   the terminal designation
     * @param airport    the associated airport
     */
    public Gate(String gateNumber, String terminal, Airport airport) {
        this.gateNumber = gateNumber;
        this.terminal = terminal;
        this.airport = airport;
    }

    /**
     * Gets the ID of the gate.
     *
     * @return gate ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the gate.
     *
     * @param id gate ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the gate number.
     *
     * @return gate number
     */
    public String getGateNumber() {
        return gateNumber;
    }

    /**
     * Sets the gate number.
     *
     * @param gateNumber gate number
     */
    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    /**
     * Alias getter for gateCode to support schemas using gateCode.
     *
     * @return gate number/code
     */
    public String getGateCode() {
        return gateNumber;
    }

    /**
     * Alias setter for gateCode.
     *
     * @param gateCode gate number/code
     */
    public void setGateCode(String gateCode) {
        this.gateNumber = gateCode;
    }

    /**
     * Gets the terminal designation.
     *
     * @return terminal
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * Sets the terminal designation.
     *
     * @param terminal terminal
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * Gets the associated airport.
     *
     * @return associated Airport
     */
    public Airport getAirport() {
        return airport;
    }

    /**
     * Sets the associated airport.
     *
     * @param airport associated Airport
     */
    public void setAirport(Airport airport) {
        this.airport = airport;
    }
}
