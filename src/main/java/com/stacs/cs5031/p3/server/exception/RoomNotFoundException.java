package com.stacs.cs5031.p3.server.exception;

/**
 * Exception thrown when a requested room cannot be found in the system.
 * This is a runtime exception as room not found issues are considered to be
 * non-recoverable from the application's perspective and should be handled appropriately.
 */
public class RoomNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message The detail message explaining the reason for the exception
     */
    public RoomNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message The detail message explaining the reason for the exception
     * @param cause The cause of the exception (a throwable that caused this exception to be thrown)
     */
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with a detail message indicating which room ID
     * could not be found.
     * 
     * @param roomId The ID of the room that could not be found
     */
    public RoomNotFoundException(int roomId) {
        super("Room not found with ID: " + roomId);
    }
}
