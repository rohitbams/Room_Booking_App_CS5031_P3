package com.stacs.cs5031.p3.server.service;

import com.stacs.cs5031.p3.server.dto.RegistrationRequest;
import com.stacs.cs5031.p3.server.exception.UserAlreadyExistsException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.User;
import com.stacs.cs5031.p3.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UserService} class.
 * Tests the business logic of the UserService using mocked dependencies.
 * Verifies that the service correctly interacts with the UserRepository
 * and handles user registration, authentication, and management operations.
 */
public class UserServiceTest {

    /** Mock of the UserRepository to simulate data access operations */
    @Mock
    private UserRepository userRepository;

    /** The UserService instance being tested, with mocked dependencies injected */
    @InjectMocks
    private UserService userService;

    /** Test user instance used across multiple test methods */
    private User testUser;

    /**
     * Setup before each test.
     * Initializes mocks and creates a sample User object for testing.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("Test User", "testuser", "password");
    }

    /**
     * Tests that a user can be retrieved by ID when the user exists.
     * Verifies that:
     * 1. The returned User matches the expected user
     * 2. The repository's findById method is called with the correct ID
     */
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        User result = userService.getUserById(1);
        assertEquals(testUser, result);
        verify(userRepository).findById(1);
    }

    /**
     * Tests that a UserNotFoundException is thrown when attempting to retrieve a non-existent user by ID.
     * Verifies that:
     * 1. The correct exception is thrown
     * 2. The repository's findById method is called with the correct ID
     */
    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
        verify(userRepository).findById(1);
    }

    /**
     * Tests that a user can be retrieved by username when the user exists.
     * Verifies that:
     * 1. The returned User matches the expected user
     * 2. The repository's findByUsername method is called with the correct username
     */
    @Test
    void getUserByUsername_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        User result = userService.getUserByUsername("testuser");
        assertEquals(testUser, result);
        verify(userRepository).findByUsername("testuser");
    }

    /**
     * Tests that an Organiser is correctly created when registering with the ORGANISER role.
     * Verifies that:
     * 1. The returned User is an instance of Organiser
     * 2. The Organiser has the correct username
     * 3. The repository's findByUsername and save methods are called with the correct parameters
     */
    @Test
    void registerUser_ShouldCreateOrganiser_WhenRoleIsOrganiser() {
        RegistrationRequest request = new RegistrationRequest(
                "Test User", "testuser", "password", "ORGANISER");
        Organiser organiser = new Organiser("Test User", "testuser", "password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(Organiser.class))).thenReturn(organiser);
        User result = userService.registerUser(request);
        assertNotNull(result);
        assertTrue(result instanceof Organiser);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
        verify(userRepository).save(any(Organiser.class));
    }

    /**
     * Tests that an Attendee is correctly created when registering with the ATTENDEE role.
     * Verifies that:
     * 1. The returned User is an instance of Attendee
     * 2. The Attendee has the correct username
     * 3. The repository's findByUsername and save methods are called with the correct parameters
     */
    @Test
    void registerUser_ShouldCreateAttendee_WhenRoleIsAttendee() {
        RegistrationRequest request = new RegistrationRequest(
                "Test User", "testuser", "password", "ATTENDEE");
        Attendee attendee = new Attendee("Test User", "testuser", "password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(Attendee.class))).thenReturn(attendee);
        User result = userService.registerUser(request);
        assertNotNull(result);
        assertTrue(result instanceof Attendee);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
        verify(userRepository).save(any(Attendee.class));
    }

    /**
     * Tests that a UserAlreadyExistsException is thrown when registering with a username that's already taken.
     * Verifies that:
     * 1. The correct exception is thrown
     * 2. The repository's findByUsername method is called with the correct username
     * 3. The repository's save method is never called
     */
    @Test
    void registerUser_ShouldThrowException_WhenUsernameAlreadyTaken() {
        RegistrationRequest request = new RegistrationRequest(
                "Test User", "testuser", "password", "ORGANISER");
        User existingUser = new User("Existing User", "testuser", "password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Tests that isUsernameTaken returns true when the username exists.
     * Verifies that:
     * 1. The method returns true for an existing username
     * 2. The repository's findByUsername method is called with the correct username
     */
    @Test
    void isUsernameTaken_ShouldReturnTrue_WhenUsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        boolean result = userService.isUsernameTaken("testuser");
        assertTrue(result);
        verify(userRepository).findByUsername("testuser");
    }

    /**
     * Tests that isUsernameTaken returns false when the username doesn't exist.
     * Verifies that:
     * 1. The method returns false for a non-existent username
     * 2. The repository's findByUsername method is called with the correct username
     */
    @Test
    void isUsernameTaken_ShouldReturnFalse_WhenUsernameDoesNotExist() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        boolean result = userService.isUsernameTaken("testuser");
        assertFalse(result);
        verify(userRepository).findByUsername("testuser");
    }

    /**
     * Tests that a user can be deleted by ID.
     * Verifies that the repository's deleteById method is called with the correct ID.
     */
    @Test
    void deleteUser_ShouldCallRepositoryDeleteById() {
        doNothing().when(userRepository).deleteById(1);
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }

    /**
     * Tests that all users can be retrieved.
     * Verifies that:
     * 1. The returned list contains the expected number of users
     * 2. The returned list matches the expected list of users
     * 3. The repository's findAll method is called
     */
    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        User newUser= new User("New User", "newuser", "password");
        List<User> userList = Arrays.asList(testUser, newUser);
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals(userList, result);
        verify(userRepository).findAll();
    }
}
// 10