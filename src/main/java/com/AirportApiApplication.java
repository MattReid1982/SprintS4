package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application entry point for the Airport REST API service.
 */
@SpringBootApplication
public class AirportApiApplication {

    /**
     * Starts the Airport API Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        System.setProperty("spring.classformat.ignore", "true");
        SpringApplication.run(AirportApiApplication.class, args);
    }
}
