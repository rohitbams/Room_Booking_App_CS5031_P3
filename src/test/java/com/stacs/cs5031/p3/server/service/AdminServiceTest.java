package com.stacs.cs5031.p3.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.AdminRepository;

/**
 * Unit tests for the {@link AdminService} class.
 * Tests the business logic of the AdminService using mocked dependencies.
 * Verifies that the service correctly interacts with its dependencies and handles data properly.
 */
public class AdminServiceTest {
    
    /** The AdminService instance being tested, with mocked dependencies injected */
    @InjectMocks
    private AdminService adminService;

    /** Mock of the RoomService to simulate room-related operations */
    @Mock
    private RoomService roomService;

    /** Mock of the OrganiserService to simulate organiser-related operations */
    @Mock
    private OrganiserService organiserService;

    /** Mock of the AttendeeService to simulate attendee-related operations */
    @Mock
    private AttendeeService attendeeService;

    /** Mock of the AdminRepository to simulate data access operations */
    @Mock
    private AdminRepository adminRepository;

    /**
     * Setup before each test.
     * Initializes mocks and prepares the test environment.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that getAllRooms returns all rooms from the RoomService.
     * Verifies that:
     * 1. The correct number of rooms is returned
     * 2. The room properties match the expected values
     * 3. The RoomService's findAllRooms method is called exactly once
     */
    @Test
    void shouldGetAllRooms() {
        List<RoomDto> allRooms = List.of(
            new RoomDto(1, "Room 1", 10, true),
            new RoomDto(2, "Room 2", 10, true),
            new RoomDto(3, "Room 3", 10, true)
        );

        when(roomService.findAllRooms()).thenReturn(allRooms);
        ArrayList<RoomDto> result = adminService.getAllRooms();
        assertEquals(allRooms.size(), result.size());

        // Verify the contents of the returned list
        for (int i = 0; i < allRooms.size(); i++) {
            RoomDto expected = allRooms.get(i);
            RoomDto actual = result.get(i);

            assertEquals(expected.getName(), actual.getName(), "Room name should match");
            assertEquals(expected.getCapacity(), actual.getCapacity(), "Room capacity should match");
            assertEquals(expected.isAvailable(), actual.isAvailable(), "Room availability should match");
        }

        // Verify that roomService.findAllRooms() was called exactly once
        verify(roomService, times(1)).findAllRooms();
    }

    /**
     * Tests that getAttendees returns all attendees from the AttendeeService.
     * Verifies that:
     * 1. The correct number of attendees is returned
     * 2. The attendee properties match the expected values
     * 3. The AttendeeService's getAllAttendees method is called exactly once
     */
    @Test
    void shouldGetAttendees() {
        ArrayList<Attendee> attendees = new ArrayList<>();
        attendees.add(new Attendee("Ada", "ada", "12345"));
        attendees.add(new Attendee("G Yeung", "g.yeung", "12345"));
        attendees.add(new Attendee("Toothless", "toothless", "12345"));

        when(attendeeService.getAllAttendees()).thenReturn(attendees);
        ArrayList<Attendee> result = adminService.getAttendees();
        assertEquals(attendees.size(), result.size());

        // Verify the contents of the returned list
        for (int i = 0; i < attendees.size(); i++) {
            Attendee expected = attendees.get(i);
            Attendee actual = result.get(i);

            assertEquals(expected.getName(), actual.getName(), "Attendee name should match");
            assertEquals(expected.getUsername(), actual.getUsername(), "Attendee username should match");
            assertEquals(expected.getPassword(), actual.getPassword(), "Attendee password should match");
        }

        // Verify that AttendeeService.getAllAttendees() was called exactly once
        verify(attendeeService, times(1)).getAllAttendees();
    }

    /**
     * Tests that getOrganisers returns all organisers from the OrganiserService.
     * Verifies that:
     * 1. The correct number of organisers is returned
     * 2. The organiser properties match the expected values
     * 3. The OrganiserService's getAllOrganisers method is called exactly once
     */
    @Test
    void shouldGetOrganisers() {
        ArrayList<OrganiserDto> organisers = new ArrayList<>();
        organisers.add(new OrganiserDto(1, "john.doe", "12345"));
        organisers.add(new OrganiserDto(2, "lily.wong", "12345"));
        organisers.add(new OrganiserDto(3, "toothless", "12345"));

        when(organiserService.getAllOrganisers()).thenReturn(organisers);
        ArrayList<OrganiserDto> result = adminService.getOrganisers();
        assertEquals(organisers.size(), result.size());

        // Verify the contents of the returned list
        for (int i = 0; i < organisers.size(); i++) {
            OrganiserDto expected = organisers.get(i);
            OrganiserDto actual = result.get(i);

            assertEquals(expected.getName(), actual.getName(), "Organiser name should match");
            assertEquals(expected.getUsername(), actual.getUsername(), "Organiser username should match");
            assertEquals(expected.getId(), actual.getId(), "Organiser id should match");
        }

        // Verify that OrganiserService.getAllOrganisers() was called exactly once
        verify(organiserService, times(1)).getAllOrganisers();
    }

    /**
     * Tests that addRoom returns true when adding a room with valid capacity.
     * Verifies that the method correctly delegates to RoomService and handles successful room creation.
     */
    @Test
    void shouldReturnTrue_whenAddRoomWithCapacityHigherThanZero() {
        Room testRoom = new Room("Test Room", 10);
        when(roomService.createRoomDto(testRoom.getName(), testRoom.getCapacity())).thenReturn(new RoomDto(1, "Test Room", 10, true));
        assertEquals(true, adminService.addRoom(testRoom));
    }

    /**
     * Tests that addRoom returns false when adding a room with zero capacity.
     * Verifies that the method correctly handles the IllegalArgumentException thrown by RoomService.
     */
    @Test
    void shouldReturnFalse_whenAddRoomWithZeroCapacity() {
        Room testRoom = new Room("Test Room", 0);
        when(roomService.createRoom(testRoom.getName(), testRoom.getCapacity()))
                .thenThrow(IllegalArgumentException.class);
        assertEquals(false, adminService.addRoom(testRoom));
    }

    /**
     * Tests that addRoom returns false when adding a room with negative capacity.
     * Verifies that the method correctly handles the IllegalArgumentException thrown by RoomService.
     */
    @Test
    void shouldReturnFalse_whenAddRoomWithNegativeCapacity() {
        Room testRoom = new Room("Test Room", -1);
        when(roomService.createRoom(testRoom.getName(), testRoom.getCapacity())).thenThrow(IllegalArgumentException.class);
        assertEquals(false, adminService.addRoom(testRoom));
    }

    /**
     * Tests that removeRoom returns true when removing a room with a valid ID.
     * Verifies that the method correctly delegates to RoomService and handles successful room deletion.
     */
    @Test
    void shouldReturnTrue_whenRemoveRoomWithValidRoomId() {
        int validRoomId = 1;
        doNothing().when(roomService).deleteRoomById(validRoomId);
        assertEquals(true, adminService.removeRoom(validRoomId));
    }

    /**
     * Tests that removeRoom returns false when attempting to remove a room with an invalid ID.
     * Verifies that the method correctly handles the RoomNotFoundException thrown by RoomService.
     */
    @Test
    void shouldReturnFalse_whenRemoveRoomWithInvalidRoomId() {
        int inValidRoomId = 999;
        doThrow(RoomNotFoundException.class).when(roomService).deleteRoomById(inValidRoomId);
        assertEquals(false, adminService.removeRoom(inValidRoomId));
    }
}
// 8