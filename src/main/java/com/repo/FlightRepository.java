package com.repository;

import com.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing Flight entities
 * <p>Provides standard CRUD operations for Flight objects through
 * Spring Data JPA</p>
 */
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
