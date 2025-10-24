package com.stacs.cs5031.p3.server.exception;

/**
 * Exception thrown when a room that is not available is attempted to be booked.
 * This is a runtime exception as room availability issues are considered to be
 * non-recoverable from the application's perspective and should be handled appropriately.
 */
public class RoomNotAvailableException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message The detail message explaining the reason for the exception
     */
    public RoomNotAvailableException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a detail message indicating which room ID
     * was not available for booking.
     * 
     * @param roomId The ID of the room that was not available
     */
    public RoomNotAvailableException(int roomId) {
        super("Room " + roomId + " is not available for booking");
    }
}
