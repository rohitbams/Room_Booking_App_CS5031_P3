package com.stacs.cs5031.p3.server.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.service.OrganiserService;
import com.stacs.cs5031.p3.server.service.RoomService;

/**
 * This class is the Organiser controller.
 * @author 190031593
 */
@RestController
@RequestMapping("/")
public class OrganiserController {

    @Autowired
    private OrganiserService organiserService;
    @Autowired
    private RoomService roomService;

    /**
     * This method is used to create an organiser and save it to the database.
     * @param organiser - The organiser to be created.
     * @return the status of the operation
     * @throws IllegalArgumentException - if the organiser credentials are invalid
     * @throws Exception - if there is an error in the server
     */
    @PostMapping("/organiser/create-organiser")
    public ResponseEntity<String> createOrganiser(@RequestBody Organiser organiser) {
        try {
            organiserService.createOrganiser(organiser);
            return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS!");
        } catch (IllegalArgumentException e) { // if organiser credentials are invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) { // if there is an error in the server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error!");
        }
    }

    /**
     * This method gets all the organisers. For admins to use.
     * @return the list of organisers
     */
    @GetMapping(value = "/organisers", produces = { "application/json" })
    public ResponseEntity<ArrayList<OrganiserDto>> getAllOrganisers() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(organiserService.getAllOrganisers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * This method is used to get all available rooms.
     * @return the list of available rooms
     * @throws Exception - if there is an error in the server
     */
    @GetMapping(value = "/organiser/available-rooms", produces = { "application/json" })
    public ResponseEntity<ArrayList<RoomDto>> findAvailableRooms() {
        try {
            ArrayList<RoomDto> availableRooms = new ArrayList<>(roomService.findAvailableRooms());
            return ResponseEntity.status(HttpStatus.OK).body(availableRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * This method is used to get all bookings for an organiser.
     * @param organiserId - the id of the organiser
     * @return the list of bookings
     * @throws Exception - if there is an error in the server
     */
    @GetMapping(value = "/organiser/my-bookings/{organiserId}", produces = { "application/json" })
    public ResponseEntity<ArrayList<BookingDto>> getBookings(@PathVariable int organiserId) {
        try{
            ArrayList<BookingDto> bookings = organiserService.getBookings(organiserId);
            return ResponseEntity.status(HttpStatus.OK).body(bookings);
        } catch (Exception e) { //if there is an error in the server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    /**
     * This method is used to get a booking by its id.
     * @param bookingId - the id of the booking
     * @param organiserId - the id of the organiser
     * @return the booking
     * @throws BookingNotFoundException - if the booking is not found
     * @throws Exception - if there is an error in the server
     */
    @GetMapping(value = "/organiser/{organiserId}/my-bookings/{bookingId}", produces = { "application/json" })
    public ResponseEntity<BookingDto> getBooking(@PathVariable int bookingId, @PathVariable int organiserId) {
        try{
            BookingDto booking = organiserService.getBooking(bookingId, organiserId);
            if (booking == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(booking);
        } catch (BookingNotFoundException e) { //if booking request is invalid
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) { //if there is an error in the server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        
    }

    /**
     * This method is used to cancel/delete a booking.
     * 
     * @param bookingId   - the id of the booking
     * @param organiserId - the id of the organiser
     * @return status of the operation
     */
    @DeleteMapping("/organiser/cancel-booking/{bookingId}/{organiserId}")
    public ResponseEntity<String> cancelBooking(@PathVariable int bookingId, @PathVariable int organiserId) {
        try {
            organiserService.cancelBooking(bookingId, organiserId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("SUCCESS!");
        } catch (Exception e) { // if booking request is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * This creates a new booking.
     * 
     * @param request     - the booking request
     * @param organiserId - the id of the organiser
     * @return status of the operation
     */
    @PostMapping("/organiser/create-booking/{organiserId}")
    public ResponseEntity<String> createBooking(@RequestBody BookingDto.BookingRequest request,
            @PathVariable int organiserId) {
        try {
            String res = organiserService.createBooking(request, organiserId);
            if(res.equals("SUCCESS!")) { 
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
        } catch (Exception e) { // if booking request is invalid
            System.out.println("Error creating booking - controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * This method is used to get all attendees for a booking.
     * @param bookingId   - the id of the booking
     * @param organiserId - the id of the organiser
     * @return the list of attendees
     */
    @GetMapping("/organiser/{organiserId}/my-bookings/{bookingId}/attendees")
    public ResponseEntity<ArrayList<AttendeeDto>> getAttendees(@PathVariable int bookingId, @PathVariable int organiserId) {
        try{
            ArrayList<AttendeeDto> attendees = organiserService.getAttendees(bookingId, organiserId);
            if (attendees == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(attendees);
        } catch (Exception e) { //if there is an error in the server
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return null;
    }

}
