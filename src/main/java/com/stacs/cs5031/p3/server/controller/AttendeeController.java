package com.stacs.cs5031.p3.server.controller;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.exception.BookingFullException;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.mapper.AttendeeDtoMapper;
import com.stacs.cs5031.p3.server.mapper.BookingDtoMapper;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.service.AttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AttendeeController class.
 * This class is a REST API controller for Attendee operations.
 */
@RestController
@RequestMapping("/attendees")
public class AttendeeController {

    private final AttendeeService attendeeService;

    /**
     * Constructor
     */
    @Autowired
    public AttendeeController(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }


    /**
     * Get all attendees.
     *
     * @return List of attendeeDtos
     */
    @GetMapping
    public ResponseEntity<List<AttendeeDto>> getAllAttendees() {
        List<Attendee> attendees = attendeeService.getAllAttendees();
        return ResponseEntity.ok(AttendeeDtoMapper.mapToDtoList(attendees));
    }


    /**
     * Get an attendee by ID.
     *
     * @param id The attendee ID
     * @return The attendee DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendeeDto> getAttendeeById(@PathVariable Integer id) {
        try {
            Attendee attendee = attendeeService.getAttendeeById(id);
            return ResponseEntity.ok(AttendeeDtoMapper.mapToDto(attendee));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets all available bookings for an attendee.
     *
     * @param id The attendee ID
     * @return List of available booking DTOs
     */
    @GetMapping("/{id}/available-bookings")
    public ResponseEntity<?> getAvailableBookings(@PathVariable Integer id) {
        try {
            List<Booking> bookings = attendeeService.getAvailableBookings(id);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTOList(bookings));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get all unavailable bookings for an attendee.
     *
     * @param id The attendee ID
     * @return List of unavailable booking DTOs
     */
    @GetMapping("/{id}/unavailable-bookings")
    public ResponseEntity<?> getUnavailableBookings(@PathVariable Integer id) {
        try {
            List<Booking> bookings = attendeeService.getUnavailableBookings(id);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTOList(bookings));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get all bookings an attendee has registered for.
     *
     * @param id The attendee ID
     * @return List of registered booking DTOs
     */
    @GetMapping("/{id}/registered-bookings")
    public ResponseEntity<?> getRegisteredBookings(@PathVariable Integer id) {
        try {
            List<Booking> bookings = attendeeService.getRegisteredBookings(id);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTOList(bookings));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Register an attendee for a booking.
     *
     * @param attendeeId The attendee ID
     * @param bookingId The booking ID
     * @return The updated booking DTO
     */
    @PostMapping("/{attendeeId}/register/{bookingId}")
    public ResponseEntity<?> registerForBooking(
            @PathVariable Integer attendeeId,
            @PathVariable long bookingId) {

        try {
            Booking booking = attendeeService.registerForBooking(attendeeId, bookingId);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee not found: " + e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
        } catch (BookingFullException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * De-register from a booking that was pre-registered by the attendee.
     *
     * @param attendeeId The attendee ID
     * @param bookingId The booking ID
     * @return The updated booking DTO
     */
    @DeleteMapping("/{attendeeId}/cancel/{bookingId}")
    public ResponseEntity<?> deregisterFromBooking(
            @PathVariable Integer attendeeId,
            @PathVariable long bookingId) {

        try {
            Booking booking = attendeeService.deregisterFromBooking(attendeeId, bookingId);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee not found: " + e.getMessage());
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
        }
    }
}
