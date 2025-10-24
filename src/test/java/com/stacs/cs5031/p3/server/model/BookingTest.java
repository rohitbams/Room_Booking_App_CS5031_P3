//package com.stacs.cs5031.p3.server.model;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.util.ReflectionTestUtils;
//
//public class BookingTest {
//
//    private Room room;
//    private Room largerRoom;
//    private Organiser organiser;
//    private Booking booking;
//    private Attendee attendee1;
//    private Attendee attendee2;
//    private Attendee attendee3;
//    private Date startTime;
//
//    @BeforeEach
//    public void setUp() {
//        // Create a room with capacity of 2
//        room = new Room("Conference Room A", 2);
//        ReflectionTestUtils.setField(room, "id", 1);
//
//        // Create a larger room with capacity of 5
//        largerRoom = new Room("Conference Room B", 5);
//        ReflectionTestUtils.setField(largerRoom, "id", 2);
//
//        // Create an organiser
////        organiser = new Organiser("organiser1", "password", "John Doe", "john@example.com");
//        ReflectionTestUtils.setField(organiser, "id", 1);
//
//        // Create a start time (current time)
//        startTime = new Date();
//
//        // Create a booking
//        booking = new Booking("Team Meeting", room, startTime, 60, organiser);
//        ReflectionTestUtils.setField(booking, "id", 1);
//
//        // Create some attendees
//       /*  attendee1 = new Attendee("attendee1", "password", "Alice Johnson", "alice@example.com");
//        attendee2 = new Attendee("attendee2", "password", "Bob Smith", "bob@example.com");
//        attendee3 = new Attendee("attendee3", "password", "Charlie Brown", "charlie@example.com");
//        ReflectionTestUtils.setField(attendee1, "id", 1);
//        ReflectionTestUtils.setField(attendee2, "id", 2);
//        ReflectionTestUtils.setField(attendee3, "id", 3); */
//    }
//
//    @Test
//    public void testBookingCreation() {
//        // Test that booking is created with correct properties
//        assertEquals(1, booking.getId());
//        assertEquals("Team Meeting", booking.getName());
//        assertEquals(room, booking.getRoom());
//        assertEquals(startTime, booking.getStartTime());
//        assertEquals(60, booking.getDuration());
//        assertEquals(organiser, booking.getOrganiser());
//        assertEquals(0, booking.getAttendees().size());
//    }
//
//    @Test
//    public void testIsThereSpace() {
//        // Initially there should be space (room capacity is 2)
//        assertTrue(booking.isThereSpace());
//
//        // Add one attendee
//        booking.addAttendee(attendee1);
//        assertTrue(booking.isThereSpace());
//
//        // Add second attendee
//        booking.addAttendee(attendee2);
//        assertFalse(booking.isThereSpace());
//    }
//
//    @Test
//    public void testAddAttendee() {
//        // Add first attendee
//        assertTrue(booking.addAttendee(attendee1));
//        assertEquals(1, booking.getAttendees().size());
//        assertTrue(booking.getAttendees().contains(attendee1));
//
//        // Add second attendee
//        assertTrue(booking.addAttendee(attendee2));
//        assertEquals(2, booking.getAttendees().size());
//        assertTrue(booking.getAttendees().contains(attendee2));
//
//        // Try to add third attendee (should fail as room capacity is 2)
//        assertFalse(booking.addAttendee(attendee3));
//        assertEquals(2, booking.getAttendees().size());
//        assertFalse(booking.getAttendees().contains(attendee3));
//    }
//
//    @Test
//    public void testRemoveAttendee() {
//        // Add attendees
//        booking.addAttendee(attendee1);
//        booking.addAttendee(attendee2);
//
//        // Remove first attendee
//        assertTrue(booking.removeAttendee(attendee1));
//        assertEquals(1, booking.getAttendees().size());
//        assertFalse(booking.getAttendees().contains(attendee1));
//
//        // Try to remove an attendee that's not registered
//        assertFalse(booking.removeAttendee(attendee3));
//
//        // Remove second attendee
//        assertTrue(booking.removeAttendee(attendee2));
//        assertEquals(0, booking.getAttendees().size());
//    }
//
//    @Test
//    public void testGetEndTime() {
//        // Calculate expected end time (start time + 60 minutes)
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime);
//        calendar.add(Calendar.MINUTE, 60);
//        Date expectedEndTime = calendar.getTime();
//
//        // Check end time calculation
//        assertEquals(expectedEndTime, booking.getEndTime());
//    }
//
//    @Test
//    public void testOverlapsWithOverlappingBookings() {
//        // Create a calendar to manipulate dates
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime);
//
//        // Create dates for another booking that overlaps
//        // Start time is 30 minutes after the first booking starts
//        calendar.add(Calendar.MINUTE, 30);
//        Date overlappingStart = calendar.getTime();
//
//        // Create another booking in the same room that starts during the first booking
//        Booking overlappingBooking = new Booking("Overlapping Meeting", room, overlappingStart, 60, organiser);
//
//        // Check that the bookings overlap
//        assertTrue(booking.overlaps(overlappingBooking));
//        assertTrue(overlappingBooking.overlaps(booking));
//    }
//
//    @Test
//    public void testOverlapsWithNonOverlappingBookings() {
//        // Create a calendar to manipulate dates
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime);
//
//        // Create dates for another booking that doesn't overlap
//        // Start time is 2 hours after the first booking starts (well after it ends)
//        calendar.add(Calendar.HOUR, 2);
//        Date nonOverlappingStart = calendar.getTime();
//
//        // Create another booking in the same room that starts after the first booking ends
//        Booking nonOverlappingBooking = new Booking("Non-Overlapping Meeting", room, nonOverlappingStart, 60, organiser);
//
//        // Check that the bookings don't overlap
//        assertFalse(booking.overlaps(nonOverlappingBooking));
//        assertFalse(nonOverlappingBooking.overlaps(booking));
//    }
//
//    @Test
//    public void testOverlapsWithDifferentRooms() {
//        // Create a booking with the same time but in a different room
//        Booking differentRoomBooking = new Booking("Different Room Meeting", largerRoom, startTime, 60, organiser);
//
//        // Check that the bookings don't overlap (because they're in different rooms)
//        assertFalse(booking.overlaps(differentRoomBooking));
//        assertFalse(differentRoomBooking.overlaps(booking));
//    }
//
//    @Test
//    public void testUpdateBookingDetails() {
//        // Update booking details
//        booking.setEventName("Updated Meeting");
//        booking.setRoom(largerRoom);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime);
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        Date newDate = calendar.getTime();
//        booking.setStartTime(newDate);
//        booking.setDuration(90);
//
//        // Check that the values were updated
//        assertEquals("Updated Meeting", booking.getName());
//        assertEquals(largerRoom, booking.getRoom());
//        assertEquals(newDate, booking.getStartTime());
//        assertEquals(90, booking.getDuration());
//    }
//
//    @Test
//    public void testAddingAttendeeAfterRoomChange() {
//        // Add attendees to fill smaller room
//        booking.addAttendee(attendee1);
//        booking.addAttendee(attendee2);
//        assertFalse(booking.isThereSpace());
//
//        // Change to larger room
//        booking.setRoom(largerRoom);
//
//        // Now we should be able to add more attendees
//        assertTrue(booking.isThereSpace());
//        assertTrue(booking.addAttendee(attendee3));
//        assertEquals(3, booking.getAttendees().size());
//    }
//
//    @Test
//    public void testOrganiserBookingRelationship() {
//        // Initially, the organiser should not have the booking in their list
//        assertEquals(0, organiser.getCreatedBookings().size());
//
//        // Add the booking to the organiser
//        organiser.addBooking(booking);
//        assertEquals(1, organiser.getCreatedBookings().size());
//        assertTrue(organiser.getCreatedBookings().contains(booking));
//
//        // Remove the booking
//        assertTrue(organiser.removeBooking(booking));
//        assertEquals(0, organiser.getCreatedBookings().size());
//    }
//
//    @Test
//    public void testAttendeeBookingRelationship() {
//        // Initially, the attendee should not be registered for any bookings
//        assertEquals(0, attendee1.getRegisteredBookings().size());
//
//        // Register for the booking
//        assertTrue(attendee1.registerForBooking(booking));
//        assertEquals(1, attendee1.getRegisteredBookings().size());
//        assertTrue(attendee1.getRegisteredBookings().contains(booking));
//        assertTrue(booking.getAttendees().contains(attendee1));
//
//        // Unregister from the booking
//        assertTrue(attendee1.unregisterFromBooking(booking));
//        assertEquals(0, attendee1.getRegisteredBookings().size());
//        assertFalse(booking.getAttendees().contains(attendee1));
//    }
//}
