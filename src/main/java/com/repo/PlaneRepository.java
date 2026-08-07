package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.model.Plane;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Plane entities.
 * Extends {@link JpaRepository} to inherit standard database interaction methods.
 */
@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {

    /**
     * Finds all planes associated with a specific passenger ID.
     *
     * @param passengerId the ID of the passenger
     * @return list of matching {@link Plane} entities
     */
    List<Plane> findByPassengersId(Long passengerId);
}
