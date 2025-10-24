package com.stacs.cs5031.p3.server.exception;

/**
 * UserAlreadyFoundException.
 * This exception is thrown when a new user is being registered with
 * a username that is already taken.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Sorry, user: " + username + " already exists");
    }
}
