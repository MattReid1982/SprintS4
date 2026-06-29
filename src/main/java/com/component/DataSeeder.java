package com.component;

import com.model.Passenger;
import com.repo.PassengerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PassengerRepository passengerRepository;

    public DataSeeder(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (passengerRepository.count() == 0) {
            // Create Passengers
            Passenger p1 = new Passenger("John", "Doe", "555-1234");
            Passenger p2 = new Passenger("Jane", "Smith", "555-5678");
            Passenger p3 = new Passenger("Bob", "Johnson", "555-9012");

            passengerRepository.saveAll(Arrays.asList(p1, p2, p3));
            
            System.out.println("Passenger data seeded successfully!");
        }
    }
}
