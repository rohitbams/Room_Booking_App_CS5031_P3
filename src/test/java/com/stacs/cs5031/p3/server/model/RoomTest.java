package com.stacs.cs5031.p3.server.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for the {@link Room} class.
 * Tests the creation of Room objects and verifies that getters and behavior methods
 * work as expected.
 */
public class RoomTest {
    /** The Room instance under test */
    private Room room;

    /**
     * Setup before each test.
     * Creates a sample Room object for testing with predefined values and
     * simulates database-assigned ID.
     */
    @BeforeEach
    void setup() {
        room = new Room("Room 1", 3);

        // Simulate the database persistence behavior
        ReflectionTestUtils.setField(room, "id", 1);
    }

    /**
     * Tests that a Room is created correctly.
     * Verifies that the Room object is not null after creation.
     */
    @Test
    void shouldCreateRoom() {
        assertNotNull(room, "room should not be null");
    }

    /**
     * Tests that the getID method returns the correct ID value.
     * Verifies that the ID set via reflection is retrievable.
     */
    @Test
    void shouldGetRoomID() {
        assertEquals(1, room.getID(), "room ID should be 1");
    }

    /**
     * Tests that the getName method returns the correct room name.
     * Verifies that the name provided during construction is retrievable.
     */
    @Test
    void shouldGetRoomName() {
        assertEquals("Room 1", room.getName(), "room name should be 'Room 1'");
    }

    /**
     * Tests that a newly created room is available by default.
     * Verifies that the availability flag is initialized to true.
     */
    @Test
    void shouldBeAvailableByDefault() {
        assertTrue(room.isAvailable(), "room should be available by default");
    }

    /**
     * Tests that the getCapacity method returns the correct capacity value.
     * Verifies that the capacity provided during construction is retrievable.
     */
    @Test
    void shouldGetRoomCapacity() {
        assertEquals(3, room.getCapacity(), "room capacity should be 3");
    }

    /**
     * Tests the room availability toggling methods.
     * Verifies that:
     * 1. The room is initially available
     * 2. After booking, the room becomes unavailable
     * 3. After making the room available, it becomes available again
     */
    @Test
    void shouldToggleAvailability() {
        assertTrue(room.isAvailable(), "room should be available initially");

        room.bookRoom();
        assertFalse(room.isAvailable(), "room should not be available after booking");

        room.makeAvailable();
        assertTrue(room.isAvailable(), "room should be available after being made available");
    }
}
