package com.stacs.cs5031.p3.server.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.dto.BookingDto;

/**
 * Service interface for operations on Booking entities.
 */
public interface BookingService {

    /**
     * Gets all bookings in the system.
     *
     * @return List of all bookings
     */
    List<Booking> getAllBookings();

    /**
     * Gets a booking by its ID.
     *
     * @param id The booking ID
     * @return Optional containing the booking if found
     */
    Optional<Booking> getBookingById(Long id);

    /**
     * Saves a booking.
     *
     * @param booking The booking to save
     * @return The saved booking
     */
    Booking saveBooking(Booking booking);

    /**
     * Deletes a booking by its ID.
     *
     * @param id The ID of the booking to delete
     */
    void deleteBooking(Long id);

    /**
     * Registers an attendee for a booking.
     *
     * @param bookingId The booking ID
     * @param attendeeId The attendee ID
     * @return The updated booking
     * @throws com.stacs.cs5031.p3.server.exception.EntityNotFoundException if booking or attendee not found
     * @throws com.stacs.cs5031.p3.server.exception.ResourceUnavailableException if the room is at capacity
     */
    Booking registerAttendee(Long bookingId, Long attendeeId);

    /**
     * Unregisters an attendee from a booking.
     *
     * @param bookingId The booking ID
     * @param attendeeId The attendee ID
     * @return The updated booking
     * @throws com.stacs.cs5031.p3.server.exception.EntityNotFoundException if booking or attendee not found
     */
    Booking unregisterAttendee(Long bookingId, Long attendeeId);

    /**
     * Gets all bookings for a specific room.
     *
     * @param roomId The room ID
     * @return List of bookings for the room
     */
    List<Booking> getBookingsByRoom(Long roomId);

    /**
     * Gets all bookings created by a specific organiser.
     *
     * @param organiserId The organiser ID
     * @return List of bookings created by the organiser
     */
    List<Booking> getBookingsByOrganiser(Long organiserId);

    /**
     * Gets all bookings an attendee is registered for.
     *
     * @param attendeeId The attendee ID
     * @return List of bookings the attendee is registered for
     */
    List<Booking> getBookingsByAttendee(Long attendeeId);

    /**
     * Checks if there's a booking conflict for a room at a specific time.
     *
     * @param roomId The room ID
     * @param startTime The start time to check
     * @param duration The duration in minutes
     * @return true if there's a conflict, false otherwise
     */
    boolean hasConflict(Long roomId, Date startTime, long duration);

    /**
     * Creates a new booking from a BookingDTO.
     *
     * @param bookingDTO The booking data
     * @param organiserId The ID of the organiser creating the booking
     * @return The created booking
     * @throws com.stacs.cs5031.p3.server.exception.EntityNotFoundException if room or organiser not found
     * @throws com.stacs.cs5031.p3.server.exception.BookingConflictException if there's a booking conflict
     */
    Booking createBooking(BookingDto.BookingRequest bookingDTO, Long organiserId);
}