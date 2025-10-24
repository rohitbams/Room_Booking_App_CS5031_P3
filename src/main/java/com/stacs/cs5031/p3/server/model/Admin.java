package com.stacs.cs5031.p3.server.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity class representing an administrator in the system.
 * Administrators have special privileges for managing rooms, users, and other system aspects.
 * This class extends the base User class and is mapped to the User table with a discriminator value of "ADMIN".
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    /**
     * Default constructor required by JPA.
     * This constructor is used by the JPA provider when instantiating entities from the database.
     */
    public Admin() {
    }
    
    /**
     * Constructs a new Admin with the specified name, username, and password.
     * This constructor calls the superclass constructor to initialize these basic user attributes.
     *
     * @param name     The full name of the administrator
     * @param username The unique username for the administrator
     * @param password The password for the administrator account
     */
    public Admin(String name, String username, String password) {
        super(name, username, password);
    }
}
