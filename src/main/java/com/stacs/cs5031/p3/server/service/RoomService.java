package com.stacs.cs5031.p3.server.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.RoomNotAvailableException;
import com.stacs.cs5031.p3.server.exception.RoomNotFoundException;
import com.stacs.cs5031.p3.server.mapper.RoomDtoMapper;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.RoomRepository;

/**
 * Service class that handles room-related operations in the system.
 * Provides methods for room creation, retrieval, booking, and management.
 * Acts as an intermediary between the controller layer and the repository layer.
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * Constructs a new RoomService with the necessary repository dependency.
     *
     * @param roomRepository Repository for room data access
     */
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    /**
     * Creates a new room and returns its DTO representation.
     *
     * @param name     The name of the room
     * @param capacity The capacity of the room
     * @return A DTO representation of the created room
     * @throws IllegalArgumentException if the capacity is less than or equal to 1
     */
    public RoomDto createRoomDto(String name, int capacity) throws IllegalArgumentException {
        // validate user-provided data
        if (capacity <= 1) {
            throw new IllegalArgumentException("Room capacity must be at least 1");
        }
        Room roomEntity = roomRepository.save(new Room(name, capacity));
        return RoomDtoMapper.mapToDTO(roomEntity);
    }

    /**
     * Creates a new room and returns the entity object.
     *
     * @param name     The name of the room
     * @param capacity The capacity of the room
     * @return The created Room entity
     * @throws IllegalArgumentException if the capacity is less than or equal to 1
     */
    public Room createRoom(String name, int capacity) throws IllegalArgumentException {
        // validate user-provided data
        if (capacity <= 1) {
            throw new IllegalArgumentException("Room capacity must be at least 1");
        }
        return roomRepository.save(new Room(name, capacity));
    }

    /**
     * Finds a room by its ID.
     *
     * @param id The ID of the room to find
     * @return The Room entity if found
     * @throws RoomNotFoundException if no room is found with the given ID
     */
    public Room findRoomById(int id) throws RoomNotFoundException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
    }

    /**
     * Finds a room by its ID and returns its DTO representation.
     *
     * @param id The ID of the room to find
     * @return A DTO representation of the room if found
     * @throws RoomNotFoundException if no room is found with the given ID
     */
    public RoomDto findRoomDtoById(int id) throws RoomNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
        return RoomDtoMapper.mapToDTO(room);
    }

    /**
     * Retrieves all rooms in the system.
     *
     * @return A list of DTO representations of all rooms
     */
    public List<RoomDto> findAllRooms(){
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
        .map(RoomDtoMapper::mapToDTO)
        .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all available rooms in the system.
     *
     * @return A list of DTO representations of all available rooms
     */
    public List<RoomDto> findAvailableRooms() {
        return StreamSupport.stream(roomRepository.findByAvailability(true).spliterator(), false)
        .map(RoomDtoMapper::mapToDTO)
        .collect(Collectors.toList());
    }
    
    /**
     * Books a room by its ID.
     *
     * @param id The ID of the room to book
     * @return A DTO representation of the booked room
     * @throws RoomNotAvailableException if the room is already booked
     * @throws RoomNotFoundException if no room is found with the given ID
     */
    public RoomDto bookRoom(int id) throws RoomNotAvailableException, RoomNotFoundException {
        Room roomEntity = roomRepository.findById(id)
        .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
        
        // if room is already booked, throw exception
        if (!roomEntity.isAvailable()) {
            throw new RoomNotAvailableException("Room " + id + " is already booked");
        }
        
        // if room is not booked
        roomEntity.bookRoom();
        roomRepository.save(roomEntity);
        return RoomDtoMapper.mapToDTO(roomEntity);
    }
    
    /**
     * Makes a room available by its ID.
     *
     * @param id The ID of the room to make available
     * @return A DTO representation of the available room
     * @throws RoomNotFoundException if no room is found with the given ID
     */
    public RoomDto makeRoomAvailable(int id) throws RoomNotFoundException{
        Room roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));

        // if room is already available, do nothing
        if (!roomEntity.isAvailable()) {
            roomEntity.makeAvailable();
            roomRepository.save(roomEntity);
        }

        return RoomDtoMapper.mapToDTO(roomEntity);
    }

    /**
     * Deletes a room by its ID.
     *
     * @param id The ID of the room to delete
     * @throws RoomNotFoundException if no room is found with the given ID
     */
    public void deleteRoomById(int id) throws RoomNotFoundException {
        Room roomEntity = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));

        roomRepository.delete(roomEntity);
    }
}
