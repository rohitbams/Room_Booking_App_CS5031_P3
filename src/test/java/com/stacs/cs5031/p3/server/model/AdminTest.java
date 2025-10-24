package com.stacs.cs5031.p3.server.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for the {@link Admin} class.
 * Tests the creation of Admin objects and verifies that getters
 * return the expected values.
 */
public class AdminTest {
    /** The Admin instance under test */
    private Admin admin;

    /**
     * Setup before each test.
     * Creates a sample Admin object for testing with predefined values.
     */
    @BeforeEach
    void setup() {
        // pass mock objects into the Admin instance
        admin = new Admin("Test Admin", "test.admin", "12345");
    }

    /**
     * Tests that an Admin is created correctly.
     * Verifies that the Admin object is not null after creation.
     */
    @Test
    void shouldCreateAdmin() {
        assertNotNull(admin, "admin should not be null");
    }

    /**
     * Tests that the getId method returns the correct ID value.
     * Uses ReflectionTestUtils to simulate database-generated ID.
     */
    @Test
    void shouldGetId() {
        // Simulate database persistence behaviour
        ReflectionTestUtils.setField(admin, "id", 1);
        assertEquals(1, admin.getId());
    }

    /**
     * Tests that the getName method returns the correct name value.
     * Verifies that the name provided during construction is retrievable.
     */
    @Test
    void shouldGetName() {
        assertEquals("Test Admin", admin.getName());
    
    }
    
    /**
     * Tests that the getUsername method returns the correct username value.
     * Verifies that the username provided during construction is retrievable.
     */
    @Test
    void shouldGetUsername() {
        assertEquals("test.admin", admin.getUsername());
    }

    /**
     * Tests that the getPassword method returns the correct password value.
     * Verifies that the password provided during construction is retrievable.
     */
    @Test
    void shouldGetPassword() {
        assertEquals("12345", admin.getPassword());
    }
}
