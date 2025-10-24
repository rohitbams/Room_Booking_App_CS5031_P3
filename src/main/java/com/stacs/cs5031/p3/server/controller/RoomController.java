package com.stacs.cs5031.p3.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotAvailableException;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.service.RoomService;

/**
 * REST controller for handling room-related operations.
 * Receives HTTP requests from the client layer, maps request data to DTO objects,
 * and sends these DTOs to the service layer.
 * All endpoints are accessible under the "/rooms" path.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    /**
     * Service that handles room-related business logic
     */
    @Autowired
    private RoomService roomService;

    /**
     * Retrieves all rooms in the system.
     *
     * @return ResponseEntity containing a list of all room DTOs and HTTP status 200 (OK),
     *         or HTTP status 204 (No Content) if no rooms exist
     */
    @GetMapping("/all")
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<RoomDto> rooms = roomService.findAllRooms();

        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(rooms);
    }

    /**
     * Retrieves a specific room by its ID.
     *
     * @param id The unique identifier of the room
     * @return ResponseEntity containing the room DTO and HTTP status 200 (OK) if found,
     *         or HTTP status 404 (Not Found) if the room does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable int id) {
        try {
            RoomDto foundRoom = roomService.findRoomDtoById(id);
            return ResponseEntity.ok(foundRoom);
        } catch (RoomNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all available rooms in the system.
     *
     * @return ResponseEntity containing a list of available room DTOs and HTTP status 200 (OK),
     *         or HTTP status 204 (No Content) if no available rooms exist
     */
    @GetMapping("/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms() {
        List<RoomDto> rooms = roomService.findAvailableRooms();
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(rooms);
    }

    /**
     * Books a room by its ID.
     *
     * @param id The unique identifier of the room to book
     * @return ResponseEntity containing the booked room DTO and HTTP status 200 (OK) if successful,
     *         HTTP status 400 (Bad Request) if the room is not available, or
     *         HTTP status 404 (Not Found) if the room does not exist
     */
    @PostMapping("/{id}/book")
    public ResponseEntity<RoomDto> bookRoom(@PathVariable int id) {
        try {
            // service returns DTO
            RoomDto bookedRoom = roomService.bookRoom(id);
            return ResponseEntity.ok(bookedRoom);
        } catch (RoomNotAvailableException e) {
            return ResponseEntity.badRequest().build();
        } catch (RoomNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Makes a room available by its ID.
     *
     * @param id The unique identifier of the room to make available
     * @return ResponseEntity containing the available room DTO and HTTP status 200 (OK) if successful,
     *         or HTTP status 404 (Not Found) if the room does not exist
     */
    @PostMapping("/{id}/makeAvailable")
    public ResponseEntity<RoomDto> makeRoomAvailable(@PathVariable int id) {
        try {
            RoomDto availableRoom = roomService.makeRoomAvailable(id);
            return ResponseEntity.ok(availableRoom);
        } catch (RoomNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
