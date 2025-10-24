package com.stacs.cs5031.p3.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link User} class.
 * Tests the creation of User objects and verifies that getters and setters
 * work as expected.
 */
public class UserTest {
    /** The User instance under test */
    private User user;

    /**
     * Setup before each test.
     * Creates a sample User object for testing with predefined values.
     */
    @BeforeEach
    void setup() {
        user = new User("Alice Bob", "alicebob", "qwerty");
    }

    /**
     * Tests that a User is created correctly.
     * Verifies that the User object is not null after creation.
     */
    @Test
    void shouldCreateUser() {
        assertNotNull(user, "user should not be null");
    }

    /**
     * Tests that the getName method returns the correct name value.
     * Verifies that the name provided during construction is retrievable.
     */
    @Test
    void shouldGetUserName() {
        assertEquals("Alice Bob", user.getName(), "User's name should be 'Alice Bob'");
    }

    /**
     * Tests that the getUsername method returns the correct username value.
     * Verifies that the username provided during construction is retrievable.
     */
    @Test
    void shouldGetUsername() {
        assertEquals("alicebob", user.getUsername(), "User's username should be 'alicebob'");
    }

    /**
     * Tests that the getPassword method returns the correct password value.
     * Verifies that the password provided during construction is retrievable.
     */
    @Test
    void shouldGetPassword() {
        assertEquals("qwerty", user.getPassword(), "User's password should be 'qwerty'");
    }

    /**
     * Tests that the setUsername method correctly updates the username.
     * Verifies that the username can be changed after object creation.
     */
    @Test
    void shouldSetUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername(), "username should be updated to 'newusername'");
    }

    /**
     * Tests that the setName method correctly updates the name.
     * Verifies that the name can be changed after object creation.
     */
    @Test
    void shouldSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName(), "name should be updated to 'Jane Doe'");
    }

    /**
     * Tests that the setPassword method correctly updates the password.
     * Verifies that the password can be changed after object creation.
     */
    @Test
    void shouldSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword(), "password should be updated to 'newpassword'");
    }
}
// 7