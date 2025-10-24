package com.stacs.cs5031.p3.server.model;

import jakarta.persistence.Entity;

/**
 * This class represents an Organiser in the system. It is a subclass of User.
 * @author 190031593
 */
@Entity
public class Organiser extends User {
    public Organiser() {
        // Default constructor required by JPA
    }

    /**
     * The constructor for the Organiser class.
     * @param name - organiser name
     * @param username - organiser username
     * @param password - organiser password
     */
    public Organiser(String name, String username, String password) {
        super(name, username, password);
    }

}
