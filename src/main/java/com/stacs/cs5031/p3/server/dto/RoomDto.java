package com.stacs.cs5031.p3.server.dto;

/**
 * Data Transfer Object (DTO) representing a room in the system.
 * This class is immutable and used to transfer room data between different layers of the application.
 */
public class RoomDto {
    // immutable DTO
    private final int id;
    private final String name;
    private final int capacity;
    private final boolean availability;

    /**
     * Constructs a new RoomDto with the specified properties.
     *
     * @param id           The unique identifier of the room
     * @param name         The name of the room
     * @param capacity     The maximum capacity of the room
     * @param availability Whether the room is currently available
     */
    public RoomDto(int id, String name, int capacity, boolean availability) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.availability = availability;
    }

    /**
     * Returns the unique identifier of the room.
     *
     * @return The room ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the room.
     *
     * @return The room name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the maximum capacity of the room.
     *
     * @return The room capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Indicates whether the room is currently available.
     *
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable() {
        return availability;
    }
}
