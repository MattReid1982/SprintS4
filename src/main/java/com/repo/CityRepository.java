package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.model.City;

/**
 * Repository interface for performing CRUD operations on City entities.
 * Extends {@link JpaRepository} to inherit standard database interaction methods.
 */
public interface CityRepository extends JpaRepository<City, Long> {
}
