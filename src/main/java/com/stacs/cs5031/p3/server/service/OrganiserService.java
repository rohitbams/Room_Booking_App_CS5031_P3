package com.stacs.cs5031.p3.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.stacs.cs5031.p3.server.model.Booking;
import org.springframework.stereotype.Service;

import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.dto.RoomDto;
import com.stacs.cs5031.p3.server.exception.BookingNotFoundException;
import com.stacs.cs5031.p3.server.mapper.AttendeeDtoMapper;
import com.stacs.cs5031.p3.server.mapper.BookingDtoMapper;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.repository.OrganiserRepository;

/**
 * This class is the service layer for the Organiser entity.
 * 
 * @author 190031593
 */
@Service
public class OrganiserService {

    private OrganiserRepository organiserRepository;
    private RoomService roomService;
    private BookingService bookingService;

    /**
     * Constructor for the OrganiserService class.
     * 
     * @param organiserRepository - organiser repository.
     * @param roomService         - room service.
     * @param bookingService      - booking service.
     */
    public OrganiserService(OrganiserRepository organiserRepository, RoomService roomService,
            BookingService bookingService) {
        this.organiserRepository = organiserRepository;
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    /**
     * This method is used to create an organiser and save it to the database.
     * 
     * @param organiser - The organiser to be created.
     * @return String - The status of the operation.
     * @throws IllegalArgumentException - If the organiser credentials are invalid.
     */
    public String createOrganiser(Organiser organiser) {
        if (organiser == null) {
            throw new IllegalArgumentException("Organiser cannot be null");
        }

        String name = organiser.getName();
        String username = organiser.getUsername();
        String password = organiser.getPassword();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Organiser name is invalid");
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Organiser username is invalid");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Organiser password is invalid");
        }

        organiserRepository.save(organiser);
        return "SUCCESS!";
    }

    /***
     * This method is used to get all the organisers from the database.
     * 
     * @return ArrayList<OrganiserDto> - List of all the organisers.
     */
    public ArrayList<OrganiserDto> getAllOrganisers() {
        ArrayList<Organiser> organisers = new ArrayList<Organiser>(organiserRepository.findAll());
        ArrayList<OrganiserDto> organiserDtos = new ArrayList<OrganiserDto>();
        organisers.forEach(organiser -> {
            OrganiserDto organiserDto = new OrganiserDto(organiser.getId(), organiser.getName(),
                    organiser.getUsername());
            organiserDtos.add(organiserDto);
        });

        return organiserDtos;
    }

    /**
     * This method is used to get all the available rooms from the database.
     * 
     * @return ArrayList<RoomDto> - List of all the available rooms.
     */
    public List<RoomDto> getAvailableRooms() {
        return roomService.findAvailableRooms();
    }

    /**
     * This method is used to get all the bookings for an organiser.
     * @param organiserId - The ID of the organiser
     * @return ArrayList<BookingDto> - List of all the bookings for the organiser
     */
    public ArrayList<BookingDto> getBookings(int organiserId) {
        Long organiserIdAsLong = Long.valueOf(organiserId); // Replace with actual organiser ID

        ArrayList<Booking> bookings = (ArrayList<Booking>) bookingService.getBookingsByOrganiser(organiserIdAsLong);

        ArrayList<BookingDto> bookingDtos = new ArrayList<BookingDto>();
        bookings.forEach(booking -> {
            BookingDto bookingDto = BookingDtoMapper.mapToDTO(booking);
            bookingDtos.add(bookingDto);
        });

        return bookingDtos;
        
    }

    /**
     * This method is used to get a booking by its ID.
     * @param bookingId - The ID of the booking
     * @param organiserId - The ID of the organiser
     * @return BookingDto - The booking DTO
     * @throws BookingNotFoundException - If the booking is not found
     */
    public BookingDto getBooking(int bookingId, int organiserId) {
        Long bookingIdAsLong = Long.valueOf(bookingId);
        
        Optional<Booking> organiserBooking = bookingService.getBookingById(bookingIdAsLong);

        if(!organiserBooking.isPresent()) {
            throw new BookingNotFoundException(bookingId);
        }else{
            Booking booking = organiserBooking.get();
            if (booking.getOrganiser().getId() != organiserId) {
                return null;
            }else{
                return BookingDtoMapper.mapToDTO(booking);
            }
        }
    }

    /**
     * This method is used to cancel a booking.
     * 
     * @param bookingId - The ID of the booking to be cancelled
     * @return String - String stating whether or not the booking was cancelled
     *         successfully
     */
    public String cancelBooking(int bookingId, int organiserId) {
        Long bookingIdAsLong = Long.valueOf(bookingId);

        Optional<Booking> organiserBooking = bookingService.getBookingById(bookingIdAsLong);

        if(!organiserBooking.isPresent()) {
            throw new BookingNotFoundException(bookingId);
        }else{
            Booking booking = organiserBooking.get();
            if (booking.getOrganiser().getId() != organiserId) {
                return "You are not the organiser of this booking";
            }else{
                bookingService.deleteBooking(bookingIdAsLong);
                return "SUCCESS!";
            }
        }
    }

    /***
     * This method is used to create a booking for an event.
     * 
     * @param booking     - The booking to be created
     * @param organiserId - The ID of the organiser
     * @return String - String stating whether or not the booking was successful
     */
    public String createBooking(BookingDto.BookingRequest booking, int organiserId) {

        try {
            bookingService.createBooking(booking, Long.valueOf(organiserId));
        } catch (Exception e) {
            return e.getMessage();
        }
        return "SUCCESS!";
    }


    /**
     * This method is used to get all the attendees for a booking.
     * @param bookingId - The ID of the booking
     * @param organiserId - The ID of the organiser
     * @return ArrayList<AttendeeDto> - List of all the attendees for the booking
     */
    public ArrayList<AttendeeDto> getAttendees(int bookingId, int organiserId) {
        Long bookingIdAsLong = Long.valueOf(bookingId);
        Booking booking =  bookingService.getBookingById(bookingIdAsLong).get();
        if(booking.getOrganiser().getId() != organiserId) {
            return null;
        }
        ArrayList<AttendeeDto> attendeeDtos = new ArrayList<AttendeeDto>();
        booking.getAttendees().forEach(attendee -> {
            AttendeeDto bookingDto = AttendeeDtoMapper.mapToDto(attendee);
            attendeeDtos.add(bookingDto);
        });
        return attendeeDtos;
    }
}
