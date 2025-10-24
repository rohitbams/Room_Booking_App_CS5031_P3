package com.stacs.cs5031.p3.server.service;

import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.AttendeeRepository;
import com.stacs.cs5031.p3.server.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AttendeeService} class.
 * Tests the business logic of the AttendeeService using mocked dependencies.
 * Verifies that the service correctly interacts with repositories and handles
 * attendee-related operations like registration, deregistration, and booking management.
 */
public class AttendeeServiceTest {
    /** Mock of the AttendeeRepository to simulate data access operations */
    @Mock
    private AttendeeRepository attendeeRepository;

    /** Mock of the BookingRepository to simulate booking data access operations */
    @Mock
    private BookingRepository bookingRepository;

    /** The AttendeeService instance being tested, with mocked dependencies injected */
    @InjectMocks
    private AttendeeService attendeeService;

    /** Test attendee instance used across multiple test methods */
    private Attendee attendee;
    
    /** Test booking instance used across multiple test methods */
    private Booking booking;
    
    /** Test date instance used for booking start time */
    private Date startTime;

    /**
     * Setup before each test.
     * Initializes mocks and creates sample objects for testing.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        attendee = new Attendee("Harry Potter", "theboywholived", "july31");
        Room room = new Room("Great Hall ", 400);
        Organiser organiser = new Organiser("Albus Dumbledore", "headmaster", "ihatestudents");
        startTime = new Date();
        booking = new Booking("event_name", room, startTime, 60, organiser);
    }

    /**
     * Tests that an attendee can be retrieved by ID when the attendee exists.
     * Verifies that:
     * 1. The returned Attendee matches the expected attendee
     * 2. The repository's findById method is called with the correct ID
     */
    @Test
    void getAttendeeById_ShouldReturnAttendee_WhenAttendeeExists() {
        when(attendeeRepository.findById(1)).thenReturn(Optional.of(attendee));
        Attendee result = attendeeService.getAttendeeById(1);
        assertNotNull(result);
        assertEquals(attendee, result);
        verify(attendeeRepository).findById(1);
    }

    /**
     * Tests that a UserNotFoundException is thrown when attempting to retrieve a non-existent attendee by ID.
     * Verifies that:
     * 1. The correct exception is thrown
     * 2. The repository's findById method is called with the correct ID
     */
    @Test
    void getAttendeeById_ShouldThrowException_WhenAttendeeDoesNotExist() {
        when(attendeeRepository.findById(10)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> attendeeService.getAttendeeById(10));
        verify(attendeeRepository).findById(10);
    }

    /**
     * Tests that an attendee can be retrieved by username when the attendee exists.
     * Verifies that:
     * 1. The returned Attendee matches the expected attendee
     * 2. The repository's findByUsername method is called with the correct username
     */
    @Test
    void getAttendeeByUsername_shouldReturnAttendee_whenAttendeeExists() {
        when(attendeeRepository.findByUsername("theboywholived")).thenReturn(attendee);
        Attendee result = attendeeService.getAttendeeByUsername("theboywholived");
        assertEquals(attendee, result);
        verify(attendeeRepository).findByUsername("theboywholived");
    }

    /**
     * Tests that a UserNotFoundException is thrown when attempting to retrieve a non-existent attendee by username.
     * Verifies that:
     * 1. The correct exception is thrown
     * 2. The repository's findByUsername method is called with the correct username
     */
    @Test
    void getAttendeeByUsername_shouldThrowUserNotFoundException_whenAttendeeDoesNotExist() {
        when(attendeeRepository.findByUsername("nonexistent")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> {
            attendeeService.getAttendeeByUsername("nonexistent");
        });
        verify(attendeeRepository).findByUsername("nonexistent");
    }

