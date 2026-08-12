package com.repo;

import com.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Gate entities.
 * Extends {@link JpaRepository} to inherit standard database interaction methods.
 */
@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

    /**
     * Finds all gates associated with a specific airport ID.
     *
     * @param airportId the ID of the airport
     * @return list of matching {@link Gate} entities
     */
    List<Gate> findByAirportId(Long airportId);
}
