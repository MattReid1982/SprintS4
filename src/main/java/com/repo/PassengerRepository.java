package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
