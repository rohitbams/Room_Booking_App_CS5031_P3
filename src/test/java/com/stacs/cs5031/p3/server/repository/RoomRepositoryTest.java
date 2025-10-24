package com.stacs.cs5031.p3.server.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.stacs.cs5031.p3.server.model.Room;

/**
 * Integration tests for the {@link RoomRepository} interface.
 * Tests CRUD operations and custom query methods on Room entities using an in-memory database.
 * The @DataJpaTest annotation configures an in-memory database and JPA repositories for testing.
 */
@DataJpaTest
public class RoomRepositoryTest {
   /** The RoomRepository instance being tested, automatically injected by Spring */
   @Autowired
   private RoomRepository roomRepository;
   
   /** Test room instances used across multiple test methods */
   private Room room1;
   private Room room2;
   private Room room3;

   /**
    * Setup before each test.
    * Creates and saves sample Room objects to the repository for testing.
    */
   @BeforeEach
   public void setUp() {
       room1 = new Room("Room 1", 4);
       room2 = new Room("Room 2", 6);
       room3 = new Room("Room 3", 8);
       roomRepository.saveAll(Arrays.asList(room1, room2, room3));
   }

   /**
    * Cleanup after each test.
    * Deletes all Room entities from the repository to ensure test isolation.
    */
   @AfterEach
   public void tearDown() {
       // Release test data after each test method
       roomRepository.deleteAll();
   }

   /**
    * Tests that rooms can be saved to the repository and found.
    * Verifies that all saved rooms exist in the repository.
    */
   @Test
   void shouldSaveAndFindRooms() {
       // room1, room2, room3 are already saved in setUp()
       List<Room> foundRooms = StreamSupport.stream(roomRepository.findAll().spliterator(), false)
       .collect(Collectors.toList());

       assertTrue(foundRooms.contains(room1), "room1 should be saved");
       assertTrue(foundRooms.contains(room2), "room2 should be saved");
       assertTrue(foundRooms.contains(room3), "room3 should be saved");
   }

   /**
    * Tests that the repository returns the correct count when a single room is saved.
    * Verifies that after clearing the repository and saving one room, the count is 1.
    */
   @Test
   void shouldReturnCorrectRoomCount_whenOneRoomIsSaved() {
       roomRepository.deleteAll(); // clear repository
       roomRepository.save(new Room("New Room", 3));
       long roomCount = StreamSupport.stream(roomRepository.findAll().spliterator(), false).count();
       assertEquals(1, roomCount);
   }

   /**
    * Tests that the repository returns the correct count when multiple rooms are saved.
    * Verifies counts for both the initial setup (3 rooms) and after clearing and adding 2 new rooms.
    */
   @Test
   void shouldReturnCorrectRoomCount_whenMultipleRoomsAreSaved() {
       // three rooms are saved in setUp()
       long roomCount = StreamSupport.stream(roomRepository.findAll().spliterator(), false).count();
       assertEquals(3, roomCount);

       roomRepository.deleteAll(); // Clear up repository
       // save two rooms
       roomRepository.save(new Room("Room 4", 5));
       roomRepository.save(new Room("Room 5", 5));
       roomCount = StreamSupport.stream(roomRepository.findAll().spliterator(), false).count();
       assertEquals(2, roomCount);
   }

   /**
    * Tests that a room can be found by its ID.
    * Verifies that the found room has the same properties as the saved room.
    */
   @Test
   void shouldFindRoomById() {
       Room foundRoom = roomRepository.findById(room1.getID()).orElse(null);
       assertNotNull(foundRoom);
       assertEquals(room1.getName(), foundRoom.getName());
       assertEquals(room1.getCapacity(), foundRoom.getCapacity());
       assertEquals(room1.isAvailable(), foundRoom.isAvailable());
   }

   /**
    * Tests the custom findByAvailability method with available rooms.
    * Verifies that all rooms are found when searching for available rooms (default state).
    */
   @Test
   void shouldFindAvailableRooms() {
       List<Room> availableRooms = roomRepository.findByAvailability(true);

       // all rooms saved in setup() are available by default
       assertTrue(availableRooms.contains(room1));
       assertTrue(availableRooms.contains(room2));
       assertTrue(availableRooms.contains(room3));
       assertEquals(3, availableRooms.size());
   }

   /**
    * Tests the custom findByAvailability method with unavailable rooms.
    * Verifies that no rooms are found when searching for unavailable rooms (since all are available by default).
    */
   @Test
   void shouldFindUnavailableRooms() {
       List<Room> unAvailableRooms = roomRepository.findByAvailability(false);

       // all rooms saved in setup() are available
       assertEquals(0, unAvailableRooms.size());
   }

   /**
    * Tests removing a room by ID.
    * Verifies that:
    * 1. The room count decreases by 1
    * 2. The deleted room cannot be found
    * 3. Other rooms still exist
    */
   @Test
   void shouldRemoveRoomById() {
       roomRepository.deleteById(room1.getID());

       List<Room> foundRooms = StreamSupport.stream(roomRepository.findAll().spliterator(), false)
               .collect(Collectors.toList());
       assertEquals(2, foundRooms.size(), "Should have 2 rooms saved after removing 1");
       assertFalse(foundRooms.contains(room1), "Should not contain room1 after removing it");
       assertTrue(foundRooms.contains(room2), "Should contain room2 after removing room1");
       assertTrue(foundRooms.contains(room3), "Should contain room3 after removing room1");
   }

   /**
    * Tests updating a room's availability status when it is booked.
    * Verifies that:
    * 1. The room is initially available
    * 2. After booking and saving, the room is unavailable when retrieved again
    */
   @Test
   void shouldUpdateAvailability_whenRoomIsBooked() {
       Room savedRoom = roomRepository.findById(room1.getID()).orElse(null);
       assertTrue(savedRoom.isAvailable(), "Room should be available initially");

       savedRoom.bookRoom(); // Set availability to false
       roomRepository.save(savedRoom);

       Room updatedRoom = roomRepository.findById(savedRoom.getID()).orElse(null);
       assertNotNull(updatedRoom, "Room should still exist after updating availability");
       assertFalse(updatedRoom.isAvailable(), "Room should be unavailable after booking");
   }

   /**
    * Tests updating a room's availability status when it is made available.
    * Verifies that:
    * 1. The room can be booked (made unavailable)
    * 2. After making it available and saving, the room is available when retrieved again
    */
   @Test
   void shouldUpdateAvailability_whenRoomIsMadeAvailable() {
       Room savedRoom = roomRepository.findById(room1.getID()).orElse(null);
       assertNotNull(savedRoom);
       savedRoom.bookRoom(); // Set availability to false
       roomRepository.save(savedRoom);
       assertFalse(savedRoom.isAvailable());

       savedRoom.makeAvailable(); // Set availability to true
       roomRepository.save(savedRoom);

       Room updatedRoom = roomRepository.findById(savedRoom.getID()).orElse(null);
       assertNotNull(updatedRoom, "Room should still exist after updating availability");
       assertTrue(updatedRoom.isAvailable(), "Room should be available after update");
   }
}
