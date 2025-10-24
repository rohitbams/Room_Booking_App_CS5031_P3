package com.stacs.cs5031.p3.server.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RegistrationRequest} class.
 * Tests the creation of RegistrationRequest objects and verifies that getters and setters
 * work as expected.
 */
public class RegistrationRequestTest {

    /**
     * Tests that a RegistrationRequest is created correctly with the specified values.
     * Verifies that the RegistrationRequest object is not null after creation.
     */
    @Test
    void shouldCreateRegistrationRequest() {
        String name = "Mary Oliver";
        String username = "maryoliver";
        String password = "wildegueese";
        String role = "ORGANISER";
        RegistrationRequest request = new RegistrationRequest(name, username, password, role);
        assertNotNull(request, "registration request should not be null");
    }

    /**
     * Tests that the getRole method returns the correct role value.
     * Verifies that the role provided during construction is retrievable.
     */
    @Test
    void shouldGetRole() {
        RegistrationRequest request = new RegistrationRequest("Mary Oliver", "maryoliver", "wildgueese", "Attendee");
        String result = request.getRole();
        assertEquals("Attendee", result, "should return the correct role");
    }

    /**
     * Tests that the setRole method correctly updates the role.
     * Verifies that the role can be changed after object creation.
     */
    @Test
    void shouldSetRole() {
        RegistrationRequest request = new RegistrationRequest("John Green", "johngreen", "nerdfighter", "ORGANISER");
        request.setRole("ATTENDEE");
        assertEquals("ATTENDEE", request.getRole(), "should update the role");
    }

    /**
     * Tests that the getName method returns the correct name value.
     * Verifies that the name provided during construction is retrievable.
     */
    @Test
    void shouldGetName() {
        RegistrationRequest request = new RegistrationRequest("Mary Oliver", "maryoliver", "wildgueese", "Attendee");
        String result = request.getName();
        assertEquals("Mary Oliver", result, "should return the correct name");
    }

    /**
     * Tests that the setName method correctly updates the name.
     * Verifies that the name can be changed after object creation.
     */
    @Test
    void shouldSetName() {
        RegistrationRequest request = new RegistrationRequest("John Green", "johngreen", "nerdfighter", "ORGANISER");
        request.setName("Jane Green");
        assertEquals("Jane Green", request.getName(), "should update the name");
    }

    /**
     * Tests that the getUsername method returns the correct username value.
     * Verifies that the username provided during construction is retrievable.
     */
    @Test
    void shouldGetUsername() {
        RegistrationRequest request = new RegistrationRequest("Mary Oliver", "maryoliver", "wildgueese", "Attendee");
        String result = request.getUsername();
        assertEquals("maryoliver", result, "should return the correct username");
    }

    /**
     * Tests that the setUsername method correctly updates the username.
     * Verifies that the username can be changed after object creation.
     */
    @Test
    void shouldSetUsername() {
        RegistrationRequest request = new RegistrationRequest("John Green", "johngreen", "nerdfighter", "ORGANISER");
        request.setUsername("greenj");
        assertEquals("greenj", request.getUsername(), "should update the username");
    }

    /**
     * Tests that the getPassword method returns the correct password value.
     * Verifies that the password provided during construction is retrievable.
     */
    @Test
    void shouldGetPassword() {
        RegistrationRequest request = new RegistrationRequest("Mary Oliver", "maryoliver", "wildgueese", "Attendee");
        String result = request.getPassword();
        assertEquals("wildgueese", result, "should return the correct password");
    }

    /**
     * Tests that the setPassword method correctly updates the password.
     * Verifies that the password can be changed after object creation.
     */
    @Test
    void shouldSetPassword() {
        RegistrationRequest request = new RegistrationRequest("John Green", "johngreen", "nerdfighter", "ORGANISER");
        request.setPassword("newpassword");
        assertEquals("newpassword", request.getPassword(), "should update the password");
    }
}