//package com.stacs.cs5031.p3.server.mapper;
//
//import com.stacs.cs5031.p3.server.model.Attendee;
//import com.stacs.cs5031.p3.server.model.Booking;
//import com.stacs.cs5031.p3.server.model.Organiser;
//import com.stacs.cs5031.p3.server.model.Room;
//import com.stacs.cs5031.p3.server.model.User;
//import com.stacs.cs5031.p3.server.dto.BookingDTO;
//import com.stacs.cs5031.p3.server.dto.RoomDto;
//import com.stacs.cs5031.p3.server.dto.UserDto.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Contains methods to map between entities and DTOs.
// */
//public class DTOMappers {
//
//    /**
//     * Maps Room entity to RoomDTO.
//     *
//     * @param room The Room entity
//     * @return The RoomDTO
//     */
//    public static RoomDTO toRoomDTO(Room room) {
//        if (room == null) {
//            return null;
//        }
//
//        return new RoomDTO(
//            room.getID(),
//            room.getName(),
//            room.getCapacity(),
//            room.isAvailable()
//        );
//    }
//
//    /**
//     * Maps User entity to UserDTO.
//     *
//     * @param user The User entity
//     * @return The UserDTO
//     */
//    public static UserDTO toUserDTO(User user) {
//        if (user == null) {
//            return null;
//        }
//
//        if (user instanceof Organiser) {
//            return toOrganiserDTO((Organiser) user);
//        } else if (user instanceof Attendee) {
//            return toAttendeeDTO((Attendee) user);
//        }
//
//        // Generic User (should not happen with current implementation)
//        return new UserDTO(
//            user.getId(),
//            user.getUsername(),
//            user.getFullName(),
//            user.getEmail(),
//            "USER"
//        );
//    }
//
//    /**
//     * Maps Organiser entity to OrganiserDTO.
//     *
//     * @param organiser The Organiser entity
//     * @return The OrganiserDTO
//     */
//    public static OrganiserDTO toOrganiserDTO(Organiser organiser) {
//        if (organiser == null) {
//            return null;
//        }
//
//        List<Long> bookingIds = organiser.getCreatedBookings().stream()
//            .map(Booking::getId)
//            .collect(Collectors.toList());
//
//        return new OrganiserDTO(
//            organiser.getId(),
//            organiser.getUsername(),
//            organiser.getFullName(),
//            organiser.getEmail(),
//            bookingIds
//        );
//    }
//
//    /**
//     * Maps Attendee entity to AttendeeDTO.
//     *
//     * @param attendee The Attendee entity
//     * @return The AttendeeDTO
//     */
//    public static AttendeeDTO toAttendeeDTO(Attendee attendee) {
//        if (attendee == null) {
//            return null;
//        }
//
//        List<Long> bookingIds = attendee.getRegisteredBookings().stream()
//            .map(Booking::getId)
//            .collect(Collectors.toList());
//
//        return new AttendeeDTO(
//            attendee.getId(),
//            attendee.getUsername(),
//            attendee.getFullName(),
//            attendee.getEmail(),
//            bookingIds
//        );
//    }
//
//    /**
//     * Maps Booking entity to BookingDTO.
//     *
//     * @param booking The Booking entity
//     * @return The BookingDTO
//     */
//    public static BookingDTO toBookingDTO(Booking booking) {
//        if (booking == null) {
//            return null;
//        }
//
//        List<AttendeeDTO> attendeeDTOs = booking.getAttendees().stream()
//            .map(DTOMappers::toAttendeeDTO)
//            .collect(Collectors.toList());
//
//        return new BookingDTO(
//            booking.getId(),
//            booking.getName(),
//            booking.getRoom().getID(),
//            booking.getRoom().getName(),
//            booking.getStartTime(),
//            booking.getDuration(),
//            booking.getOrganiser().getId(),
//            booking.getOrganiser().getFullName(),
//            attendeeDTOs,
//            booking.getAttendees().size(),
//            booking.getRoom().getCapacity()
//        );
//    }
//
//    /**
//     * Maps list of Booking entities to list of BookingDTOs.
//     *
//     * @param bookings The list of Booking entities
//     * @return The list of BookingDTOs
//     */
//    public static List<BookingDTO> toBookingDTOs(List<Booking> bookings) {
//        return bookings.stream()
//            .map(DTOMappers::toBookingDTO)
//            .collect(Collectors.toList());
//    }
//
//    /**
//     * Maps list of Room entities to list of RoomDTOs.
//     *
//     * @param rooms The list of Room entities
//     * @return The list of RoomDTOs
//     */
//    public static List<RoomDTO> toRoomDTOs(List<Room> rooms) {
//        return rooms.stream()
//            .map(DTOMappers::toRoomDTO)
//            .collect(Collectors.toList());
//    }
//
//    /**
//     * Maps list of User entities to list of UserDTOs.
//     *
//     * @param users The list of User entities
//     * @return The list of UserDTOs
//     */
//    public static List<UserDTO> toUserDTOs(List<User> users) {
//        return users.stream()
//            .map(DTOMappers::toUserDTO)
//            .collect(Collectors.toList());
//    }
//}
