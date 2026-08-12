package com.component;

import com.model.Airport;
import com.model.City;
import com.model.Passenger;
import com.model.Plane;
import com.model.User;
import com.repo.AirportRepository;
import com.repo.CityRepository;
import com.repo.PassengerRepository;
import com.repo.PlaneRepository;
import com.repo.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Component that seeds initial data into the database upon application startup.
 * Populates sample cities, airports, passengers, planes, and default users.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    /** Repository for city persistence. */
    private final CityRepository cityRepository;

    /** Repository for airport persistence. */
    private final AirportRepository airportRepository;

    /** Repository for passenger persistence. */
    private final PassengerRepository passengerRepository;

    /** Repository for plane persistence. */
    private final PlaneRepository planeRepository;

    /** Repository for user persistence. */
    private final UserRepository userRepository;

    /**
     * Constructs a DataSeeder component with required repositories.
     *
     * @param cityRepository      city repository
     * @param airportRepository   airport repository
     * @param passengerRepository passenger repository
     * @param planeRepository     plane repository
     * @param userRepository      user repository
     */
    public DataSeeder(CityRepository cityRepository,
                      AirportRepository airportRepository,
                      PassengerRepository passengerRepository,
                      PlaneRepository planeRepository,
                      UserRepository userRepository) {
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Runs the data seeding process if no planes currently exist in the database.
     *
     * @param args command line arguments
     * @throws Exception if an error occurs during seeding
     */
    @Override
    @Transactional
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "password123", "admin@airport.com"));
            System.out.println("Default admin user seeded successfully!");
        }

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
            cityRepository.save(new City("St. John's", "Newfoundland", 110000));

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

            airportRepository.saveAll(List.of(yyz, ytz, yvr, yyc, yul));

            // 3. Create Passengers
            Passenger alice = passengerRepository.save(new Passenger("Alice", "Nguyen", "555-0101"));
            Passenger brandon = passengerRepository.save(new Passenger("Brandon", "Lee", "555-0202"));
            Passenger carla = passengerRepository.save(new Passenger("Carla", "Patel", "555-0303"));
            Passenger david = passengerRepository.save(new Passenger("David", "Smith", "555-0404"));
            Passenger john = passengerRepository.save(new Passenger("John", "Doe", "555-1234"));
            Passenger jane = passengerRepository.save(new Passenger("Jane", "Smith", "555-5678"));
            Passenger bob = passengerRepository.save(new Passenger("Bob", "Johnson", "555-9012"));
            passengerRepository.save(new Passenger("Keith", "Bishop", "7097865464"));

            // 4. Create Planes and associate with Airports and Passengers
            Plane plane1 = new Plane();
            plane1.setType("Boeing 737");
            plane1.setAirlineName("Air Canada");
            plane1.setNumOfPassengers(160);
            plane1.setAirports(List.of(yyz, yvr));
            plane1.setPassengers(List.of(alice, brandon, john));

            Plane plane2 = new Plane();
            plane2.setType("Airbus A320");
            plane2.setAirlineName("WestJet");
            plane2.setNumOfPassengers(150);
            plane2.setAirports(List.of(ytz, yyc));
            plane2.setPassengers(List.of(carla, bob, john));

            Plane plane3 = new Plane();
            plane3.setType("Boeing 777");
            plane3.setAirlineName("Air Transat");
            plane3.setNumOfPassengers(300);
            plane3.setAirports(List.of(yyz, yyc, yul));
            plane3.setPassengers(List.of(alice, david, jane));

            planeRepository.saveAll(List.of(plane1, plane2, plane3));

            System.out.println("Passenger, City, Airport, and Plane data seeded successfully!");
        }
    }
}
