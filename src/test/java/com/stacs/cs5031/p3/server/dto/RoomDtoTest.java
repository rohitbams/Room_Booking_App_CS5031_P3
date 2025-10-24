package com.stacs.cs5031.p3.server.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link RoomDto} class.
 * Tests the creation of RoomDto objects and verifies that getters 
 * return the expected values.
 */
public class RoomDtoTest {
    /** The RoomDto instance under test */
    private RoomDto roomDTO;

    /**
     * Setup before each test.
     * Creates a sample RoomDto object for testing.
     */
    @BeforeEach
    void setUp() {
        roomDTO = new RoomDto(1, "Room 1", 10, true);
    }

    /**
     * Tests that a RoomDto is created correctly with the specified values.
     * Verifies that all properties are set properly during construction.
     */
    @Test
    void shouldCreateRoomDTO() {
        assertEquals(1, roomDTO.getId(), "Should have correct ID");
        assertEquals("Room 1", roomDTO.getName(), "Should have correct name");
        assertEquals(10, roomDTO.getCapacity(), "Should have correct capacity");
        assertTrue(roomDTO.isAvailable(), "Should be available");
    }

    /**
     * Tests that the getId method returns the correct ID value.
     */
    @Test
    void shouldGetId() {
        assertEquals(1, roomDTO.getId());
    }

    /**
     * Tests that the getName method returns the correct name value.
     */
    @Test
    void shouldGetName() {
        assertEquals("Room 1", roomDTO.getName());
    }

    /**
     * Tests that the getCapacity method returns the correct capacity value.
     */
    @Test
    void shouldGetCapacity() {
        assertEquals(10, roomDTO.getCapacity());
    }

    /**
     * Tests that the isAvailable method correctly reflects the availability status.
     * Verifies both available and unavailable room statuses.
     */
    @Test
    void shouldCheckAvailability() {
        RoomDto availableRoom = new RoomDto(1, "Room 1", 10, true);
        RoomDto unavailableRoom = new RoomDto(2, "Room 2", 15, false);

        assertTrue(availableRoom.isAvailable(), "Room should be available");
        assertFalse(unavailableRoom.isAvailable(), "Room should be unavailable");
    }
}
