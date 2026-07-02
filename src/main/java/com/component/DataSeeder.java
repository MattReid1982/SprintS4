package com.component;

import com.model.Airport;
import com.model.City;
import com.model.Passenger;
import com.model.Plane;
import com.repo.AirportRepository;
import com.repo.CityRepository;
import com.repo.PassengerRepository;
import com.repo.PlaneRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;

    public DataSeeder(CityRepository cityRepository,
                      AirportRepository airportRepository,
                      PassengerRepository passengerRepository,
                      PlaneRepository planeRepository) {
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (planeRepository.count() == 0) {
            // Delete existing data to start clean and match the seed pattern
            planeRepository.deleteAll();
            passengerRepository.deleteAll();
            airportRepository.deleteAll();
            cityRepository.deleteAll();

            // 1. Create Cities
            City toronto = cityRepository.save(new City("Toronto", "Ontario", 3000000));
            City vancouver = cityRepository.save(new City("Vancouver", "British Columbia", 2300000));
            City calgary = cityRepository.save(new City("Calgary", "Alberta", 1400000));
            City montreal = cityRepository.save(new City("Montreal", "Quebec", 1800000));
            City stJohns = cityRepository.save(new City("St. John's", "Newfoundland", 110000));

            // 2. Create Airports and associate with Cities
            Airport yyz = new Airport("Toronto Pearson International Airport", "YYZ");
            yyz.setCity(toronto);
            Airport ytz = new Airport("Billy Bishop Toronto City Airport", "YTZ");
            ytz.setCity(toronto);
            Airport yvr = new Airport("Vancouver International Airport", "YVR");
            yvr.setCity(vancouver);
            Airport yyc = new Airport("Calgary International Airport", "YYC");
            yyc.setCity(calgary);
            Airport yul = new Airport("Montreal-Trudeau International Airport", "YUL");
            yul.setCity(montreal);

            airportRepository.saveAll(Arrays.asList(yyz, ytz, yvr, yyc, yul));

            // 3. Create Passengers
            Passenger alice = passengerRepository.save(new Passenger("Alice", "Nguyen", "555-0101"));
            Passenger brandon = passengerRepository.save(new Passenger("Brandon", "Lee", "555-0202"));
            Passenger carla = passengerRepository.save(new Passenger("Carla", "Patel", "555-0303"));
            Passenger david = passengerRepository.save(new Passenger("David", "Smith", "555-0404"));
            Passenger john = passengerRepository.save(new Passenger("John", "Doe", "555-1234"));
            Passenger jane = passengerRepository.save(new Passenger("Jane", "Smith", "555-5678"));
            Passenger bob = passengerRepository.save(new Passenger("Bob", "Johnson", "555-9012"));
            Passenger keith = passengerRepository.save(new Passenger("Keith", "Bishop", "7097865464"));

            // 4. Create Planes and associate with Airports and Passengers
            Plane plane1 = new Plane();
            plane1.setType("Boeing 737");
            plane1.setAirlineName("Air Canada");
            plane1.setNumOfPassengers(160);
            plane1.setAirports(Arrays.asList(yyz, yvr));
            plane1.setPassengers(Arrays.asList(alice, brandon, john));

            Plane plane2 = new Plane();
            plane2.setType("Airbus A320");
            plane2.setAirlineName("WestJet");
            plane2.setNumOfPassengers(150);
            plane2.setAirports(Arrays.asList(ytz, yyc));
            plane2.setPassengers(Arrays.asList(carla, bob, john));

            Plane plane3 = new Plane();
            plane3.setType("Boeing 777");
            plane3.setAirlineName("Air Transat");
            plane3.setNumOfPassengers(300);
            plane3.setAirports(Arrays.asList(yyz, yyc, yul));
            plane3.setPassengers(Arrays.asList(alice, david, jane));

            planeRepository.saveAll(Arrays.asList(plane1, plane2, plane3));

            System.out.println("Passenger, City, Airport, and Plane data seeded successfully!");
        }
    }
}
