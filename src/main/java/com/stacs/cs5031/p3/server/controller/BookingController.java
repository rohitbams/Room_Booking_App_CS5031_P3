package com.stacs.cs5031.p3.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.mapper.BookingDtoMapper;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.service.BookingService;

/**
 * BookingController class.
 * This class is a REST API controller for Booking operations.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Constructor
     */
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Get all bookings.
     *
     * @return List of BookingDto
     */
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(BookingDtoMapper.mapToDTOList(bookings));
    }

    /**
     * Get a booking by ID.
     *
     * @param bookingId The ID of the booking
     * @return BookingDto
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingService.getBookingById(bookingId)
                    .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));
            return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
        }
    }

    /**
     * Create a new booking.
     *
     * @param request The booking request
     * @return Response message
     */
    @PostMapping("/organiser/{organiserId}")
    public ResponseEntity<String> createBooking(@RequestBody BookingDto.BookingRequest request,
                                                @PathVariable Long organiserId) {
        try {
            bookingService.createBooking(request, organiserId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Booking created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create booking: " + e.getMessage());
        }
    }

    /**
     * Delete a booking by ID.
     *
     * @param bookingId The ID of the booking
     * @return Response message
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Booking deleted successfully.");
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting booking.");
        }
    }

    /**
     * Register an attendee to a booking.
     *
     * @param bookingId  The ID of the booking
     * @param attendeeId The ID of the attendee
     * @return Updated booking
     */
    @PostMapping("/{bookingId}/attendees/{attendeeId}")
    public ResponseEntity<?> registerAttendee(@PathVariable Long bookingId, @PathVariable Long attendeeId) {
        try {
            Booking booking = bookingService.registerAttendee(bookingId, attendeeId);
            return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register attendee: " + e.getMessage());
        }
    }

    // Optional: add unregister attendee if needed
    // @DeleteMapping("/{bookingId}/attendees/{attendeeId}")
    // public ResponseEntity<?> unregisterAttendee(@PathVariable Long bookingId, @PathVariable Long attendeeId) {
    //     try {
    //         Booking booking = bookingService.unregisterAttendee(bookingId, attendeeId);
    //         return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to unregister attendee: " + e.getMessage());
    //     }
    // }

    /**
     * Custom exception class for Booking not found.
     */
    public static class BookingNotFoundException extends RuntimeException {
        public BookingNotFoundException(String message) {
            super(message);
        }
    }
}




// package com.stacs.cs5031.p3.server.controller;

// import com.stacs.cs5031.p3.server.dto.BookingDto;
// import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
// import com.stacs.cs5031.p3.server.model.Booking;
// import com.stacs.cs5031.p3.server.service.BookingService;
// import com.stacs.cs5031.p3.server.mapper.BookingDtoMapper;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// /**
//  * BookingController class.
//  * This class is a REST API controller for Booking operations.
//  */
// @RestController
// @RequestMapping("/api/bookings")
// public class BookingController {

//     private final BookingService bookingService;

//     /**
//      * Constructor
//      */
//     @Autowired
//     public BookingController(BookingService bookingService) {
//         this.bookingService = bookingService;
//     }

//     /**
//      * Get all bookings.
//      *
//      * @return List of BookingDto
//      */
//     @GetMapping
//     public ResponseEntity<List<BookingDto>> getAllBookings() {
//         List<Booking> bookings = bookingService.getAllBookings();
//         return ResponseEntity.ok(BookingDtoMapper.mapToDTOList(bookings));
//     }

//     /**
//      * Get a booking by ID.
//      *
//      * @param bookingId The ID of the booking
//      * @return BookingDto
//      */
    
//     @GetMapping("/{bookingId}")
// public ResponseEntity<?> getBookingById(@PathVariable Integer bookingId) {
//     try {
//         Booking booking = bookingService.getBookingById(Long.valueOf(bookingId))
//                 .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));
//         return ResponseEntity.ok(BookingDtoMapper.mapToDTO(booking));
//     } catch (BookingNotFoundException e) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
//     }
// }

// public class BookingNotFoundException extends RuntimeException {
//     public BookingNotFoundException(String message) {
//         super(message);
//     }
// }

//     /**
//      * Create a new booking.
//      *
//      * @param request The booking request
//      * @return Response message
//      */
//     @PostMapping
//     public ResponseEntity<String> createBooking(@RequestBody BookingDto.BookingRequest request) {
//         try {
//     //        bookingService.createBooking(request, Long.valueOf(organiserID));
//             return ResponseEntity.status(HttpStatus.CREATED).body("Booking created successfully!");
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create booking: " + e.getMessage());
//         }
//     }

//     /**
//      * Delete a booking by ID.
//      *
//      * @param bookingId The ID of the booking
//      * @return Response message
//      */
//     @DeleteMapping("/{bookingId}")
//     public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId) {
//         try {
//             bookingService.deleteBooking(Long.valueOf(bookingId));;
//             return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Booking deleted successfully.");
//         } catch (BookingNotFoundException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found: " + e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting booking.");
//         }
//     }
// }
