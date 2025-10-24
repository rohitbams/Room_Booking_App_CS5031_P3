package com.stacs.cs5031.p3.server.repository;

import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the {@link AttendeeRepository} interface.
 * Tests CRUD operations and custom query methods on Attendee entities using an in-memory database.
 * The @DataJpaTest annotation configures an in-memory database and JPA repositories for testing.
 */
@DataJpaTest
public class AttendeeRepositoryTest {

    /** TestEntityManager for entity operations during tests */
    @Autowired
    private TestEntityManager entityManager;

    /** The AttendeeRepository instance being tested, automatically injected by Spring */
    @Autowired
    private AttendeeRepository attendeeRepository;

    /** Test attendee instance used across multiple test methods */
    private Attendee attendee;
    
    /** Test room instance used for creating bookings */
    private Room room;
    
    /** Test organiser instance used for creating bookings */
    private Organiser organiser;
    
    /** Test booking instance that is available for registration */
    private Booking availableBooking;
    
    /** Test booking instance that the test attendee is already registered for */
    private Booking registeredBooking;
    
    /** Test booking instance that is at full capacity */
    private Booking fullBooking;
    
    /** Event name used for test bookings */
    private String eventName = "Hannah Montana concert";

    /**
     * Setup before each test.
     * Creates and persists sample entities for testing including:
     * - An attendee
     * - A room
     * - An organiser
     * - An available booking
     * - A booking that the attendee is registered for
     * - A booking that is at full capacity
     */
    @BeforeEach
    public void setup() {
        attendee = new Attendee("Hannah Montana", "hannahmontana", "mileystewart");
        entityManager.persist(attendee);

        room = new Room("Room 101", 5);
        entityManager.persist(room);

        organiser = new Organiser("Jackson Stewart", "jackson", "jacksonstewart123");
        entityManager.persist(organiser);

        availableBooking = new Booking(eventName, room, new Date(), 60, organiser);
        entityManager.persist(availableBooking);

        registeredBooking = new Booking(eventName, room, new Date(), 60, organiser);
        entityManager.persist(registeredBooking);
        attendee.getRegisteredBookings().add(registeredBooking);
        entityManager.persist(attendee);

        fullBooking = new Booking("Full Event", room, new Date(), 60, organiser);
        fillBookingToCapacity(fullBooking);
        entityManager.persist(fullBooking);

        entityManager.flush();
    }

    /**
     * Helper method to fill a booking to its room capacity.
     * Creates fake attendees and adds them to the booking until capacity is reached.
     *
     * @param booking The booking to fill with attendees
     */
    private void fillBookingToCapacity(Booking booking) {
        for (int i = 0; i < booking.getRoom().getCapacity(); i++) {
            Attendee fakeAttendee = new Attendee("Fake" + i, "fake" + i, "password");
            entityManager.persist(fakeAttendee);
            booking.getAttendees().add(fakeAttendee);
        }
    }

    /**
     * Tests that an attendee can be found by username.
     * Verifies that:
     * 1. The found attendee is not null
     * 2. The username of the found attendee matches the expected username
     */
    @Test
    public void testFindByUsername() {
        Attendee found = attendeeRepository.findByUsername("hannahmontana");
        assertNotNull(found);
        assertEquals("hannahmontana", found.getUsername());
    }

    /**
     * Tests that finding an attendee by a non-existent username returns null.
     * Verifies that the repository correctly handles queries for non-existent usernames.
     */
    @Test
    public void nonExistentAttendeeShouldReturnNull() {
        Attendee found = attendeeRepository.findByUsername("billyraystewart");
        assertNull(found);
    }

    /**
     * Tests that registered bookings can be found for an attendee.
     * Verifies that:
     * 1. The list of registered bookings is not null
     * 2. The list contains the expected number of bookings
     * 3. The booking name matches the expected name
     */
    @Test
    public void shouldFindRegisteredBookings() {
        List<Booking> registeredBookings = attendeeRepository.findRegisteredBookings(attendee.getId());
        assertNotNull(registeredBookings);
        assertEquals(1, registeredBookings.size());
        assertEquals("Hannah Montana concert", registeredBookings.get(0).getName());
    }

    /**
     * Tests that available bookings can be found for an attendee.
     * Available bookings are those that:
     * 1. The attendee is not already registered for
     * 2. Have space available for registration
     * 
     * Verifies that:
     * 1. The list of available bookings is not null
     * 2. The list contains the expected number of bookings
     * 3. The booking name matches the expected name
     */
    @Test
    public void shouldFindAvailableBookings() {
        List<Booking> availableBookings = attendeeRepository.findAvailableBookings(attendee.getId());
        assertNotNull(availableBookings);
        assertEquals(1, availableBookings.size());
        assertEquals("Hannah Montana concert", availableBookings.get(0).getName());
    }

    /**
     * Tests that unavailable bookings can be found for an attendee.
     * Unavailable bookings are those that:
     * 1. The attendee is not registered for
     * 2. Are at full capacity
     * 
     * Verifies that:
     * 1. The list of unavailable bookings is not null
     * 2. The list contains the expected number of bookings
     * 3. The booking name matches the expected name
     */
    @Test
    public void shouldFindUnavailableBookings() {
        List<Booking> unavailableBookings = attendeeRepository.findUnavailableBookings(attendee.getId());
        assertNotNull(unavailableBookings);
        assertEquals(1, unavailableBookings.size());
        assertEquals("Full Event", unavailableBookings.get(0).getName());
    }
}
