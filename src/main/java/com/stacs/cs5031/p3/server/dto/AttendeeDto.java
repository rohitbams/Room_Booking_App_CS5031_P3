package com.stacs.cs5031.p3.server.dto;

import java.util.List;

/**
 * AttendeeDTO class.
 * This class implements data transfer objects for Attendee entities.
 * It is used for transferring user data to clients without exposing
 * sensitive data.
 */
public class AttendeeDto {
    private final Integer id;
    private final String username;
    private final String name;
    private final List<Integer> registeredBookingIds;

    /**
     * Constructor.
     * @param id attendee ID
     * @param username attendee username
     * @param name attendee name
     * @param registeredBookingIds booking IDs of registered bookings
     */
    public AttendeeDto(Integer id, String username, String name, List<Integer> registeredBookingIds) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.registeredBookingIds = registeredBookingIds;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }


    public List<Integer> getRegisteredBookingIds() {
        return registeredBookingIds;
    }
}