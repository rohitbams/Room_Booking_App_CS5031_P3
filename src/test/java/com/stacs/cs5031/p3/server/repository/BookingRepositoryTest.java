//import com.stacs.cs5031.p3.server.model.Organiser;
//import com.stacs.cs5031.p3.server.model.Room;
//import com.stacs.cs5031.p3.server.repository.RoomRepository;
//import com.stacs.cs5031.p3.server.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//public package com.stacs.cs5031.p3.server;
//
//import com.stacs.cs5031.p3.server.model.Organiser;
//import com.stacs.cs5031.p3.server.model.Room;
//import com.stacs.cs5031.p3.server.repository.RoomRepository;
//import com.stacs.cs5031.p3.server.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import com.stacs.cs5031.p3.server.model.Booking;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Tests for the BookingRepository using Spring Data JPA.
// */
//@DataJpaTest
//public class BookingRepositoryTest {
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private Room room1;
//    private Room room2;
//    private Organiser organiser1;
//    private Attendee attendee1;
//    private Attendee attendee2;
//    private Booking booking1;
//    private Booking booking2;
//    private Booking booking3;
//    private Date startTime1;
//    private Date startTime2;
//    private Date startTime3;
//
//    @BeforeEach
//    public void setUp() {
//        // Create rooms
//        room1 = new Room(5);
//        room1.setName("Conference Room A");
//        room2 = new Room(10);
//        room2.setName("Conference Room B");
//        roomRepository.saveAll(Arrays.asList(room1, room2));
//
//        // Create users
//        organiser1 = new Organiser("organiser1", "password", "John Organiser", "john@example.com");
//        attendee1 = new Attendee("attendee1", "password", "Alice Attendee", "alice@example.com");
//        attendee2 = new Attendee("attendee2", "password", "Bob Attendee", "bob@example.com");
//        userRepository.saveAll(Arrays.asList(organiser1, attendee1, attendee2));
//
//        // Create start times
//        Calendar calendar = Calendar.getInstance();
//        startTime1 = calendar.getTime();
//
//        calendar.add(Calendar.HOUR, 2);
//        startTime2 = calendar.getTime();
//
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        startTime3 = calendar.getTime();
//
//        // Create bookings
//        booking1 = new Booking("Meeting 1", room1, startTime1, 60, organiser1);
//        booking2 = new Booking("Meeting 2", room1, startTime2, 60, organiser1);
//        booking3 = new Booking("Meeting 3", room2, startTime3, 120, organiser1);
//
//        // Add attendees to bookings
//        booking1.addAttendee(attendee1);
//        booking1.addAttendee(attendee2);
//        booking2.addAttendee(attendee1);
//        booking3.addAttendee(attendee2);
//
//        // Save bookings
//        bookingRepository.saveAll(Arrays.asList(booking1, booking2, booking3));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        bookingRepository.deleteAll();
//        userRepository.deleteAll();
//        roomRepository.deleteAll();
//    }
//
//    @Test
//    public void shouldSaveAndFindBookings() {
//        // Booking1, booking2, booking3 are already saved in setUp()
//        List<Booking> foundBookings = StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//
//        assertEquals(3, foundBookings.size());
//        assertTrue(foundBookings.contains(booking1));
//        assertTrue(foundBookings.contains(booking2));
//        assertTrue(foundBookings.contains(booking3));
//    }
//
//    @Test
//    public void shouldFindBookingById() {
//        Booking foundBooking = bookingRepository.findById(booking1.getId()).orElse(null);
//
//        assertNotNull(foundBooking);
//        assertEquals(booking1.getName(), foundBooking.getName());
//        assertEquals(booking1.getRoom(), foundBooking.getRoom());
//        assertEquals(booking1.getStartTime(), foundBooking.getStartTime());
//        assertEquals(booking1.getDuration(), foundBooking.getDuration());
//        assertEquals(booking1.getOrganiser(), foundBooking.getOrganiser());
//        assertEquals(2, foundBooking.getAttendees().size());
//    }
//
//    @Test
//    public void shouldFindBookingsByRoomId() {
//        List<Booking> roomBookings = bookingRepository.findByRoomId(room1.getID());
//
//        assertEquals(2, roomBookings.size());
//        assertTrue(roomBookings.contains(booking1));
//        assertTrue(roomBookings.contains(booking2));
//        assertFalse(roomBookings.contains(booking3));
//    }
//
//    @Test
//    public void shouldFindBookingsByOrganiserId() {
//        List<Booking> organiserBookings = bookingRepository.findByOrganiserId(organiser1.getId());
//
//        assertEquals(3, organiserBookings.size());
//        assertTrue(organiserBookings.contains(booking1));
//        assertTrue(organiserBookings.contains(booking2));
//        assertTrue(organiserBookings.contains(booking3));
//    }
//
//    @Test
//    public void shouldFindBookingsByAttendeeId() {
//        List<Booking> attendee1Bookings = bookingRepository.findByAttendeeId(attendee1.getId());
//        List<Booking> attendee2Bookings = bookingRepository.findByAttendeeId(attendee2.getId());
//
//        assertEquals(2, attendee1Bookings.size());
//        assertTrue(attendee1Bookings.contains(booking1));
//        assertTrue(attendee1Bookings.contains(booking2));
//
//        assertEquals(2, attendee2Bookings.size());
//        assertTrue(attendee2Bookings.contains(booking1));
//        assertTrue(attendee2Bookings.contains(booking3));
//    }
//
//    @Test
//    public void shouldFindBookingsByTimeRange() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime1);
//        calendar.add(Calendar.MINUTE, -10);
//        Date rangeStart = calendar.getTime();
//
//        calendar.setTime(startTime2);
//        calendar.add(Calendar.MINUTE, 10);
//        Date rangeEnd = calendar.getTime();
//
//        List<Booking> rangeBookings = bookingRepository.findByTimeRange(rangeStart, rangeEnd);
//
//        assertEquals(2, rangeBookings.size());
//        assertTrue(rangeBookings.contains(booking1));
//        assertTrue(rangeBookings.contains(booking2));
//        assertFalse(rangeBookings.contains(booking3));
//    }
//
//    @Test
//    public void shouldFindConflictingBookings() {
//        // Create a time that overlaps with booking1
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime1);
//        calendar.add(Calendar.MINUTE, 30);
//        Date conflictStart = calendar.getTime();
//
//        calendar.add(Calendar.MINUTE, 60);
//        Date conflictEnd = calendar.getTime();
//
//        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
//                room1.getID(), conflictStart, conflictEnd);
//
//        assertEquals(1, conflictingBookings.size());
//        assertTrue(conflictingBookings.contains(booking1));
//    }
//
//    @Test
//    public void shouldDeleteBookingsByRoomId() {
//        bookingRepository.deleteByRoomId(room1.getID());
//
//        List<Booking> allBookings = StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//
//        assertEquals(1, allBookings.size());
//        assertTrue(allBookings.contains(booking3));
//        assertFalse(allBookings.contains(booking1));
//        assertFalse(allBookings.contains(booking2));
//    }
//
//    @Test
//    public void shouldDeleteBookingsByOrganiserId() {
//        bookingRepository.deleteByOrganiserId(organiser1.getId());
//
//        List<Booking> allBookings = StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//
//        assertEquals(0, allBookings.size());
//    }
//
//    @Test
//    public void shouldUpdateBooking() {
//        Booking savedBooking = bookingRepository.findById(booking1.getId()).orElse(null);
//        assertNotNull(savedBooking);
//
//        // Update booking properties
//        savedBooking.setEventName("Updated Meeting");
//        savedBooking.setDuration(90);
//        savedBooking.setRoom(room2);
//
//        // Remove an attendee
//        Attendee attendee = savedBooking.getAttendees().get(0);
//        savedBooking.removeAttendee(attendee);
//
//        bookingRepository.save(savedBooking);
//
//        // Retrieve updated booking
//        Booking updatedBooking = bookingRepository.findById(savedBooking.getId()).orElse(null);
//        assertNotNull(updatedBooking);
//
//        // Check if properties were updated
//        assertEquals("Updated Meeting", updatedBooking.getName());
//        assertEquals(90, updatedBooking.getDuration());
//        assertEquals(room2, updatedBooking.getRoom());
//        assertEquals(1, updatedBooking.getAttendees().size());
//    }
//}
