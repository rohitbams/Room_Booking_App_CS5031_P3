package com.stacs.cs5031.p3.server.mapper;

import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.model.Room;

/**
 * Utility class for mapping between Room entity objects and RoomDto data transfer objects.
 * This class provides methods to convert between the domain model and DTOs used for API responses.
 * It is implemented as a utility class with a private constructor to prevent instantiation.
 */
public final class RoomDtoMapper {
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RoomDtoMapper() {
        // Prevent instantiation of utility class
    }

    /**
     * Maps a Room entity to a RoomDto data transfer object.
     * This method extracts the necessary information from the Room entity and creates a new DTO with that data.
     *
     * @param room The Room entity to map
     * @return A new RoomDto containing the data from the Room entity
     */
    public static RoomDto mapToDTO(Room room) {
        return new RoomDto(
            room.getID(),
            room.getName(),
            room.getCapacity(),
            room.isAvailable()
        );
    }
}
