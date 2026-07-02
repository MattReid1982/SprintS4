package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.model.Plane;

import java.util.List;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {
    List<Plane> findByPassengersId(Long passengerId);
}
