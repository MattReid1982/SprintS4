package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Represents a User entity in the system for authentication and authorization.
 */
@Entity
@Table(name = "user_account")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /** Primary key for the User table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username for authentication. */
    @Column(nullable = false, unique = true)
    private String username;

    /** Password hash stored for the user. */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** Email address associated with the user account. */
    private String email;

    /** Role assigned to this user account (e.g. ADMIN, USER). */
    @Column(nullable = false)
    private String role = "ADMIN";

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructs a User with username, passwordHash, and email.
     *
     * @param username     username
     * @param passwordHash password hash
     * @param email        email address
     */
    public User(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = "ADMIN";
    }

    /**
     * Constructs a User with username, passwordHash, email, and role.
     *
     * @param username     username
     * @param passwordHash password hash
     * @param email        email address
     * @param role         role (e.g. ADMIN, USER)
     */
    public User(String username, String passwordHash, String email, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the user ID.
     *
     * @return user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password hash.
     *
     * @return password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the password hash.
     *
     * @param passwordHash password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
     * Gets the role.
     *
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role role
     */
    public void setRole(String role) {
        this.role = role;
    }
}
