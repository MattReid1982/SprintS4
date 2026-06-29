package com.example.airportapi.repo;

import com.example.airportapi.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interface for performing CRUD operations on Airport entities.
// Extends JpaRepository to inherit standard CRUD methods.
public interface AirportRepository extends JpaRepository<Airport, Long> {

//    Finds all airports associated with a specific city ID.
//    This method is automatically implemented by Spring Data JPA based on the method name.
    List<Airport> findByCityId(Long cityId);
}
