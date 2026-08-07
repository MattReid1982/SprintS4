package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Secondary application entry point for Sprint S4 Spring Boot application.
 */
@SpringBootApplication
public class SprintS4Application {

    /**
     * Starts the Sprint S4 application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        System.setProperty("spring.classformat.ignore", "true");
        SpringApplication.run(SprintS4Application.class, args);
    }
}
