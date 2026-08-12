package com.repo;

import com.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Airline entity CRUD operations.
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    /**
     * Finds an airline by its IATA code.
     *
     * @param code airline IATA code
     * @return optional airline
     */
    Optional<Airline> findByCode(String code);
}
