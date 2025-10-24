package com.stacs.cs5031.p3.server.exception;

/**
 * This exception is thrown when a user with a given ID or username does not exist.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super("User with id: " + id + " does not exist");
    }
    public UserNotFoundException(String username) {
        super("User with username: " + username + " does not exist");
    }
}
