package com.stacs.cs5031.p3.server.exception;

/**
 * BookingNotFoundException class.
 * This exception is thrown when a booking with a given ID does not exist.
 */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(int id) {
        super("Booking with id: " + id + " does not exist");
    }
    public BookingNotFoundException(long id) {
        super("Booking with id: " + id + " does not exist");
    }
}
