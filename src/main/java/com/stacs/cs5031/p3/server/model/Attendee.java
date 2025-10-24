package com.stacs.cs5031.p3.server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Attendee model class.
 * This is a subclass of User and represents an Attendee in the system.
 * Attendees can register for bookings created by Organisers.
 */
@Entity
public class Attendee extends User {

    @ManyToMany
    @JoinTable(
            name = "attendee_bookings",
            joinColumns = @JoinColumn(name = "attendee_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id")
    )
    private List<Booking> registeredBookings = new ArrayList<>();

    protected Attendee() {
        super();
    }

    /**
     * Constructor for attendee.
     *
     * @param name name of attendee
     * @param username attendee's username
     * @param password attendee's password
     */
    public Attendee(String name, String username, String password) {
        super(name, username, password);
    }

    /**
     * Get the bookings attendee has registered for.
     *
     * * @return List of registered bookings
     */
    public List<Booking> getRegisteredBookings() {
        return registeredBookings;
    }

    /**
     * Register the attendee for a booking.
     * Will not add the booking if the attendee is already registered.
     *
     * @param booking The booking to register for
     */
    public void registerForBooking(Booking booking) {
        if (!registeredBookings.contains(booking)) {
            registeredBookings.add(booking);
        }
    }

    /**
     * Cancel registration from a booking.
     *
     * @param booking The booking to deregister from
     */
    public void deRegisterFromBooking(Booking booking) {
        registeredBookings.remove(booking);
    }

    // creates a new ArrayList containing all elements from the Set
    public void setRegisteredBookings(Set<Booking> registeredBookings) {
        this.registeredBookings = new ArrayList<>(registeredBookings);
    }

//    public void setRegisteredBookings(Set<Booking> registeredBookings) {
//        this.registeredBookings = registeredBookings;
//    }

}