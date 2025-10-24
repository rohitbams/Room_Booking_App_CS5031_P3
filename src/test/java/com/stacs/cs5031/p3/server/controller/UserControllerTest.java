package com.stacs.cs5031.p3.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stacs.cs5031.p3.server.dto.LoginRequest;
import com.stacs.cs5031.p3.server.dto.RegistrationRequest;
import com.stacs.cs5031.p3.server.exception.UserAlreadyExistsException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.User;
import com.stacs.cs5031.p3.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the {@link UserController} class.
 * Tests the REST endpoints of the UserController using MockMvc to simulate HTTP requests.
 * Verifies that the controller correctly interacts with the UserService and returns
 * appropriate HTTP status codes and responses.
 */
public class UserControllerTest {

    /** MockMvc instance for simulating HTTP requests */
    private MockMvc mockMvc;

    /** Mock of the UserService to simulate service operations */
    @Mock
    private UserService userService;

    /** The UserController instance being tested, with mocked dependencies injected */
    @InjectMocks
    private UserController userController;

    /** Test user instance used across multiple test methods */
    private User testUser;
    
    /** ObjectMapper for converting objects to JSON and vice versa */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Setup before each test.
     * Initializes mocks, creates the MockMvc instance, and sets up a test user.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        testUser = new User("John Doe", "johndoe", "password123");
    }

    /**
     * Tests registering an organiser with a username that isn't already taken.
     * Verifies that:
     * 1. The endpoint returns a 201 Created status
     * 2. The service's registerUser method is called with the registration request
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldRegisterOrganiser_whenUsernameNotTaken() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "New User", "newuser", "password", "ORGANISER");
        Organiser organiser = new Organiser("New User", "newuser", "password");
        when(userService.registerUser(any(RegistrationRequest.class)))
                .thenReturn(organiser);
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(userService).registerUser(any(RegistrationRequest.class));
    }

    /**
     * Tests registering an attendee with a username that isn't already taken.
     * Verifies that:
     * 1. The endpoint returns a 201 Created status
     * 2. The service's registerUser method is called with the registration request
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldRegisterAttendee_whenUsernameNotTaken() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "New User", "newuser", "password", "ATTENDEE");
        Attendee attendee = new Attendee("New User", "newuser", "password");
        when(userService.registerUser(any(RegistrationRequest.class))).thenReturn(attendee);
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(userService).registerUser(any(RegistrationRequest.class));
    }

    /**
     * Tests retrieving all users.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The service's getAllUsers method is called
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnAllUsers() throws Exception {
        User user2 = new User("Jane Doe", "janedoe", "password456");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser, user2));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        verify(userService).getAllUsers();
    }

    /**
     * Tests retrieving a user by ID when the user exists.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The service's getUserById method is called with the correct ID
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnUserById_whenUserExists() throws Exception {
        when(userService.getUserById(1)).thenReturn(testUser);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
        verify(userService).getUserById(1);
    }

    /**
     * Tests retrieving a user by ID when the user doesn't exist.
     * Verifies that:
     * 1. The endpoint returns a 404 Not Found status
     * 2. The service's getUserById method is called with the correct ID
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnNotFound_whenUserIdDoesNotExist() throws Exception {
        when(userService.getUserById(1000)).thenThrow(new UserNotFoundException(1000));
        mockMvc.perform(get("/users/1000"))
                .andExpect(status().isNotFound());
        verify(userService).getUserById(1000);
    }

    /**
     * Tests retrieving a user by username when the user exists.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The service's getUserByUsername method is called with the correct username
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnUserByUsername_whenUserExists() throws Exception {
        when(userService.getUserByUsername("johndoe")).thenReturn(testUser);
        mockMvc.perform(get("/users/by-username/johndoe"))
                .andExpect(status().isOk());
        verify(userService).getUserByUsername("johndoe");
    }

    /**
     * Tests retrieving a user by username when the username doesn't exist.
     * Verifies that:
     * 1. The endpoint returns a 404 Not Found status
     * 2. The service's getUserByUsername method is called with the correct username
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnNotFound_whenUsernameDoesNotExist() throws Exception {
        when(userService.getUserByUsername("unknown")).thenThrow(new UserNotFoundException("unknown"));
        mockMvc.perform(get("/users/by-username/unknown"))
                .andExpect(status().isNotFound());
        verify(userService).getUserByUsername("unknown");
    }

    /**
     * Tests deleting a user when the user exists.
     * Verifies that:
     * 1. The endpoint returns a 204 No Content status
     * 2. The service's getUserById method is called with the correct ID
     * 3. The service's deleteUser method is called with the correct ID
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldDeleteUser_whenUserExists() throws Exception {
        when(userService.getUserById(1)).thenReturn(testUser);
        doNothing().when(userService).deleteUser(1);
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
        verify(userService).getUserById(1);
        verify(userService).deleteUser(1);
    }

    /**
     * Tests deleting a user when the user doesn't exist.
     * Verifies that:
     * 1. The endpoint returns a 404 Not Found status
     * 2. The service's getUserById method is called with the correct ID
     * 3. The service's deleteUser method is never called
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnNotFound_whenDeletingNonExistentUser() throws Exception {
        when(userService.getUserById(1000)).thenThrow(new UserNotFoundException(1000));
        mockMvc.perform(delete("/users/1000"))
                .andExpect(status().isNotFound());
        verify(userService).getUserById(1000);
        verify(userService, never()).deleteUser(1000);
    }

    /**
     * Tests registering a user with a username that is already taken.
     * Verifies that:
     * 1. The endpoint returns a 409 Conflict status
     * 2. The service's registerUser method is called with the registration request
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnConflict_whenUsernameAlreadyTaken() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "John Doe", "johndoe", "password", "ORGANISER");
        when(userService.registerUser(any(RegistrationRequest.class)))
                .thenThrow(new UserAlreadyExistsException("johndoe"));
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
        verify(userService).registerUser(any(RegistrationRequest.class));
    }

    /**
     * Tests login with valid credentials.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The service's getUserByUsername method is called with the correct username
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldLoginSuccessfully_withValidCredentials() throws Exception {
        when(userService.getUserByUsername("johndoe")).thenReturn(testUser);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("password123");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
        verify(userService).getUserByUsername("johndoe");
    }

    /**
     * Tests login with invalid password.
     * Verifies that:
     * 1. The endpoint returns a 401 Unauthorized status
     * 2. The service's getUserByUsername method is called with the correct username
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnUnauthorized_withInvalidPassword() throws Exception {
        when(userService.getUserByUsername("johndoe")).thenReturn(testUser);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("wrongpassword");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
        verify(userService).getUserByUsername("johndoe");
    }

    /**
     * Tests login with a non-existent username.
     * Verifies that:
     * 1. The endpoint returns a 401 Unauthorized status
     * 2. The service's getUserByUsername method is called with the correct username
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldReturnUnauthorized_withNonExistentUsername() throws Exception {
        when(userService.getUserByUsername("unknown")).thenThrow(new UserNotFoundException("unknown"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("unknown");
        loginRequest.setPassword("anypassword");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
        verify(userService).getUserByUsername("unknown");
    }

    /**
     * Tests logout functionality.
     * Verifies that:
     * 1. The endpoint returns a 200 OK status
     * 2. The response body contains the expected message
     * 
     * @throws Exception if an error occurs during the mock HTTP request
     */
    @Test
    void shouldLogoutSuccessfully() throws Exception {
        mockMvc.perform(post("/users/logout")
                        .header("Authorization", "some-token-value"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }
}
// 14