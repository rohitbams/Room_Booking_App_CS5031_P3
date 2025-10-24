package com.stacs.cs5031.p3.server.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a booking in the system.
 * Contains information about the event, room, time, organiser, and attendees.
 */
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String eventName;

    @ManyToOne
    private Room room;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    private int duration; // in minutes

    @ManyToOne
    private Organiser organiser;

    // Attendee to be implemented first
    @ManyToMany
    private List<Attendee> attendees = new ArrayList<>();

    @ManyToOne
    private Attendee attendee;
    private LocalDateTime bookingTime;


    /**
     * Default constructor required by JPA.
     */
    public Booking() {
        // Required by JPA
    }

    /**
     * Constructor for creating a new booking.
     *
     * @param eventName Name of the event
     * @param room      Room where the event will take place
     * @param startTime Start time of the event
     * @param duration  Duration in minutes
     * @param organiser User who organized the event
     */
    public Booking(String eventName, Room room, Date startTime, int duration, Organiser organiser) {
        this.eventName = eventName;
        this.room = room;
        this.startTime = startTime;
        this.duration = duration;
        this.organiser = organiser;
    }

    /**
     * Gets the booking ID.
     *
     * @return The booking ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the event name.
     *
     * @return The event name
     */
    public String getName() {
        return eventName;
    }

    /**
     * Sets a new event name.
     *
     * @param eventName The new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the room where the event will take place.
     *
     * @return The room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets a new room for the event.
     *
     * @param room The new room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets a new start time for the event.
     *
     * @param startTime The new start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the duration of the event in minutes.
     *
     * @return The duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets a new duration for the event.
     *
     * @param duration The new duration in minutes
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the organiser of the event.
     *
     * @return The organiser
     */
    public Organiser getOrganiser() {
        return organiser;
    }

    /**
     * Sets a new organiser for the event.
     *
     * @param organiser The new organiser
     */
    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }

    /**
     * Gets the list of attendees for this event.
     *
     * @return The list of attendees
     */
    public List<Attendee> getAttendees() {
        return attendees;
    }

    /**
     * Checks if there is space available in the room for more attendees.
     *
     * @return true if there is space, false if the room is at capacity
     */
    public boolean isThereSpace() {
        return attendees.size() < room.getCapacity();
    }

    /**
     * Adds an attendee to the booking if there is space available.
     *
     * @param attendee The attendee to add
     * @return true if the attendee was added successfully, false if there was no space
     */
    public boolean addAttendee(Attendee attendee) {
        if (attendees.contains(attendee)) {
            return false; // Already registered
        }

        if (isThereSpace()) {
            attendees.add(attendee);
            return true;
        }

        return false;
    }

    /**
     * Removes an attendee from the booking.
     *
     * @param attendee The attendee to remove
     * @return true if the attendee was removed, false if they were not registered
     */
    public boolean removeAttendee(Attendee attendee) {
        return attendees.remove(attendee);
    }

    /**
     * Calculates the end time of the event based on start time and duration.
     *
     * @return The end time
     */
    public Date getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, duration);
        return calendar.getTime();
    }

    /**
     * Checks if this booking overlaps with another booking.
     *
     * @param other The other booking to check against
     * @return true if there is an overlap, false otherwise
     */
    public boolean overlaps(Booking other) {
        // If the rooms are different, there's no overlap
        if (!this.room.equals(other.room)) {
            return false;
        }

        Date thisEnd = this.getEndTime();
        Date otherEnd = other.getEndTime();

        // Check if one booking starts during the other
        return (this.startTime.before(otherEnd) && thisEnd.after(other.startTime));
    }

    /**
     * Gets the current number of attendees.
     *
     * @return The number of attendees
     */
    public int getCurrentAttendeeCount() {
        return attendees.size();
    }

    /**
     * Gets the maximum capacity for the booking (from the room).
     *
     * @return The maximum capacity
     */
    public int getMaxCapacity() {
        return room.getCapacity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Booking [id=" + id +
                ", eventName=" + eventName +
                ", room=" + (room != null ? room.getName() : "null") +
                ", startTime=" + startTime +
                ", duration=" + duration + " mins" +
                ", organiser=" + (organiser != null ? organiser.getUsername() : "null") +
                ", attendees=" + (attendees != null ? attendees.size() : 0) +
                "]";
    }

    // creates a new ArrayList from the attendees Set
    public void setAttendees(Set<Attendee> attendees) {
        this.attendees = new ArrayList<>(attendees);
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }


    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}