package com.stacs.cs5031.p3.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stacs.cs5031.p3.server.exception.BookingFullException;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.exception.ResourceUnavailableException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.User;
import com.stacs.cs5031.p3.server.repository.AttendeeRepository;
import com.stacs.cs5031.p3.server.repository.BookingRepository;
import com.stacs.cs5031.p3.server.repository.UserRepository;

import jakarta.transaction.Transactional;

/**
 * The AttendeeService class.
 * This class implements attendee-specific operations including
 * transactions, validation, and business logic.
 */
@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AttendeeService(AttendeeRepository attendeeRepository, BookingRepository bookingRepository) {
        this.attendeeRepository = attendeeRepository;
        this.bookingRepository = bookingRepository;
    }

    // /**
    //  * Gets an attendee by ID.
    //  *
    //  * @param id The attendee ID
    //  * @return The attendee
    //  * @throws UserNotFoundException if attendee is not found
    //  */
    // public Attendee getAttendeeById(Integer id) {
    //     return attendeeRepository.findById(id)
    //             .orElseThrow(() -> new UserNotFoundException(id));
    // }

    /**
     * Gets an attendee by username.
     *
     * @param username The attendee username
     * @return The attendee
     * @throws UserNotFoundException if attendee is not found
     */
    public Attendee getAttendeeByUsername(String username) {
        Attendee attendee = attendeeRepository.findByUsername(username);
        if (attendee == null) {
            throw new UserNotFoundException(username);
        }
        return attendee;
    }

    // get all available attendees
    public List<Attendee> getAllAttendees() {
        return attendeeRepository.findAll();
    }

    /**
     * Get all available bookings for an attendee.
     *
     * @param attendeeId The attendee ID
     * @return List of available bookings
     */
    public List<Booking> getAvailableBookings(Integer attendeeId) {
        getAttendeeById(attendeeId);
        return attendeeRepository.findAvailableBookings(attendeeId);
    }

    /**
     * Get all unavailable bookings for an attendee.
     *
     * @param attendeeId The attendee ID
     * @return List of unavailable bookings
     */
    public List<Booking> getUnavailableBookings(Integer attendeeId) {
        getAttendeeById(attendeeId);
        return attendeeRepository.findUnavailableBookings(attendeeId);
    }

    /**
     * Get all bookings an attendee has registered for.
     *
     * @param attendeeId attendee ID
     * @return List of all bookings an attendee has registered for.
     */
    public List<Booking> getRegisteredBookings(Integer attendeeId) {
        getAttendeeById(attendeeId);
        return attendeeRepository.findRegisteredBookings(attendeeId);
    }

    /**
     * Register an attendee for a booking.
     *
     * @param attendeeId The attendee ID
     * @param bookingId The booking ID
     * @return The updated booking
     * @throws UserNotFoundException if attendee is not found
     * @throws BookingNotFoundException if booking is not found
     * @throws BookingFullException if booking is already at capacity
     */

    @Transactional
    public Booking registerForBooking(Integer attendeeId, long bookingId) {
        Attendee attendee = getAttendeeById(attendeeId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (attendee.getRegisteredBookings().contains(booking)) {
            throw new IllegalStateException("Attendee is already registered for this booking");
        }

        if (booking.getAttendees().size() >= booking.getRoom().getCapacity()) {
            throw new ResourceUnavailableException("Booking is at full capacity");
        }

        attendee.getRegisteredBookings().add(booking);
        booking.getAttendees().add(attendee);

        attendeeRepository.save(attendee);
        return bookingRepository.save(booking);
    }

    /**
     * De-register an attendee from an existing registered event.
     *
     * @param attendeeId attendee ID
     * @param bookingId booking ID
     * @return updated booking
     * @throws IllegalStateException if attendee is not registered on the event
     */
    @Transactional
    public Booking deregisterFromBooking(Integer attendeeId, long bookingId) {
        Attendee attendee = getAttendeeById(attendeeId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (!attendee.getRegisteredBookings().contains(booking)) {
            throw new IllegalStateException("Attendee is not registered for this booking");
        }

//        attendee.getRegisteredBookings().remove(booking);
//        booking.getAttendees().remove(attendee);
        attendee.deRegisterFromBooking(booking);
        booking.removeAttendee(attendee);

        attendeeRepository.save(attendee);
        return bookingRepository.save(booking);
    }


    public Attendee getAttendeeById(Integer id) {
        Attendee attendee = attendeeRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
//        if (!(attendee instanceof Attendee)) {
            if (attendee == null) {
            throw new IllegalArgumentException("User with ID " + id + " is not an Attendee.");
        }
        return (Attendee) attendee;
    }
}
