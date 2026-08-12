package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Airline entity in the database.
 * Each airline operates multiple planes.
 */
@Entity
@Table(name = "airline")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airline {

    /** Primary key for the Airline table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Airline company name (e.g., "Air Canada"). */
    @Column(nullable = false)
    private String name;

    /** IATA airline code (e.g., "AC"). */
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    /** One-to-many relationship with planes operated by this airline. */
    @OneToMany(mappedBy = "airline")
    @JsonIgnoreProperties({"airline", "airports", "passengers"})
    private List<Plane> planes = new ArrayList<>();

    /**
     * Default constructor for JPA.
     */
    public Airline() {
    }

    /**
     * Constructs an Airline with name and code.
     *
     * @param name airline name
     * @param code IATA code
     */
    public Airline(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plane> planes) {
        this.planes = planes;
    }
}
