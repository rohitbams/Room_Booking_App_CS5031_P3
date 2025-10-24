//package com.stacs.cs5031.p3.server.dto;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
///**
// * Test class for BookingDto.
// */
//public class BookingDtoTest {
//
//    private BookingDto bookingDTO;
//    private Date startTime;
//    private List<AttendeeDto> attendees;
//
//    @BeforeEach
//    public void setUp() {
//        startTime = new Date();
//
//        // Create some attendee DTOs for testing
//        attendees = new ArrayList<>();
//        attendees.add(new AttendeeDto(1, "attendee1", "Alice Johnson", 1, null));
//        attendees.add(new AttendeeDto(2, "attendee2", "Bob Smith", 2, null));
//
//        // Create a booking DTO with all fields
//        bookingDTO = new BookingDto(
//            1L,                  // id
//            "Team Meeting",      // eventName
//            2L,                  // roomId
//            "Conference Room A", // roomName
//            startTime,           // startTime
//            60,                  // duration
//            3L,                  // organiserId
//            "John Organiser",    // organiserName
//            attendees,           // attendees
//            2,                   // currentAttendees
//            10                   // maxCapacity
//        );
//    }
//
//    @Test
//    public void testAllArgsConstructor() {
//        // Assert all fields are correctly set
//        assertEquals(1L, bookingDTO.getId());
//        assertEquals("Team Meeting", bookingDTO.getEventName());
//        assertEquals(2L, bookingDTO.getRoomId());
//        assertEquals("Conference Room A", bookingDTO.getRoomName());
//        assertEquals(startTime, bookingDTO.getStartTime());
//        assertEquals(60, bookingDTO.getDuration());
//        assertEquals(3L, bookingDTO.getOrganiserId());
//        assertEquals("John Organiser", bookingDTO.getOrganiserName());
//        assertEquals(attendees, bookingDTO.getAttendees());
//        assertEquals(2, bookingDTO.getCurrentAttendees());
//        assertEquals(10, bookingDTO.getMaxCapacity());
//    }
//
//    @Test
//    public void testNoArgsConstructor() {
//        // Create a DTO using no-args constructor
//        BookingDto emptyDTO = new BookingDto();
//
//        // Assert all fields are null or default values
//        assertNull(emptyDTO.getId());
//        assertNull(emptyDTO.getEventName());
//        assertNull(emptyDTO.getRoomId());
//        assertNull(emptyDTO.getRoomName());
//        assertNull(emptyDTO.getStartTime());
//        assertEquals(0, emptyDTO.getDuration());
//        assertNull(emptyDTO.getOrganiserId());
//        assertNull(emptyDTO.getOrganiserName());
//        assertNull(emptyDTO.getAttendees());
//        assertEquals(0, emptyDTO.getCurrentAttendees());
//        assertEquals(0, emptyDTO.getMaxCapacity());
//    }
//
//    @Test
//    public void testSetters() {
//        // Create a DTO using no-args constructor
//        BookingDto dto = new BookingDto();
//
//        // Set values using setters
//        dto.setId(5L);
//        dto.setEventName("New Meeting");
//        dto.setRoomId(6L);
//        dto.setRoomName("Meeting Room B");
//        Date newDate = new Date(startTime.getTime() + 3600000); // 1 hour later
//        dto.setStartTime(newDate);
//        dto.setDuration(90);
//        dto.setOrganiserId(7L);
//        dto.setOrganiserName("Jane Organiser");
//        List<AttendeeDto> newAttendees = new ArrayList<>();
//        dto.setAttendees(newAttendees);
//        dto.setCurrentAttendees(0);
//        dto.setMaxCapacity(15);
//
//        // Assert all fields are correctly set
//        assertEquals(5L, dto.getId());
//        assertEquals("New Meeting", dto.getEventName());
//        assertEquals(6L, dto.getRoomId());
//        assertEquals("Meeting Room B", dto.getRoomName());
//        assertEquals(newDate, dto.getStartTime());
//        assertEquals(90, dto.getDuration());
//        assertEquals(7L, dto.getOrganiserId());
//        assertEquals("Jane Organiser", dto.getOrganiserName());
//        assertEquals(newAttendees, dto.getAttendees());
//        assertEquals(0, dto.getCurrentAttendees());
//        assertEquals(15, dto.getMaxCapacity());
//    }
//
//    @Test
//    public void testBookingRequest() {
//        // Create a booking request
//        BookingDto.BookingRequest request = new BookingDto.BookingRequest();
//        request.setEventName("Request Meeting");
//        request.setRoomId(8L);
//        request.setStartTime(startTime);
//        request.setDuration(120);
//
//        // Assert all fields are correctly set
//        assertEquals("Request Meeting", request.getEventName());
//        assertEquals(8L, request.getRoomId());
//        assertEquals(startTime, request.getStartTime());
//        assertEquals(120, request.getDuration());
//    }
//
//    @Test
//    public void testEqualsAndHashCode() {
//        // Create a copy of the bookingDTO with the same ID
//        BookingDto sameDTOWithSameId = new BookingDto(
//            1L,                      // Same ID
//            "Different Meeting",     // Different name
//            4L,                      // Different roomId
//            "Different Room",        // Different roomName
//            new Date(),              // Different startTime
//            30,                      // Different duration
//            5L,                      // Different organiserId
//            "Different Organiser",   // Different organiserName
//            new ArrayList<>(),       // Different attendees
//            0,                       // Different currentAttendees
//            5                        // Different maxCapacity
//        );
//
//        // Create a different DTO with a different ID
//        BookingDto differentDTO = new BookingDto(
//            99L,                     // Different ID
//            bookingDTO.getEventName(),
//            bookingDTO.getRoomId(),
//            bookingDTO.getRoomName(),
//            bookingDTO.getStartTime(),
//            bookingDTO.getDuration(),
//            bookingDTO.getOrganiserId(),
//            bookingDTO.getOrganiserName(),
//            bookingDTO.getAttendees(),
//            bookingDTO.getCurrentAttendees(),
//            bookingDTO.getMaxCapacity()
//        );
//
//        // Test equals method
//        assertEquals(bookingDTO, bookingDTO);                // Same object reference
//        assertEquals(bookingDTO, sameDTOWithSameId);         // Different object, same ID
//        assertNotEquals(bookingDTO, differentDTO);           // Different ID
//        assertNotEquals(bookingDTO, null);                   // Null comparison
//        assertNotEquals(bookingDTO, new Object());           // Different types
//
//        // Test hashCode method
//        assertEquals(bookingDTO.hashCode(), sameDTOWithSameId.hashCode());         // Same ID, same hash code
//        assertNotEquals(bookingDTO.hashCode(), differentDTO.hashCode());           // Different ID, different hash code
//    }
//
//    @Test
//    public void testToString() {
//        // Just make sure it doesn't throw an exception and returns a non-null string
//        String toString = bookingDTO.toString();
//        assertNotNull(toString);
//        assertTrue(toString.contains("Team Meeting"));  // Should contain the event name
//        assertTrue(toString.contains("1"));             // Should contain the ID
//    }
//}