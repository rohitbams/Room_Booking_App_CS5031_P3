package com.stacs.cs5031.p3.server.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.service.AdminService;

/**
 * REST controller for admin operations in the system.
 * Provides endpoints for managing rooms, attendees, and organisers.
 * All endpoints are accessible under the "/admin/" path.
 */
@RestController
@RequestMapping("/admin/")
public class AdminController {
    
    private final AdminService adminService;

    /**
     * Constructs a new AdminController with the specified AdminService.
     *
     * @param adminService the service that handles admin business logic
     */
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    /**
     * Retrieves all rooms in the system.
     *
     * @return ResponseEntity containing a list of all room DTOs and HTTP status 200 (OK)
     */
    @GetMapping("/rooms")
    public ResponseEntity<ArrayList<RoomDto>> getAllRooms() {
        ArrayList<RoomDto> rooms = adminService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
   
    /**
     * Retrieves all attendees registered in the system.
     *
     * @return ResponseEntity containing a list of all attendees and HTTP status 200 (OK)
     */
    @GetMapping("/attendees")
    public ResponseEntity<ArrayList<Attendee>> getAllAttendees() {
        ArrayList<Attendee> attendees = adminService.getAttendees();
        return ResponseEntity.ok(attendees);
    }

    /**
     * Retrieves all organisers registered in the system.
     *
     * @return ResponseEntity containing a list of all organiser DTOs and HTTP status 200 (OK)
     */
    @GetMapping("/organisers")
    public ResponseEntity<ArrayList<OrganiserDto>> getAllOrganisers() {
        ArrayList<OrganiserDto> organisers = adminService.getOrganisers();
        return ResponseEntity.ok(organisers);
    }

    /**
     * Adds a new room to the system.
     *
     * @param room the room object to be added
     * @return ResponseEntity with success message and HTTP status 200 (OK) if successful,
     *         or error message and HTTP status 400 (Bad Request) if failed
     */
    @PostMapping("/rooms")
    public ResponseEntity<String> addRoom(@RequestBody Room room) {
        boolean success = adminService.addRoom(room);
        if (success) {
            return ResponseEntity.ok("Room added successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add room");
        }
    }

    /**
     * Removes a room from the system by its ID.
     *
     * @param roomId the ID of the room to be removed
     * @return ResponseEntity with success message and HTTP status 200 (OK) if successful,
     *         or error message and HTTP status 400 (Bad Request) if failed
     */
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<String> removeRoom(@PathVariable int roomId) {
        boolean success = adminService.removeRoom(roomId);
        if (success) {
            return ResponseEntity.ok("Room removed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to remove room");
        }
    }
}
