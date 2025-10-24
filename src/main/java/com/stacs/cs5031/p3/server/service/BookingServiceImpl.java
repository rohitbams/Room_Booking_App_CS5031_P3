package com.stacs.cs5031.p3.server.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.exception.EntityNotFoundException;
import com.stacs.cs5031.p3.server.exception.ResourceUnavailableException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.BookingRepository;
import com.stacs.cs5031.p3.server.repository.OrganiserRepository;
import com.stacs.cs5031.p3.server.repository.RoomRepository;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final OrganiserRepository organiserRepository;
    private final AttendeeService attendeeService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              OrganiserRepository organiserRepository,
                              AttendeeService attendeeService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.organiserRepository = organiserRepository;
        this.attendeeService = attendeeService;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Booking registerAttendee(Long bookingId, Long attendeeId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        // Convert Long attendeeId to Integer
        Attendee attendee = attendeeService.getAttendeeById(attendeeId.intValue());
        if (attendee == null) {
            throw new EntityNotFoundException("Attendee not found with ID: " + attendeeId);
        }
    

        if (booking.getAttendees().size() >= booking.getRoom().getCapacity()) {
            throw new ResourceUnavailableException("Room is at full capacity");
        }

        booking.getAttendees().add(attendee);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking unregisterAttendee(Long bookingId, Long attendeeId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        // Convert Long attendeeId to Integer
        Attendee attendee = attendeeService.getAttendeeById(attendeeId.intValue());
       if (attendee == null) {
           throw new EntityNotFoundException("Attendee not found with ID: " + attendeeId);
       }
   

        booking.getAttendees().remove(attendee);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByRoom(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public List<Booking> getBookingsByOrganiser(Long organiserId) {
        return bookingRepository.findByOrganiserId(organiserId);
    }

    @Override
    public List<Booking> getBookingsByAttendee(Long attendeeId) {
        return bookingRepository.findByAttendeesId(attendeeId);
    }
    

    @Override
    public boolean hasConflict(Long roomId, Date startTime, long duration) {
        List<Booking> roomBookings = bookingRepository.findByRoomId(roomId);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        Date endTime = new Date(calendar.getTimeInMillis() + duration * 60000);

        for (Booking booking : roomBookings) {
            Date existingStart = booking.getStartTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(existingStart);
            Date existingEnd = new Date((cal.getTimeInMillis() + booking.getDuration() * 60000) -60000);

            
            boolean isNotOverlapping =
            endTime.before(existingStart) ||
            startTime.after(existingEnd) ;

            if (!isNotOverlapping) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    public Booking createBooking(BookingDto.BookingRequest bookingDTO, Long organiserId) {
        // 1. Get room
        Room room = roomRepository.findById(bookingDTO.getRoomId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + bookingDTO.getRoomId()));

        // 2. Get organiser
        Organiser organiser = organiserRepository.findById(organiserId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Organiser not found with ID: " + organiserId));

        // 3. Check if room is available
        if (hasConflict((long)room.getID(), bookingDTO.getStartTime(), bookingDTO.getDuration())) {
            throw new ResourceUnavailableException("Room is not available");
        }

        // 4. Create booking object
        Booking booking = new Booking(
                bookingDTO.getEventName(),
                room,
                bookingDTO.getStartTime(),
                bookingDTO.getDuration(),
                organiser
        );

        // 5. Save and return booking
        return bookingRepository.save(booking);
    }

//     @Transactional
// public Booking createBooking(BookingDto.BookingRequest bookingDTO, Long organiserId) {
//     Room room = roomRepository.findById(bookingDTO.getRoomId().intValue()) // convert Long to Integer
//         .orElseThrow(() -> new EntityNotFoundException("Room not found"));

//     Organiser organiser = organiserRepository.findById(organiserId.intValue()) // convert Long to Integer
//         .orElseThrow(() -> new EntityNotFoundException("Organiser not found"));

//      if (hasConflict(Long.valueOf(room.getID()), bookingDTO.getStartTime(), bookingDTO.getDuration())) {
//         {
//         throw new BookingConflictException("Room already booked for the specified time");
//     }

//     Booking booking = new Booking();
//     booking.setRoom(room);
//     booking.setOrganiser(organiser);
//     booking.setStartTime(bookingDTO.getStartTime());
//     booking.setDuration(bookingDTO.getDuration());
//     booking.setTitle(bookingDTO.getTitle());

//     //if (bookingDTO.getDescription() != null) {
//        booking.setDescription(bookingDTO.getDescription());
//     //}

//     return bookingRepository.save(booking);
// }
// }
}

