package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Represents a Passenger entity in the system.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger {

    /** Primary key for the Passenger table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** First name of the passenger. */
    private String firstName;

    /** Last name of the passenger. */
    private String lastName;

    /** Contact phone number of the passenger. */
    private String phoneNumber;

    /** Email address of the passenger. */
    private String email;

    /** Passport number of the passenger. */
    private String passportNumber;

    /**
     * Default constructor for JPA.
     */
    public Passenger() {
    }

    /**
     * Constructs a Passenger with first name, last name, and phone number.
     *
     * @param firstName   first name
     * @param lastName    last name
     * @param phoneNumber phone number
     */
    public Passenger(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the passenger ID.
     *
     * @return passenger ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the passenger ID.
     *
     * @param id passenger ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the phone number.
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the passport number.
     *
     * @return passport number
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * Sets the passport number.
     *
     * @param passportNumber passport number
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
