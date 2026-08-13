package com.repo;

import com.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing Flight entities
 * Provides standard CRUD operations for Flight objects
 * Spring Data JPA
 */
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
