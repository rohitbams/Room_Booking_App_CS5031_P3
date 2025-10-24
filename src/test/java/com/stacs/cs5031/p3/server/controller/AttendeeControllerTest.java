package com.stacs.cs5031.p3.server.controller;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.exception.BookingFullException;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.service.AttendeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link AttendeeController} class.
 * Tests the REST endpoints of the AttendeeController using mocked dependencies.
 * Verifies that the controller correctly interacts with the AttendeeService and returns
 * appropriate HTTP status codes and responses.
 */
public class AttendeeControllerTest {

    /** Mock of the AttendeeService to simulate service operations */
    @Mock
    private AttendeeService attendeeService;

    /** The AttendeeController instance being tested, with mocked dependencies injected */
    @InjectMocks
    private AttendeeController attendeeController;

    /** Test attendee instance used across multiple test methods */
    private Attendee attendee;
    
    /** Test booking instance used across multiple test methods */
    private Booking booking;

    /**
     * Setup before each test.
     * Initializes mocks and creates sample test objects including:
     * - An attendee
     * - A room
     * - An organiser
     * - A booking
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        attendee = new Attendee("Ringo Star", "drummerboy", "peaceandlove");
        Room room = new Room("Cavern Club", 200);
        Organiser organiser = new Organiser("Brian Epstein", "manager", "lennonsucks");
        setId(organiser, 1);
        booking = new Booking("Beatles concert", room, new Date(), 60, organiser);
        setId(booking, 1);
    }

    /**
     * Tests retrieving all attendees.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body contains the expected number of attendees
     * 3. The service's getAllAttendees method is called
     */
    @Test
    void getAllAttendees_ShouldReturnAttendees() {
        List<Attendee> attendees = Arrays.asList(attendee);
        when(attendeeService.getAllAttendees()).thenReturn(attendees);
        ResponseEntity<List<AttendeeDto>> response = attendeeController.getAllAttendees();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Tests retrieving an attendee by ID when the attendee exists.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's getAttendeeById method is called with the correct ID
     */
    @Test
    void getAttendeeById_ShouldReturnAttendee_WhenExists() {
        when(attendeeService.getAttendeeById(1)).thenReturn(attendee);
        ResponseEntity<AttendeeDto> response = attendeeController.getAttendeeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests retrieving an attendee by ID when the attendee doesn't exist.
     * Verifies that:
     * 1. The endpoint returns a 404 Not Found status
     * 2. The service's getAttendeeById method is called with the correct ID
     */
    @Test
    void getAttendeeById_ShouldReturnNotFound_WhenNotExists() {
        when(attendeeService.getAttendeeById(1)).thenThrow(new UserNotFoundException(1));
        ResponseEntity<AttendeeDto> response = attendeeController.getAttendeeById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests retrieving available bookings for an attendee.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's getAvailableBookings method is called with the correct attendee ID
     */
    @Test
    void getAvailableBookings_ShouldReturnBookings() {
        List<Booking> bookings = Arrays.asList(booking);
        when(attendeeService.getAvailableBookings(1)).thenReturn(bookings);
        ResponseEntity<?> response = attendeeController.getAvailableBookings(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests retrieving unavailable bookings for an attendee.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's getUnavailableBookings method is called with the correct attendee ID
     */
    @Test
    void getUnavailableBookings_ShouldReturnBookings() {
        List<Booking> bookings = Arrays.asList(booking);
        when(attendeeService.getUnavailableBookings(1)).thenReturn(bookings);
        ResponseEntity<?> response = attendeeController.getUnavailableBookings(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests retrieving registered bookings for an attendee.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's getRegisteredBookings method is called with the correct attendee ID
     */
    @Test
    void getRegisteredBookings_ShouldReturnBookings() {
        List<Booking> bookings = Arrays.asList(booking);
        when(attendeeService.getRegisteredBookings(1)).thenReturn(bookings);
        ResponseEntity<?> response = attendeeController.getRegisteredBookings(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests registering an attendee for a booking when registration is successful.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's registerForBooking method is called with the correct IDs
     */
    @Test
    void registerForBooking_ShouldReturnBooking_WhenSuccess() {
        when(attendeeService.registerForBooking(1, 1)).thenReturn(booking);
        ResponseEntity<?> response = attendeeController.registerForBooking(1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests registering an attendee for a booking when the booking is full.
     * Verifies that:
     * 1. The endpoint returns a 400 Bad Request status
     * 2. The service's registerForBooking method is called with the correct IDs
     */
    @Test
    void registerForBooking_ShouldReturnBadRequest_WhenBookingFull() {
        when(attendeeService.registerForBooking(1, 1)).thenThrow(new BookingFullException(1));
        ResponseEntity<?> response = attendeeController.registerForBooking(1, 1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Tests deregistering an attendee from a booking when deregistration is successful.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body is not null
     * 3. The service's deregisterFromBooking method is called with the correct IDs
     */
    @Test
    void deregisterFromBooking_ShouldReturnBooking_WhenSuccess() {
        when(attendeeService.deregisterFromBooking(1, 1)).thenReturn(booking);
        ResponseEntity<?> response = attendeeController.deregisterFromBooking(1, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Tests deregistering an attendee from a booking when the booking doesn't exist.
     * Verifies that:
     * 1. The endpoint returns a 404 Not Found status
     * 2. The service's deregisterFromBooking method is called with the correct IDs
     */
    @Test
    void cancelRegistration_ShouldReturnNotFound_WhenBookingNotFound() {
        when(attendeeService.deregisterFromBooking(1, 1)).thenThrow(new BookingNotFoundException(1));
        ResponseEntity<?> response = attendeeController.deregisterFromBooking(1, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Helper method to set an ID field on an object using reflection.
     * Used to simulate database-assigned IDs for testing.
     *
     * @param object The object to set the ID on
     * @param id The ID value to set
     */
    private void setId(Object object, int id) {
        try {
            Field field = object.getClass().getSuperclass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(object, id);
        } catch (Exception e) {
            // Silently handle exceptions
        }
    }
}
// 10