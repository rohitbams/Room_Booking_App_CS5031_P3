package com.stacs.cs5031.p3.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Attendee} class.
 * Tests the creation of Attendee objects and verifies that the booking registration/deregistration 
 * functionality works as expected.
 */
public class AttendeeTest {

    /** The Attendee instance under test */
    private Attendee attendee;
    
    /** Test booking instance used across multiple test methods */
    private Booking booking;
    
    /** Test date instance used for booking start time */
    private Date startTime;

    /**
     * Setup before each test.
     * Creates a sample Attendee object and a Booking for testing with predefined values.
     */
    @BeforeEach
    void setup() {
        attendee = new Attendee("John Doe", "johndoe", "password123");

        // mock booking
        Room room = new Room("name ", 1);
        Organiser organiser = new Organiser("organiser_name", "organiser", "password");
        startTime = new Date();
        booking = new Booking("event_name", room, startTime, 60, organiser);
    }

    /**
     * Tests that an Attendee is created correctly with the specified values.
     * Verifies that:
     * 1. The Attendee object is not null after creation
     * 2. The name, username, and password properties match the expected values
     */
    @Test
    void shouldCreateAttendee() {
        assertNotNull(attendee, "attendee should not be null");
        assertEquals("John Doe", attendee.getName(), "name should match");
        assertEquals("johndoe", attendee.getUsername(), "username should match");
        assertEquals("password123", attendee.getPassword(), "password should match");
    }

    /**
     * Tests that a newly created Attendee has an empty registered bookings list.
     * Verifies that the registered bookings set is empty initially.
     */
    @Test
    void shouldHaveEmptyBookingsListByDefault() {
        assertTrue(attendee.getRegisteredBookings().isEmpty(), "registered bookings should be empty initially");
    }

    /**
     * Tests that an Attendee can register for a Booking.
     * Verifies that:
     * 1. After registration, the registered bookings list has size 1
     * 2. The booking is present in the registered bookings set
     */
    @Test
    void shouldRegisterForBooking() {
        attendee.registerForBooking(booking);
        assertEquals(1, attendee.getRegisteredBookings().size(), "should have one registered booking");
        assertTrue(attendee.getRegisteredBookings().contains(booking), "should contain the registered booking");
    }

    /**
     * Tests that an Attendee can deregister from a Booking.
     * Verifies that:
     * 1. After registration, the registered bookings list has size 1
     * 2. After deregistration, the registered bookings list is empty
     */
    @Test
    void shouldDeregisterFromBooking() {
        attendee.registerForBooking(booking);
        assertEquals(1, attendee.getRegisteredBookings().size(), "should have one registered booking initially");
        attendee.deRegisterFromBooking(booking);
        assertTrue(attendee.getRegisteredBookings().isEmpty(), "should have no registered bookings after deregistering");
    }

    /**
     * Tests that an Attendee cannot register for the same Booking twice.
     * Verifies that after attempting to register for the same booking twice,
     * the registered bookings list still has only one entry.
     */
    @Test
    void shouldNotRegisterForSameBookingTwice() {
        attendee.registerForBooking(booking);
        attendee.registerForBooking(booking); // Register again
        assertEquals(1, attendee.getRegisteredBookings().size(), "should have only one booking registration");
    }
}
// 5