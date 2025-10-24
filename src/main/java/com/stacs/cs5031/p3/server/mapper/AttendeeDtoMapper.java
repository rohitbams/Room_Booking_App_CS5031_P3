package com.stacs.cs5031.p3.server.mapper;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AttendeeDtoMapper class.
 * Utility class used for mapping Attendee entities and DTOs.
 */
public final class AttendeeDtoMapper {

    private AttendeeDtoMapper() {
    }

    /**
     * Map an Attendee entity to an AttendeeDto.
     *
     * @param attendee the attendee entity
     * @return the mapped AttendeeDto
     */
    public static AttendeeDto mapToDto(Attendee attendee) {
        if (attendee == null) {
            return null;
        }

        List<Integer> bookingIds = attendee.getRegisteredBookings().stream()
                .map(Booking::getId)
                .collect(Collectors.toList());

        return new AttendeeDto(
                attendee.getId(),
                attendee.getUsername(),
                attendee.getName(),
                bookingIds
        );
    }

    /**
     * Map a list of Attendee entities to a list of AttendeeDtos.
     *
     * @param attendees The list of attendee entities
     * @return The list of mapped AttendeeDtos
     */
    public static List<AttendeeDto> mapToDtoList(List<Attendee> attendees) {
        return attendees.stream()
                .map(AttendeeDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

}