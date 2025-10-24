package com.stacs.cs5031.p3.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Room Booking System server.
 * This class serves as the entry point for the Spring Boot application.
 * It initializes the Spring context and starts the embedded web server.
 * 
 * The @SpringBootApplication annotation enables auto-configuration,
 * component scanning, and defines this as a configuration class.
 */
@SpringBootApplication
public class ServerApplication {

    /**
     * Main method that starts the Spring Boot application.
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
