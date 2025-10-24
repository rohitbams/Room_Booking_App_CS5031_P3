package com.stacs.cs5031.p3.server.dto;

/**
 * This class represents an Organiser DTO
 */
public class OrganiserDto {

    private final int id; // id of the organiser
    private final String name; // name of the organiser
    private final String username; // username of the organiser

    public OrganiserDto(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    /**
     * This method returns the id of the organiser.
     * @return the id of the organiser
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the name of the organiser.
     * @return the name of the organiser
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the username of the organiser.
     * @return the username of the organiser
     */
    public String getUsername() {
        return username;
    }
}
