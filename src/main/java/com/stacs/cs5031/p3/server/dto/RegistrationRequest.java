package com.stacs.cs5031.p3.server.dto;

/**
 * RegistrationRequest class.
 * This class represents a user registration request and contains
 * information required for creating a new user.
 */
public class RegistrationRequest {
    private String name;
    private String username;
    private String password;
    private String role; // "ORGANISER" or "ATTENDEE"

    protected RegistrationRequest() {
    }

    public RegistrationRequest(String name, String username, String password, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
