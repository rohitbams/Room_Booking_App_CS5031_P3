package com.stacs.cs5031.p3.server.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


/**
 *  This class is responsible for testing the Organiser class.
 * @author 190031593
 */
public class OrganiserTest{

    private Organiser organiser;

    /**
     * This method is responsible for creating an Organiser object and ensuring that it is created without any issues.
     */
    @Test
    void shouldCreateAnOrganiserObject(){
        organiser = new Organiser( "James Dean", "james.dean", "password123");
    }

    /**
     * This method is responsible for testing the getter methods of the Organiser class.
     */
    @Test
    void shouldGetOrganiserDetailsWithoutIssue(){
        organiser = new Organiser( "James Dean", "james.dean", "password123");
        assertEquals("James Dean", organiser.getName());
        assertEquals("james.dean", organiser.getUsername());
        assertEquals("password123", organiser.getPassword());
    }

    /**
     * This method is responsible for testing the setter methods of the Organiser class.
     */
    @Test
    void shouldSetOrganiserDetailsWithoutIssue(){
        organiser = new Organiser( "James Dean", "james.dean", "password123");
        organiser.setName("Holly Doe");
        organiser.setUsername("holly.doe");
        organiser.setPassword("password456");

        assertEquals("Holly Doe", organiser.getName());
        assertEquals("holly.doe", organiser.getUsername());
        assertEquals("password456", organiser.getPassword());
    }

}