//package com.stacs.cs5031.p3.server.controller;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.stacs.cs5031.p3.server.dto.BookingDto;
//import com.stacs.cs5031.p3.server.dto.BookingDto.BookingRequest;
//import com.stacs.cs5031.p3.server.dto.AttendeeDto;
//import com.stacs.cs5031.p3.server.exception.BookingConflictException;
//import com.stacs.cs5031.p3.server.exception.EntityNotFoundException;
//import com.stacs.cs5031.p3.server.exception.ResourceUnavailableException;
//import com.stacs.cs5031.p3.server.service.BookingService;
//import com.stacs.cs5031.p3.server.service.RoomService;
//import com.stacs.cs5031.p3.server.service.UserService;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class BookingControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private BookingService bookingService;
//
//    @MockitoBean
//    private RoomService roomService;
//
//    @MockitoBean
//    private UserService userService;
//
//    private BookingDto testBooking1;
//    private BookingDto testBooking2;
//    private BookingRequest testBookingRequest;
//    private List<AttendeeDto> attendees;
//
//    @BeforeEach
//    void setup() {
//        // Create test attendees
//        AttendeeDto attendee1 = new AttendeeDto(1L, "attendee1", "Alice Johnson", 1, List.of(1L));
//        AttendeeDto attendee2 = new AttendeeDto(2L, "attendee2", "Bob Smith", 2, List.of(1L, 2L));
//        attendees = Arrays.asList(attendee1, attendee2);
//
//        // Create test bookings
//        testBooking1 = new BookingDto(
//            1L, "Team Meeting", 1L, "Conference Room A", new Date(), 60,
//            1L, "John Organiser", attendees, 2, 10
//        );
//
//        testBooking2 = new BookingDto(
//            2L, "Project Planning", 2L, "Meeting Room B", new Date(), 90,
//            1L, "John Organiser", List.of(), 0, 8
//        );
//
//        // Create test booking request
//        testBookingRequest = new BookingRequest();
//        testBookingRequest.setEventName("New Meeting");
//        testBookingRequest.setRoomId(1L);
//        testBookingRequest.setStartTime(new Date());
//        testBookingRequest.setDuration(60);
//    }
//
//    @Test
//    void shouldGetBookingWithValidId() throws Exception {
//        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(testBooking1));
//
//        mvc.perform(get("/api/bookings/1")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.eventName", is("Team Meeting")))
//                .andExpect(jsonPath("$.roomId", is(1)))
//                .andExpect(jsonPath("$.duration", is(60)))
//                .andExpect(jsonPath("$.attendees", hasSize(2)));
//    }
//
//    @Test
//    void shouldReturnNotFoundWithInvalidBookingId() throws Exception {
//        when(bookingService.getBookingById(999L)).thenReturn(Optional.empty());
//
//        mvc.perform(get("/api/bookings/999")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldGetAllBookings_whenBookingsExist() throws Exception {
//        List<BookingDto> expectedBookings = Arrays.asList(testBooking1, testBooking2);
//        when(bookingService.getAllBookings()).thenReturn(expectedBookings);
//
//        mvc.perform(get("/api/bookings")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].eventName", is("Team Meeting")))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].eventName", is("Project Planning")));
//    }
//
//    @Test
//    void shouldReturnEmptyList_whenNoBookingsExist() throws Exception {
//        when(bookingService.getAllBookings()).thenReturn(List.of());
//
//        mvc.perform(get("/api/bookings")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//    }
//
//    @Test
//    void shouldCreateBooking_withValidData() throws Exception {
//        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(null)); // Just needs to not throw exception
//        when(userService.getOrganiserById(anyLong())).thenReturn(Optional.of(null)); // Just needs to not throw exception
//        when(bookingService.hasConflict(anyLong(), any(Date.class), anyLong())).thenReturn(false);
//        when(bookingService.saveBooking(any())).thenReturn(testBooking1);
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(post("/api/bookings?organiserId=1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.eventName", is("Team Meeting")));
//    }
//
//    @Test
//    void shouldReturnBadRequest_whenRoomIsNotAvailable() throws Exception {
//        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(null));
//        when(userService.getOrganiserById(anyLong())).thenReturn(Optional.of(null));
//        when(bookingService.hasConflict(anyLong(), any(Date.class), anyLong())).thenReturn(true);
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(post("/api/bookings?organiserId=1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    void shouldReturnNotFound_whenRoomDoesNotExist() throws Exception {
//        when(roomService.getRoomById(anyLong())).thenReturn(Optional.empty());
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(post("/api/bookings?organiserId=1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldReturnNotFound_whenOrganiserDoesNotExist() throws Exception {
//        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(null));
//        when(userService.getOrganiserById(anyLong())).thenReturn(Optional.empty());
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(post("/api/bookings?organiserId=1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldUpdateBooking_withValidData() throws Exception {
//        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(testBooking1));
//        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(null));
//        when(bookingService.saveBooking(any())).thenReturn(testBooking1);
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(put("/api/bookings/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    void shouldReturnNotFound_whenUpdatingNonExistentBooking() throws Exception {
//        when(bookingService.getBookingById(999L)).thenReturn(Optional.empty());
//
//        String requestJson = objectMapper.writeValueAsString(testBookingRequest);
//
//        mvc.perform(put("/api/bookings/999")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldDeleteBooking_withValidId() throws Exception {
//        mvc.perform(delete("/api/bookings/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void shouldRegisterAttendee_toBooking() throws Exception {
//        when(bookingService.registerAttendee(1L, 1L)).thenReturn(testBooking1);
//
//        mvc.perform(post("/api/bookings/1/register/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.attendees", hasSize(2)));
//    }
//
//    @Test
//    void shouldReturnBadRequest_whenRegisteringToFullBooking() throws Exception {
//        when(bookingService.registerAttendee(1L, 3L)).thenThrow(new ResourceUnavailableException("Booking is full"));
//
//        mvc.perform(post("/api/bookings/1/register/3"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldReturnNotFound_whenRegisteringToNonExistentBooking() throws Exception {
//        when(bookingService.registerAttendee(999L, 1L)).thenThrow(new EntityNotFoundException("Booking not found"));
//
//        mvc.perform(post("/api/bookings/999/register/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldUnregisterAttendee_fromBooking() throws Exception {
//        when(bookingService.unregisterAttendee(1L, 1L)).thenReturn(testBooking1);
//
//        mvc.perform(post("/api/bookings/1/unregister/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    void shouldGetBookingsByRoom() throws Exception {
//        List<BookingDto> roomBookings = Arrays.asList(testBooking1);
//        when(bookingService.getBookingsByRoom(1L)).thenReturn(roomBookings);
//
//        mvc.perform(get("/api/bookings/room/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].roomId", is(1)));
//    }
//
//    @Test
//    void shouldGetBookingsByOrganiser() throws Exception {
//        List<BookingDto> organiserBookings = Arrays.asList(testBooking1, testBooking2);
//        when(bookingService.getBookingsByOrganiser(1L)).thenReturn(organiserBookings);
//
//        mvc.perform(get("/api/bookings/organiser/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].organiserId", is(1)))
//                .andExpect(jsonPath("$[1].organiserId", is(1)));
//    }
//
//    @Test
//    void shouldGetBookingsByAttendee() throws Exception {
//        List<BookingDto> attendeeBookings = Arrays.asList(testBooking1);
//        when(bookingService.getBookingsByAttendee(1L)).thenReturn(attendeeBookings);
//
//        mvc.perform(get("/api/bookings/attendee/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id", is(1)));
//    }
//}