package com.stacs.cs5031.p3.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a booking conflicts with an existing booking.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class BookingConflictException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public BookingConflictException(String message) {
        super(message);
    }
    
    public BookingConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}