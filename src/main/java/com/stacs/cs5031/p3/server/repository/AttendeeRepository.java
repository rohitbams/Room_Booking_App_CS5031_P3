package com.stacs.cs5031.p3.server.repository;

import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The AttendeeRepository class.
 * This class directly interfaces with the database and extends the JpaRepository class.
 * It contains methods for querying, saving, updating, and deleting data.
 */

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {

    Attendee findByUsername(String username);

    @Query("SELECT booking FROM Attendee attendee " +
            "JOIN attendee.registeredBookings booking " +
            "WHERE attendee.id = ?1")
    List<Booking> findRegisteredBookings(Integer attendeeId);

    @Query("SELECT booking FROM Booking booking WHERE booking NOT IN " +
            "(SELECT registered_booking FROM Attendee attendee " +
            "JOIN attendee.registeredBookings registered_booking " +
            "WHERE attendee.id = ?1) " +
            "AND SIZE(booking.attendees) < booking.room.capacity")
    List<Booking> findAvailableBookings(Integer attendeeId);

    @Query("SELECT booking FROM Booking booking WHERE booking NOT IN " +
            "(SELECT registered_booking FROM Attendee attendee " +
            "JOIN attendee.registeredBookings registered_booking " +
            "WHERE attendee.id = ?1) " +
            "AND SIZE(booking.attendees) >= booking.room.capacity")
    List<Booking> findUnavailableBookings(Integer attendeeId);



}
