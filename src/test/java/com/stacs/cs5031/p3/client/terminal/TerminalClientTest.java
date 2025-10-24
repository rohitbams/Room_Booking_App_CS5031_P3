package com.stacs.cs5031.p3.client.terminal;

import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TerminalClientTest {

    @Mock
    private RestTemplate mockRestTemplate;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private AutoCloseable closeable;

    private String password;
    private String username;
    private String name;
    private String role;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream));
        TerminalClient.setRestTemplate(mockRestTemplate);
        TerminalClient.setRunning(false);
        name = "Freddie Mercury";
        username = "readyfreddie";
        password = "queen";
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        TerminalClient.setCurrentUser(null);
        closeable.close();
    }

    @Test
    public void testHandleLogin_Success() {
        UserDto mockUser = new UserDto(1, "readyfreddie", name, "ATTENDEE");

        when(mockRestTemplate.postForObject(
                contains("/users/login"),
                any(HttpEntity.class),
                eq(UserDto.class)))
                .thenReturn(mockUser);

        boolean result = TerminalClient.handleLogin(username, password);

        assertTrue(result);
        UserDto currentUser = TerminalClient.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("readyfreddie", currentUser.getUsername());
        assertEquals("ATTENDEE", currentUser.getRole()); // Fixed - was comparing role to username
    }

    @Test
    public void testHandleLogin_Failure() {
        when(mockRestTemplate.postForObject(
                contains("/users/login"),
                any(HttpEntity.class),
                eq(Map.class)))
                .thenReturn(null);

        boolean result = TerminalClient.handleLogin(username, password);
        assertFalse(result);
        assertNull(TerminalClient.getCurrentUser());
    }

    @Test
    public void testHandleRegistration_Success() {
        role = "ORGANISER";
        UserDto mockUser = new UserDto(1, username, name, role);
        when(mockRestTemplate.postForObject(
                contains("/users/register"),
                any(HttpEntity.class),
                eq(UserDto.class)))
                .thenReturn(mockUser);

        boolean result = TerminalClient.handleRegistration(name, username, password, role);
        assertTrue(result);
    }

    @Test
    public void testGetAvailableRooms_Success() {
        RoomDto[] rooms = new RoomDto[2];
        rooms[0] = new RoomDto(1, "Room 101", 20, true);
        rooms[1] = new RoomDto(2, "Room 102", 15, true);

        when(mockRestTemplate.getForObject(
                contains("/rooms/available"),
                eq(RoomDto[].class)))
                .thenReturn(rooms);

        List<RoomDto> result = TerminalClient.getAvailableRooms();
        assertEquals(2, result.size());
        assertEquals("Room 101", result.get(0).getName());
        assertEquals("Room 102", result.get(1).getName());
    }

}