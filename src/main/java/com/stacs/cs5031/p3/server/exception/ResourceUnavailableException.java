package com.stacs.cs5031.p3.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not available.
 * Used when a room is at capacity or already booked.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceUnavailableException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ResourceUnavailableException(String message) {
        super(message);
    }
    
    public ResourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}