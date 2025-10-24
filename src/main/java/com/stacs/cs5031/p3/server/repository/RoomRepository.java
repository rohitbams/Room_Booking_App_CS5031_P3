package com.stacs.cs5031.p3.server.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.stacs.cs5031.p3.server.model.Room;

/**
 * Repository interface for Room entity operations.
 * Provides methods to perform CRUD operations on Room entities in the database.
 * Extends CrudRepository to inherit common data access operations.
 */
@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

    /**
     * Finds all rooms with the specified availability status.
     * 
     * @param availability The availability status to search for (true for available rooms, false for booked rooms)
     * @return A list of rooms matching the specified availability status
     */
    public List<Room> findByAvailability(boolean availability);
}
