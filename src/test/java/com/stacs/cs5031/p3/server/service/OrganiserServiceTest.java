package com.stacs.cs5031.p3.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.OrganiserRepository;

/**
 * This class is responsible for testing the OrganiserService class.
 * 
 * @author 190031593
 */
public class OrganiserServiceTest {

    private Organiser organiser1, organiser2; // Organiser objects for testing
    private OrganiserService organiserService; // organiser service
    private BookingService bookingService; // mocked booking service
    private RoomService roomService; // room service

    private OrganiserRepository organiserRepository; // mocked repository

    @BeforeEach
    void setUp() {
        organiserRepository = Mockito.mock(OrganiserRepository.class);
        roomService = Mockito.mock(RoomService.class);
        bookingService = Mockito.mock(BookingService.class);
        organiserService = new OrganiserService(organiserRepository, roomService, bookingService);
        organiser1 = new Organiser("James Dean", "james.dean", "password123");
        organiser2 = new Organiser("Mary Dean", "mary.dean", "password123");

    }

    /**
     * Tests that an organiser can be created and saved successfully.
     */
    @Test
    void shouldCreateAnOrganiserWithoutIssue() {
        Mockito.when(organiserRepository.save(organiser1)).thenReturn(organiser1);
        assertEquals("SUCCESS!", organiserService.createOrganiser(organiser1));
    }

    /**
     * Tests that an exception is thrown when the organiser is null.
     */
    @Test
    void shouldThrowExceptionWhenOrganiserIsNull() {
        Mockito.when(organiserRepository.save(null))
                .thenThrow(new IllegalArgumentException("Organiser cannot be null"));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(null);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser cannot be null", thrown.getMessage());
    }

    /**
     * Tests that an exception is thrown when the organiser name is invalid.
     */
    @Test
    void shouldThrowExceptionWhenOrganiserNameIsInvalid() {
        organiser1.setName(null);
        Mockito.when(organiserRepository.save(organiser1))
                .thenThrow(new IllegalArgumentException("Organiser name is invalid"));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser name is invalid", thrown.getMessage());

        organiser1.setName("");

        thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser name is invalid", thrown.getMessage());
    }

    /**
     * Tests that an exception is thrown when the organiser username is invalid.
     */
    @Test
    void shouldThrowExceptionWhenOrganiserUserNameIsInvalid() {
        organiser1.setUsername(null);
        Mockito.when(organiserRepository.save(organiser1))
                .thenThrow(new IllegalArgumentException("Organiser username is invalid"));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser username is invalid", thrown.getMessage());

        organiser1.setUsername("");

        thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser username is invalid", thrown.getMessage());
    }

