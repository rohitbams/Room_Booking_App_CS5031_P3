package com.stacs.cs5031.p3.server.dto;

/**
 * UserDto class.
 * This class implements data transfer objects for user entities.
 * It is used for transferring user data to clients without
 * exposing sensitive information.
 */
public class UserDto {
    private final Integer id;
    private final String username;
    private final String name;
    private final String role;

    public UserDto(Integer id, String username, String name, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}