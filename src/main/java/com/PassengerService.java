package com;

import com.model.Passenger;
import com.repo.PassengerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

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

    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }
}