    /**
     * Tests that available bookings can be retrieved for an attendee.
     * Verifies that:
     * 1. The returned list contains the expected bookings
     * 2. The repository's findById and findAvailableBookings methods are called with the correct parameters
     */
    @Test
    void getAvailableBookings_ShouldReturnBookings() {
        when(attendeeRepository.findById(1)).thenReturn(Optional.of(attendee));
        when(attendeeRepository.findAvailableBookings(1)).thenReturn(Arrays.asList(booking));
        List<Booking> result = attendeeService.getAvailableBookings(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));
        verify(attendeeRepository).findById(1);
        verify(attendeeRepository).findAvailableBookings(1);
    }

    /**
     * Tests that unavailable bookings can be retrieved for an attendee.
     * Verifies that:
     * 1. The returned list contains the expected bookings
     * 2. The repository's findById and findUnavailableBookings methods are called with the correct parameters
     */
    @Test
    void getUnavailableBookings_shouldReturnUnavailableBookings() {
        Integer attendeeId = 1;
        when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));
        List<Booking> unavailableBookings = Collections.singletonList(booking);
        when(attendeeRepository.findUnavailableBookings(attendeeId)).thenReturn(unavailableBookings);
        List<Booking> result = attendeeService.getUnavailableBookings(attendeeId);
        assertEquals(unavailableBookings, result);
        verify(attendeeRepository).findUnavailableBookings(attendeeId);
    }

    /**
     * Tests that all attendees can be retrieved.
     * Verifies that:
     * 1. The returned list contains the expected number of attendees
     * 2. The returned list matches the expected list of attendees
     * 3. The repository's findAll method is called
     */
    @Test
    void getAllAttendees_shouldReturnAllAttendees() {
        List<Attendee> attendees = Arrays.asList(attendee, new Attendee("Ron Weasley", "ronaldw", "brokenwand"));
        when(attendeeRepository.findAll()).thenReturn(attendees);
        List<Attendee> result = attendeeService.getAllAttendees();
        assertEquals(attendees, result);
        assertEquals(2, result.size());
        verify(attendeeRepository).findAll();
    }

    /**
     * Tests that an attendee can register for a booking when the booking has space.
     * Verifies that:
     * 1. The returned Booking matches the expected booking
     * 2. The repository methods are called with the correct parameters
     * 3. The attendee and booking are saved after registration
     */
    @Test
    void registerForBooking_ShouldRegisterAttendee_WhenBookingHasSpace() {
        when(attendeeRepository.findById(1)).thenReturn(Optional.of(attendee));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(attendeeRepository.save(any(Attendee.class))).thenReturn(attendee);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        Booking result = attendeeService.registerForBooking(1, 1);
        assertNotNull(result);
        verify(attendeeRepository).findById(1);
        verify(bookingRepository).findById(1L);
        verify(attendeeRepository).save(attendee);
        verify(bookingRepository).save(booking);
    }

    /**
     * Tests that an attendee can deregister from a booking they are registered for.
     * Verifies that:
     * 1. The returned Booking matches the expected booking
     * 2. The attendee is removed from the booking's attendee list
     * 3. The booking is removed from the attendee's registered bookings
     * 4. The attendee and booking are saved after deregistration
     */
    @Test
    void deregisterFromBooking_shouldRemoveBookingFromAttendee_whenRegistered() {
        Integer attendeeId = 1;
        long bookingId = 1;
        when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Set<Booking> registeredBookings = new HashSet<>();
        registeredBookings.add(booking);
        attendee.setRegisteredBookings(registeredBookings);
        Set<Attendee> attendees = new HashSet<>();
        attendees.add(attendee);
        booking.setAttendees(attendees);

        Booking result = attendeeService.deregisterFromBooking(attendeeId, bookingId);
        assertEquals(booking, result);
        assertEquals(0, attendee.getRegisteredBookings().size());
        assertEquals(0, booking.getAttendees().size());
        verify(attendeeRepository).save(attendee);
        verify(bookingRepository).save(booking);
    }

    /**
     * Tests that registered bookings can be retrieved for an attendee.
     * Verifies that:
     * 1. The returned list contains the expected bookings
     * 2. The repository's findById and findRegisteredBookings methods are called with the correct parameters
     */
    @Test
    void getRegisteredBookings_shouldReturnBookings_whenAttendeeExists() {
        Integer attendeeId = 1;
        when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));
        List<Booking> bookings = Arrays.asList(booking);
        when(attendeeRepository.findRegisteredBookings(attendeeId)).thenReturn(bookings);
        List<Booking> result = attendeeService.getRegisteredBookings(attendeeId);
        assertEquals(bookings, result);
        verify(attendeeRepository).findById(attendeeId);
        verify(attendeeRepository).findRegisteredBookings(attendeeId);
    }

    /**
     * Tests that a UserNotFoundException is thrown when attempting to retrieve registered bookings for a non-existent attendee.
     * Verifies that:
     * 1. The correct exception is thrown
     * 2. The repository's findById method is called with the correct ID
     * 3. The repository's findRegisteredBookings method is never called
     */
    @Test
    void getRegisteredBookings_shouldThrowUserNotFoundException_whenAttendeeDoesNotExist() {
        Integer attendeeId = 10;
        when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            attendeeService.getRegisteredBookings(attendeeId);
        });
        verify(attendeeRepository).findById(attendeeId);
        verify(attendeeRepository, never()).findRegisteredBookings(any());
    }
}
// 11