    /**
     * Tests that an exception is thrown when the organiser password is invalid.
     */
    @Test
    void shouldThrowExceptionWhenOrganiserPasswordIsInvalid() {
        organiser1.setPassword(null);
        Mockito.when(organiserRepository.save(organiser1))
                .thenThrow(new IllegalArgumentException("Organiser password is invalid"));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser password is invalid", thrown.getMessage());

        organiser1.setPassword("");

        thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organiserService.createOrganiser(organiser1);
        }, "Expected IllegalArgumentException to be thrown, but it was not.");

        assertEquals("Organiser password is invalid", thrown.getMessage());
    }

    /**
     * Tests that all organisers can be retrieved successfully.
     */
    @Test
    void shouldGetAllOrganisersWithoutIssue() {
        Organiser organiser3 = Mockito.mock(Organiser.class);
        Organiser organiser4 = Mockito.mock(Organiser.class);
        ArrayList<Organiser> organisers = new ArrayList<>();

        Mockito.when(organiserRepository.findAll()).thenReturn(organisers);

        Mockito.when(organiser3.getName()).thenReturn("James Dean");
        Mockito.when(organiser3.getUsername()).thenReturn("james.dean");
        Mockito.when(organiser3.getId()).thenReturn(1);

        Mockito.when(organiser4.getName()).thenReturn("Mary Dean");
        Mockito.when(organiser4.getUsername()).thenReturn("mary.dean");
        Mockito.when(organiser4.getId()).thenReturn(2);

        assertEquals(0, organiserService.getAllOrganisers().size());

        organisers.add(organiser3);
        organisers.add(organiser4);

        assertEquals(2, organiserService.getAllOrganisers().size());
        assertEquals(organiser1.getUsername(), organiserService.getAllOrganisers().get(0).getUsername());
        assertEquals(organiser2.getUsername(), organiserService.getAllOrganisers().get(1).getUsername());
    }

    /**
     * Tests that all available rooms can be retrieved successfully.
     */
    @Test
    void shouldGetAllAvailableRoomsWithoutIssue() {
        RoomDto room1 = new RoomDto(1, "JCB 1.3", 100, true);
        RoomDto room2 = new RoomDto(2, "JCB 2.2", 300, true);

        ArrayList<RoomDto> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        Mockito.when(roomService.findAvailableRooms()).thenReturn(rooms);
        assertEquals(2, organiserService.getAvailableRooms().size());
        assertEquals(room1, organiserService.getAvailableRooms().get(0));
        assertEquals(room2, organiserService.getAvailableRooms().get(1));
    }

    /**
     * Tests that all available rooms can be retrieved successfully. In this case,
     * no rooms are available.
     */
    @Test
    void shouldGetAllAvailableRoomsIfNoneExist() {
        ArrayList<RoomDto> rooms = new ArrayList<>();
        Mockito.when(roomService.findAvailableRooms()).thenReturn(rooms);
        assertEquals(0, organiserService.getAvailableRooms().size());
    }

    /**
     * Tests that an organiser can get all their bookings successfully.
     */
    @Test
    void shouldGetBookingsWithoutIssue() {

        Booking booking1 = Mockito.mock(Booking.class);
        Booking booking2 = Mockito.mock(Booking.class);
        Room room = Mockito.mock(Room.class);
        Organiser organiser = Mockito.mock(Organiser.class);

        Mockito.when(booking1.getRoom()).thenReturn(room);
        Mockito.when(booking2.getRoom()).thenReturn(room);

        Mockito.when(booking1.getOrganiser()).thenReturn(organiser);
        Mockito.when(booking2.getOrganiser()).thenReturn(organiser);

        Mockito.when(room.getID()).thenReturn(1);
        Mockito.when(organiser.getId()).thenReturn(1);

        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        Mockito.when(bookingService.getBookingsByOrganiser(1L)).thenReturn(bookings);
        organiserService.getBookings(1);
        Mockito.verify(bookingService, times(1)).getBookingsByOrganiser(1L);

    }

    /**
     * Tests that an organiser can get a booking by its ID successfully.
     */
    @Test
    void shouldGetBookingDetailsWithoutIssue() {
        Booking booking1 = Mockito.mock(Booking.class);
        Organiser organiser = Mockito.mock(Organiser.class);
        Room room = Mockito.mock(Room.class);

        Mockito.when(booking1.getRoom()).thenReturn(room);
        Mockito.when(room.getID()).thenReturn(1);
        Mockito.when(room.getCapacity()).thenReturn(100);
        Mockito.when(organiser.getId()).thenReturn(1);
        Mockito.when(booking1.getOrganiser()).thenReturn(organiser);
        Mockito.when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking1));

        assertNotEquals(null, organiserService.getBooking(1, 1));
    }

    /**
     * Test that an organiser can create a booking successfully.
     */
    @Test
    void shouldCreateBookingWithoutIssue() {
        BookingDto.BookingRequest bookingReq = Mockito.mock(BookingDto.BookingRequest.class);
        Booking booking = Mockito.mock(Booking.class);
        Mockito.when(bookingService.createBooking(bookingReq, 1L)).thenReturn(booking);
        Mockito.when(booking.getId()).thenReturn(1);
        String operationStatus = organiserService.createBooking(bookingReq, 1);
        Mockito.verify(bookingService, times(1)).createBooking(bookingReq, 1L);
        assertEquals("SUCCESS!", operationStatus);
    }

    /**
     * Test that an organiser can cancel a booking successfully.
     */
    @Test
    void shouldCancelBookingWithoutIssue() {
        Long bookingId = 1L;
        int organiserId = 1;
        Booking booking = Mockito.mock(Booking.class);
        Organiser organiser = Mockito.mock(Organiser.class);

        Mockito.when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(booking.getOrganiser()).thenReturn(organiser);
        Mockito.when(booking.getOrganiser().getId()).thenReturn(organiserId);
        Mockito.doNothing().when(bookingService).deleteBooking(bookingId);
        String res = organiserService.cancelBooking(bookingId.intValue(), organiserId);
        Mockito.verify(bookingService, times(1)).deleteBooking(bookingId);
        Mockito.verify(bookingService, times(1)).getBookingById(bookingId);
        assertEquals(res, "SUCCESS!");
    }

    /**
     * Test that an exception is thrown when the organiser tries to cancel a booking
     * that doesnt not exist.
     * 
     * @throws Exception - BookingNotFoundException
     */
    @Test
    void shouldNotCancelBookingIfBookingDoesntExist() {
        Long bookingId = 1L;
        int organiserId = 1;
        Mockito.when(bookingService.getBookingById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> {
            organiserService.cancelBooking(bookingId.intValue(), organiserId);
        });

    }

    /**
     * Test that an exception is thrown when the organiser tries to get a booking
     * that doesnt not exist.
     * 
     * @throws Exception - BookingNotFoundException
     */
    @Test
    void shouldNotCGetBookingIfBookingDoesntExist() {
        Long bookingId = 1L;
        int organiserId = 1;
        Mockito.when(bookingService.getBookingById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> {
            organiserService.getBooking(bookingId.intValue(), organiserId);
        });

    }

    /**
     * Test that an organiser cannot cancel a booking if they are not the organiser
     * of that booking.
     */
    @Test
    void shouldNotCancelBookingIfNotTheOrganiser() {
        Long bookingId = 1L;
        int organiserId = 1;
        Booking booking = Mockito.mock(Booking.class);
        Organiser organiser = Mockito.mock(Organiser.class);

        Mockito.when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(booking.getOrganiser()).thenReturn(organiser);
        Mockito.when(booking.getOrganiser().getId()).thenReturn(2);

        String res = organiserService.cancelBooking(bookingId.intValue(), organiserId);
        Mockito.verify(bookingService, times(0)).deleteBooking(bookingId);
        assertEquals(res, "You are not the organiser of this booking");

    }

    /**
     * Test that an organiser cannot get a booking if they are not the organiser
     * of that booking.
     */
    @Test
    void shouldReturnNullIfBookingDoesNotBelongToOrganiser() {
        Long bookingId = 1L;
        int organiserId = 1;
        Booking booking = Mockito.mock(Booking.class);
        Organiser organiser = Mockito.mock(Organiser.class);

        Mockito.when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));
        Mockito.when(booking.getOrganiser()).thenReturn(organiser);
        Mockito.when(booking.getOrganiser().getId()).thenReturn(2);

        BookingDto res = organiserService.getBooking(bookingId.intValue(), organiserId);
        assertEquals(null, res);
    }



    /**
     * Tests that an organiser can get all their booking attendees successfully.
     */
    @Test
    void shouldGetAttendeesForBookingWithoutIssue() {

        Booking booking1 = Mockito.mock(Booking.class);
        Room room = Mockito.mock(Room.class);
        Organiser organiser = Mockito.mock(Organiser.class);
        ArrayList<Attendee> attendees = new ArrayList<>();
        Attendee attendee = Mockito.mock(Attendee.class);
        Attendee attendee2 = Mockito.mock(Attendee.class);
        attendees.add(attendee);
        attendees.add(attendee2);

        Mockito.when(booking1.getRoom()).thenReturn(room);

        Mockito.when(booking1.getOrganiser()).thenReturn(organiser);
        Mockito.when(booking1.getAttendees()).thenReturn(attendees); 
        Mockito.when(attendee.getId()).thenReturn(1);
        Mockito.when(attendee2.getId()).thenReturn(2);

        Mockito.when(room.getID()).thenReturn(1);
        Mockito.when(organiser.getId()).thenReturn(1);

        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);

        Mockito.when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking1));
        ArrayList<AttendeeDto> attendeeDtos = organiserService.getAttendees(1,1);
        Mockito.verify(bookingService, times(1)).getBookingById(1L);
        assertEquals(2, attendeeDtos.size());
        assertEquals(attendee.getId(), attendeeDtos.get(0).getId());
        assertEquals(attendee2.getId(), attendeeDtos.get(1).getId());

    }


}
