package com.service;

import com.model.Airport;
import com.model.Passenger;
import com.repo.PassengerRepository;
import com.repo.PlaneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository, PlaneRepository planeRepository) {
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Passenger updatePassenger(Long id, Passenger updatedPassenger) {
        return passengerRepository.findById(id)
                .map(passenger -> {
                    passenger.setFirstName(updatedPassenger.getFirstName());
                    passenger.setLastName(updatedPassenger.getLastName());
                    passenger.setPhoneNumber(updatedPassenger.getPhoneNumber());
                    return passengerRepository.save(passenger);
                })
                .orElseThrow(() -> new RuntimeException("Passenger not found with id " + id));
    }

    @Transactional(readOnly = false)
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    public List<Plane> getPlanesForPassenger(Long passengerId) {
        return planeRepository.findByPassengersId(passengerId);
    }

    public List<Airport> getAirportsUsedByPassenger(Long passengerId) {
        return planeRepository.findByPassengersId(passengerId).stream()
                .flatMap(plane -> plane.getAirports().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
