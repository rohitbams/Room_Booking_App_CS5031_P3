package com.stacs.cs5031.p3.server.dto;

/**
 * LoginRequest class.
 * This class is a simple request model that
 * represents a user login request and contains
 * username and password for authentication.
 */
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}