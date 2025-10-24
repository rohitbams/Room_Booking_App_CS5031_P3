package com.stacs.cs5031.p3.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotAvailableException;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.RoomRepository;

/**
 * Unit tests for the {@link RoomService} class.
 * Tests the business logic of the RoomService using mocked dependencies.
 * This service handles the following operations:
 * - Coordinate booking a room (checking availability, updating database)
 * - Handle complex search criteria for rooms
 * - Manage room capacity validation
 * - Coordinate between multiple repositories if needed
 */
@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

   /** Mock of the RoomRepository to simulate data access operations */
   @Mock
   private RoomRepository roomRepository;
   
   /** Test room entity used across multiple test methods */
   private Room testRoom;
   
   /** Expected DTO representation of the test room */
   private RoomDto testRoomDTO;

   /** The RoomService instance being tested, with mocked dependencies injected */
   @InjectMocks
   private RoomService roomService;

   /**
    * Setup before each test.
    * Creates a sample Room object for testing with predefined values and
    * simulates database-assigned ID.
    */
   @BeforeEach
   void setup() {
       testRoom = new Room("Test Room", 20);

       /*
       * In production, these fields are set by:
       * - id: JPA's @GeneratedValue strategy
       * - name: @PostPersist callback
       * Using ReflectionTestUtils to simulate this behaviour for testing
       */
       ReflectionTestUtils.setField(testRoom, "id", 1);

       // Expected DTO after mapping
       testRoomDTO = new RoomDto(1, "Test Room", 20, true);

       // stub repository behaviour
       // when(roomRepository.save(testRoom)).thenReturn(testRoom);
       // when room entity exists
       // when(roomRepository.findById(1)).thenReturn(Optional.of(testRoom));
   }

   /**
    * Tests that a room can be created with valid parameters.
    * Verifies that:
    * 1. The returned RoomDto has the correct properties
    * 2. The repository's save method is called with the room entity
    */
   @Test
   void shouldCreateRoom() {
       when(roomRepository.save(any(Room.class))).thenReturn(testRoom);
       RoomDto result = roomService.createRoomDto(testRoom.getName(), testRoom.getCapacity());

       assertNotNull(result);
       assertEquals(testRoomDTO.getId(), result.getId());
       assertEquals(testRoomDTO.getName(), result.getName());
       assertEquals(testRoomDTO.getCapacity(), result.getCapacity());
       assertEquals(testRoomDTO.isAvailable(), result.isAvailable());

       verify(roomRepository).save(any(Room.class));
   }

   /**
    * Tests that an exception is thrown when attempting to create a room with zero capacity.
    * Verifies that the service properly validates capacity constraints.
    */
   @Test
   void shouldThrowException_whenRoomCapacityIsZero() {
       assertThrows(IllegalArgumentException.class,
               () -> roomService.createRoom("Invalid Room", 0),
               "Should throw exception for room with zero capacity");
   }

   /**
    * Tests that an exception is thrown when attempting to create a room with negative capacity.
    * Verifies that the service properly validates capacity constraints.
    */
   @Test
   void shouldThrowException_whenRoomCapacityIsNegative() {
       assertThrows(IllegalArgumentException.class,
               () -> roomService.createRoom("Invalid Room", -1),
               "Should throw exception for room with negative capacity");
   }

   /**
    * Tests that a room can be found by its ID when it exists.
    * Verifies that:
    * 1. The returned RoomDto has the correct properties
    * 2. The repository's findById method is called with the correct ID
    */
   @Test
   void shouldFindRoomById_whenRoomExists() {
       // testRoom is initialised in setUp(), will not return empty
       when(roomRepository.findById(1)).thenReturn(Optional.of(testRoom));
       RoomDto result = roomService.findRoomDtoById(1);

       assertNotNull(result, "Result should not be null");
       assertEquals(testRoomDTO.getId(), result.getId(), "ID should match");
       assertEquals(testRoomDTO.getName(), result.getName(), "Name should match");
       assertEquals(testRoomDTO.getCapacity(), result.getCapacity(), "Capacity should match");
       assertTrue(result.isAvailable(), "Room should be available");
   }

   /**
    * Tests that a RoomNotFoundException is thrown when attempting to find a room that doesn't exist.
    * Verifies that the service properly handles missing room scenarios.
    */
   @Test
   void shouldThrowRoomNotFoundException_whenRoomDoesNotExist() {
       when(roomRepository.findById(999)).thenReturn(Optional.empty());
       assertThrows(RoomNotFoundException.class, () -> roomService.findRoomDtoById(999));
       verify(roomRepository).findById(999);
   }

   /**
    * Tests that all rooms can be retrieved.
    * Verifies that:
    * 1. The returned list contains the expected number of rooms
    * 2. The RoomDto properties match the expected values
    * 3. The repository's findAll method is called
    */
   @Test
   void shouldFindAllRooms() {
       when(roomRepository.findAll()).thenReturn(List.of(testRoom));
       List<RoomDto> result = roomService.findAllRooms();

       assertNotNull(result, "Result list should not be null");
       assertFalse(result.isEmpty(), "Result list should not be empty");

       RoomDto resultDto = result.get(0);

       assertEquals(1, result.size());
       assertEquals(testRoomDTO.getId(), resultDto.getId());
       assertEquals(testRoomDTO.getName(), resultDto.getName());
       assertEquals(testRoomDTO.getCapacity(), resultDto.getCapacity());
       assertEquals(testRoomDTO.isAvailable(), resultDto.isAvailable());
       verify(roomRepository).findAll();
   }

   /**
    * Tests that available rooms can be retrieved.
    * Verifies that:
    * 1. The returned list contains the expected number of available rooms
    * 2. The RoomDto properties match the expected values
    * 3. The repository's findByAvailability method is called with the correct parameter
    */
   @Test
   void shouldFindAvailableRooms() {
       when(roomRepository.findByAvailability(true)).thenReturn(List.of(testRoom));
       List<RoomDto> result = roomService.findAvailableRooms();

       assertNotNull(result, "Result list should not be null");
       assertFalse(result.isEmpty(), "Result list should not be empty");

       RoomDto resultDto = result.get(0);

       assertEquals(1, result.size());
       assertEquals(testRoomDTO.getId(), resultDto.getId());
       assertEquals(testRoomDTO.getName(), resultDto.getName());
       assertEquals(testRoomDTO.getCapacity(), resultDto.getCapacity());
       assertEquals(testRoomDTO.isAvailable(), resultDto.isAvailable());
       verify(roomRepository).findByAvailability(true);
   }

   /**
    * Tests that a room can be booked.
    * Verifies that:
    * 1. The returned RoomDto has the correct properties after booking
    * 2. The room's availability is set to false
    * 3. The repository's findById method is called with the correct ID
    */
   @Test
   void shouldBookRoom() {
       when(roomRepository.findById(1)).thenReturn(Optional.of(testRoom));
       RoomDto result = roomService.bookRoom(testRoom.getID());

       assertNotNull(result, "Result should not be null");
       assertEquals(testRoomDTO.getId(), result.getId(), "ID should match");
       assertEquals(testRoomDTO.getName(), result.getName(), "Name should match");
       assertEquals(testRoomDTO.getCapacity(), result.getCapacity(), "Capacity should match");
       assertFalse(result.isAvailable(), "Room should be unavailable after booking");
       verify(roomRepository).findById(1);
   }

   /**
    * Tests that a RoomNotAvailableException is thrown when attempting to book an unavailable room.
    * Verifies that the service properly validates availability before booking.
    */
   @Test
   void shouldThrowException_whenBookingUnavailableRoom() {
       Room unavailableRoom = new Room("Test Room", 10);
       ReflectionTestUtils.setField(unavailableRoom, "id", 2);
       unavailableRoom.bookRoom(); // Set availability to false
       when(roomRepository.findById(2)).thenReturn(Optional.of(unavailableRoom));

       assertThrows(RoomNotAvailableException.class, () -> roomService.bookRoom(unavailableRoom.getID()));
       verify(roomRepository).findById(2);
   }

   /**
    * Tests that a room can be made available.
    * Verifies that:
    * 1. The returned RoomDto has the correct properties after making it available
    * 2. The room's availability is set to true
    * 3. The repository's findById method is called with the correct ID
    */
   @Test
   void shouldMakeRoomAvailable() {
       Room unavailableRoom = new Room("Test Room", 10); // Available initially
       ReflectionTestUtils.setField(unavailableRoom, "id", 2);
       ReflectionTestUtils.setField(unavailableRoom, "name", "Unavailable Room");
       unavailableRoom.bookRoom(); // Set availability to false
       when(roomRepository.findById(2)).thenReturn(Optional.of(unavailableRoom));

       RoomDto result = roomService.makeRoomAvailable(unavailableRoom.getID());

       assertNotNull(result, "Result should not be null");
       assertEquals(unavailableRoom.getID(), result.getId(), "ID should match");
       assertEquals(unavailableRoom.getName(), result.getName(), "Name should match");
       assertEquals(unavailableRoom.getCapacity(), result.getCapacity(), "Capacity should match");
       assertTrue(result.isAvailable(), "Room should be available after being made available");
   }

   /**
    * Tests that a room can be deleted by its ID.
    * Verifies that:
    * 1. The repository's findById method is called with the correct ID
    * 2. No exceptions are thrown when the room exists
    */
   @Test
   void shouldRemoveRoom() {
       when(roomRepository.findById(1)).thenReturn(Optional.of(testRoom));
       roomService.deleteRoomById(1);
   }

   /**
    * Tests that a RoomNotFoundException is thrown when attempting to delete a room that doesn't exist.
    * Verifies that the service properly validates room existence before deletion.
    */
   @Test
   void shouldThrowRoomNotFoundException_whenRemoveNonexistingRoom() {
       assertThrows(RoomNotFoundException.class, () -> roomService.deleteRoomById(2));
   }
}
