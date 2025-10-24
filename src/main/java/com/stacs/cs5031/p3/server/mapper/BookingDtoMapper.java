package com.stacs.cs5031.p3.server.mapper;

import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.model.Booking;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between {@link Booking} entities and {@link BookingDto} data transfer objects.
 * Provides methods for converting domain models to DTOs for client-side consumption.
 * <p>
 * This mapper handles the conversion of complex object relationships, including nested attendee
 * information, room details, and organiser data.
 * </p>
 */
public final class BookingDtoMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * All methods in this class are static and should be accessed directly.
     */
    private BookingDtoMapper() {
        // Prevent instantiation of utility class
    }

    /**
     * Maps a single {@link Booking} entity to its corresponding {@link BookingDto}.
     * Includes all booking details such as room information, organiser details,
     * and the list of registered attendees.
     *
     * @param booking the booking entity to map
     * @return a data transfer object representing the booking
     */
    public static BookingDto mapToDTO(Booking booking) {
        List<AttendeeDto> attendeeDtos = AttendeeDtoMapper.mapToDtoList(booking.getAttendees());

        return new BookingDto(
                (long) booking.getId(),
                booking.getName(),
                (long) booking.getRoom().getID(),
                booking.getRoom().getName(),
                booking.getStartTime(),
                booking.getDuration(),
                (long) booking.getOrganiser().getId(),
                booking.getOrganiser().getName(),
                attendeeDtos,
                attendeeDtos.size(),
                booking.getRoom().getCapacity()
        );
    }

    /**
     * Maps a list of {@link Booking} entities to a list of {@link BookingDto} objects.
     * Useful for operations that return multiple bookings, such as retrieving all bookings
     * or filtering bookings by criteria.
     *
     * @param bookings the list of booking entities to map
     * @return a list of data transfer objects representing the bookings
     */
    public static List<BookingDto> mapToDTOList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingDtoMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}