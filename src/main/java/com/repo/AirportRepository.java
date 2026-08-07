package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.model.Airport;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Airport entities.
 * Extends {@link JpaRepository} to inherit standard database interaction methods.
 */
public interface AirportRepository extends JpaRepository<Airport, Long> {

    /**
     * Finds all airports associated with a specific city ID.
     *
     * @param cityId the ID of the city
     * @return list of matching {@link Airport} entities
     */
    List<Airport> findByCityId(Long cityId);
}
