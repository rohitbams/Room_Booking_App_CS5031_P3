package com.stacs.cs5031.p3.server.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.AdminRepository;
import org.springframework.stereotype.Service;

/**
 * Service class that handles administrative operations in the system.
 * Provides methods for room management, user management, and other admin operations.
 * Acts as a facade over various individual service components for admin-specific functions.
 */
@Service
public class AdminService {
    private AdminRepository adminRepository;
    private final RoomService roomService;
    private final AttendeeService attendeeService;
    private final OrganiserService organiserService;
    
    /**
     * Constructs a new AdminService with necessary dependencies.
     *
     * @param adminRepository  Repository for admin data access
     * @param roomService      Service for room operations
     * @param attendeeService  Service for attendee operations
     * @param organiserService Service for organiser operations
     */
    @Autowired
    public AdminService(AdminRepository adminRepository, RoomService roomService, AttendeeService attendeeService, OrganiserService organiserService) {
        this.adminRepository = adminRepository;
        this.roomService = roomService;
        this.attendeeService = attendeeService;
        this.organiserService = organiserService;
    }

    /**
     * Retrieves all rooms in the system.
     *
     * @return ArrayList of RoomDto objects representing all rooms
     */
    public ArrayList<RoomDto> getAllRooms() {
        return new ArrayList<>(roomService.findAllRooms());
    }
    
    /**
     * Retrieves all attendees registered in the system.
     *
     * @return ArrayList of Attendee objects
     */
    public ArrayList<Attendee> getAttendees() {
        return new ArrayList<>(attendeeService.getAllAttendees());
    }

    /**
     * Retrieves all organisers registered in the system.
     *
     * @return ArrayList of OrganiserDto objects
     */
    public ArrayList<OrganiserDto> getOrganisers() {
        return organiserService.getAllOrganisers();
    }

    /**
     * Adds a new room to the system.
     *
     * @param room The Room object to add
     * @return true if the room was successfully added, false otherwise
     */
    public boolean addRoom(Room room) {
        try {
            roomService.createRoom(room.getName(), room.getCapacity());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Removes a room from the system by its ID.
     *
     * @param roomId The ID of the room to remove
     * @return true if the room was successfully removed, false if the room was not found
     */
    public boolean removeRoom(int roomId) {
        try {
            roomService.deleteRoomById(roomId);
            return true;
        } catch (RoomNotFoundException e) {
            return false;
        }
    }
}
