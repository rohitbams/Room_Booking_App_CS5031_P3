package com.stacs.cs5031.p3.server.exception;

/**
 * The BookingFullException class.
 * This exception is thrown when the Booking capacity is already full.
 */
public class BookingFullException extends RuntimeException{
        public BookingFullException(int id) {
            super("Booking with id: " + id + " is already at full capacity");
        }

        public BookingFullException(long id) {
        super("Booking with id: " + id + " is already at full capacity");
    }
}
