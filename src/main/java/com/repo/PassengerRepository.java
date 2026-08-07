package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Passenger;

/**
 * Repository interface for performing CRUD operations on Passenger entities.
 * Extends {@link JpaRepository} to inherit standard database interaction methods.
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
