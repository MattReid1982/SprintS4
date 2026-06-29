package com.example.airportapi.repo;

import com.example.airportapi.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for performing CRUD operations on City entities.
// Extends JpaRepository to inherit standard CRUD methods.
public interface CityRepository extends JpaRepository <City, Long> {
}
