package com.stacs.cs5031.p3.server.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotAvailableException;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.service.RoomService;

/**
 * Unit tests for the {@link RoomController} class.
 * Tests the RESTful endpoints provided by the RoomController using MockMvc.
 * The RoomService is mocked to isolate tests from the service layer.
 */
public class RoomControllerTest {

    /** MockMvc instance for testing REST endpoints */
    private MockMvc mvc;

    /** Mock of the RoomService to simulate service behavior */
    @Mock
    private RoomService roomService;

    /** The controller being tested, with mocked dependencies injected */
    @InjectMocks
    private RoomController roomController;

    /**
     * Setup before each test.
     * Initializes mocks and sets up the MockMvc test environment.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    /**
     * Tests that GET /rooms/{id} returns the correct room when a valid ID is provided.
     * Verifies the HTTP status, content type, and response JSON structure.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldGetRoomWithValidId() throws Exception {
        when(roomService.findRoomDtoById(1)).thenReturn(new RoomDto(1, "Room 1", 10, true));

        mvc.perform(get("/rooms/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Room 1")))
                .andExpect(jsonPath("$.capacity", is(10)))
                .andExpect(jsonPath("$.available", is(true)));
    }

    /**
     * Tests that GET /rooms/{id} returns a 404 Not Found status when an invalid ID is provided.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnNotFoundWithInvalidRoomId() throws Exception {
        when(roomService.findRoomDtoById(999)).thenThrow(RoomNotFoundException.class);

        mvc.perform(get("/rooms/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests that GET /rooms/all returns all rooms when rooms exist.
     * Verifies the HTTP status, content type, and response JSON structure.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldGetAllRooms_whenRoomsExist() throws Exception {
        List<RoomDto> expectedRooms = Arrays.asList(
                new RoomDto(1, "Room 1", 10, true),
                new RoomDto(2, "Room 2", 20, true),
                new RoomDto(3, "Room 3", 30, false));
        when(roomService.findAllRooms()).thenReturn(expectedRooms);

        mvc.perform(get("/rooms/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Room 1")))
                .andExpect(jsonPath("$[0].capacity", is(10)))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
    }

    /**
     * Tests that GET /rooms/all returns a 204 No Content status when no rooms exist.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnNoContent_whenNoRoomExists() throws Exception {
        when(roomService.findAllRooms()).thenReturn(List.of()); // return empty list

        mvc.perform(get("/rooms/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that GET /rooms/available returns a 204 No Content status when no available rooms exist.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnNoContent_whenNoRoomIsAvailable() throws Exception {
        when(roomService.findAvailableRooms()).thenReturn(List.of()); // return empty list

        mvc.perform(get("/rooms/available")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that GET /rooms/available returns all available rooms when they exist.
     * Verifies the HTTP status, content type, and response JSON structure.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldGetAvailableRooms_whenAvailableRoomsExist() throws Exception {
        List<RoomDto> expectedRooms = Arrays.asList(
            new RoomDto(1, "Room 1", 10, true),
            new RoomDto(2, "Room 2", 20, true),
            new RoomDto(3, "Room 3", 30, false)
        );
        when(roomService.findAvailableRooms()).thenReturn(expectedRooms);

        mvc.perform(get("/rooms/available")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].available", is(true)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].available", is(false)));
    }

    /**
     * Tests that POST /rooms/{id}/book successfully books a room with valid ID.
     * Verifies the HTTP status, content type, and response JSON structure.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldBookRoomWithValidId() throws Exception {
        when(roomService.bookRoom(1)).thenReturn(new RoomDto(1, "Room 1", 10, true));

        mvc.perform(post("/rooms/1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Room 1")))
                .andExpect(jsonPath("$.capacity", is(10)))
                .andExpect(jsonPath("$.available", is(true)));
    }

    /**
     * Tests that POST /rooms/{id}/book returns a 400 Bad Request status when trying to book an unavailable room.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnBadRequest_whenBookingUnavailableRoom() throws Exception {
        when(roomService.bookRoom(2)).thenThrow(RoomNotAvailableException.class);
        mvc.perform(post("/rooms/2/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that POST /rooms/{id}/book returns a 404 Not Found status when trying to book a room with invalid ID.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnNotFound_whenBookingRoomWithInvalidId() throws Exception {
        when(roomService.bookRoom(999)).thenThrow(RoomNotFoundException.class);
        mvc.perform(post("/rooms/999/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests that POST /rooms/{id}/makeAvailable successfully makes a room available.
     * Verifies the HTTP status is OK.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldMakeRoomAvailable() throws Exception {
        when(roomService.makeRoomAvailable(1)).thenReturn(new RoomDto(1, "Room 1", 10, true));
        mvc.perform(post("/rooms/1/makeAvailable")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Tests that POST /rooms/{id}/makeAvailable returns a 404 Not Found status when trying to make a non-existent room available.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void shouldReturnNotFound_whenMakingRoomAvailableWithInvalidId() throws Exception {
        when(roomService.makeRoomAvailable(999)).thenThrow(RoomNotFoundException.class);
        mvc.perform(post("/rooms/999/makeAvailable")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
