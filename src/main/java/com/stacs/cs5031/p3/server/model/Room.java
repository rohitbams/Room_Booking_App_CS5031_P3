package com.stacs.cs5031.p3.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity class representing a room in the system.
 * This class is persisted in the database and contains all properties and behaviors of a room.
 */
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private int capacity;
    private boolean availability;

    /**
     * Default constructor required by JPA.
     * This constructor is protected and should not be used directly.
     */
    protected Room() {}
    
    /**
     * Constructs a new Room with the specified name and capacity.
     * The room is initially available.
     *
     * @param name     The name of the room
     * @param capacity The maximum capacity of the room
     */
    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.availability = true;
    }

    /**
     * Checks if the room is currently available.
     *
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable() {
        return availability;
    }

    /**
     * Books the room by setting its availability to false.
     */
    public void bookRoom() {
        this.availability = false;
    }

    /**
     * Makes the room available by setting its availability to true.
     */
    public void makeAvailable() {
        this.availability = true;
    }

    /**
     * Returns the unique identifier of the room.
     *
     * @return The room ID
     */
    public int getID() {
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
}
