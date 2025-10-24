//package com.stacs.cs5031.p3.server.service;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
//import com.stacs.cs5031.p3.server.model.Booking;
//import com.stacs.cs5031.p3.server.model.Organiser;
//import com.stacs.cs5031.p3.server.model.Room;
//import com.stacs.cs5031.p3.server.repository.BookingRepository;
//
//public class BookingServiceTest {
//
//    private BookingRepository bookingRepository;
//    private BookingService bookingService;
//
//    @BeforeEach
//public void setUp() {
//    bookingRepository = mock(BookingRepository.class);
//    bookingService = mock(BookingService.class);  // Mock the abstract class
//}
//
//
//    @Test
//public void testGetAllBookings() {
//    List<Booking> bookings = Arrays.asList(
//        new Booking(
//            "Event1",
//            new Room("Room1", 10),
//            new Date(),
//            60,
//            new Organiser("Alice Smith", "alice", "pass123")
//        ),
//        new Booking(
//            "Event2",
//            new Room("Room2", 20),
//            new Date(),
//            30,
//            new Organiser("Bob Johnson", "bob", "pass456")
//        )
//    );
//    when(bookingRepository.findAll()).thenReturn(bookings);
//
//    List<Booking> result = bookingService.getAllBookings();
//    assertEquals(2, result.size());
//    verify(bookingRepository, times(1)).findAll();
//}
//
//
//    @Test
//public void testGetBookingById_Found() {
//    Booking booking = new Booking(
//        "Event",
//        new Room("Room", 15),
//        new Date(),
//        45,
//        new Organiser("John Doe", "johndoe", "securePassword")
//    );
//    when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//    Optional<Booking> result = bookingService.getBookingById(1L);
//    assertTrue(result.isPresent());
//    assertEquals("Event", result.get().getName());
//}
//
//    @Test
//    public void testGetBookingById_NotFound() {
//        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<Booking> result = bookingService.getBookingById(1L);
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//public void testDeleteBooking_Success() {
//    Booking booking = new Booking(
//        "Event",
//        new Room("Room", 15),
//        new Date(),
//        45,
//        new Organiser("John Doe", "johndoe", "securePassword")
//    );
//    when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//    bookingService.deleteBooking(1L);
//
//    verify(bookingRepository, times(1)).delete(booking);
//}
//
//
//    @Test
//    public void testDeleteBooking_NotFound() {
//        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBooking(1L));
//    }
//}
