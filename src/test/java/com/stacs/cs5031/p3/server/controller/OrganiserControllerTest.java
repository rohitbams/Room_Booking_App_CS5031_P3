package com.stacs.cs5031.p3.server.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Booking;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.repository.AttendeeRepository;
import com.stacs.cs5031.p3.server.repository.OrganiserRepository;
import com.stacs.cs5031.p3.server.service.AttendeeService;
import com.stacs.cs5031.p3.server.service.BookingService;
import com.stacs.cs5031.p3.server.service.RoomService;

import jakarta.transaction.Transactional;

/**
 * This class is responsible for testing the Organiser Controller - integration
 * tests.
 * 
 * @author 190031593
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
@ActiveProfiles("test")
public class OrganiserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private OrganiserRepository organiserRepository;
        @Autowired
        private AttendeeRepository attendeeRepository;

        @Autowired
        private static RoomService roomService;

        @Autowired
        private AttendeeService attendeeService;

        @Autowired
        private BookingService bookingService;

        private Organiser organiser1, organiser2, organiser3, organiser4, organiser5;

        @BeforeAll
        static void setUpAll(@Autowired RoomService roomService2) {
                roomService = roomService2;
                roomService.createRoom("R1", 100);
                roomService.createRoom("R2", 200);
               
        }

        @BeforeEach
        void setUp() {
                organiserRepository.deleteAll();
                System.out.println("Organiser in repository: " + organiserRepository.findAll());
                organiser1 = new Organiser("James Dean", "james.dean", "password123");
                organiser2 = new Organiser("Holly Dean", "", "password123");
                organiser4 = new Organiser("Jim Dean", "jim.dean", null);
                organiser3 = new Organiser("Johnny Doe", "johnny.doe", "passwordABC"); 
                organiser5 = new Organiser("Millie Doe", "mil.doe", "passwordABC"); 
        }

            /**
         * This test is responsible for testing that all organisers can be retrieved
         * from the
         * database without issue. In this case, there are no organisers in the
         * database.
         *
         * @throws Exception
         */
        @Test
        @Order(1)
        void shouldGetAllOrganisersIfNoneExist() throws Exception {

                this.mockMvc.perform(
                                get("/organisers"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isEmpty());
        }

        /**
         * This test is responsible for testing that an organiser can be created without
         * issue.
         * 
         * @throws Exception
         */
        @Test
        @Order(2)
        void shouldCreateOrganiser() throws Exception {
               
                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser5))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(content().string("SUCCESS!"));
                
        }

        /**
         * This test is responsible for testing that an exception is thrown when the
         * organiser information is invalid. I.e. username, name or password is
         * null/empty.
         * 
         * @throws Exception
         */
        @Test
        @Order(3)
        void shouldNotCreateOrganiserIfCredentialAreInvalid() throws Exception {

                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser2))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect((content().string("Organiser username is invalid")));

                organiser2.setName(null);

                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser2))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect((content().string("Organiser name is invalid")));

                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser4))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect((content().string("Organiser password is invalid")));
        }

        /**
         * This test is reponsible for testing that all organisers can be retrieved from
         * the
         * database without issue.
         * 
         * @throws Exception
         */
        @Test
        @Order(4)
        void shouldGetAllOrganisers() throws Exception {
                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser1))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(content().string("SUCCESS!"));

                this.mockMvc.perform(
                                post("/organiser/create-organiser")
                                                .content(new ObjectMapper().writeValueAsString(organiser3))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(content().string("SUCCESS!"));

                this.mockMvc.perform(
                                get("/organisers"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.[0].username").value(organiser1.getUsername()))
                                .andExpect(jsonPath("$.[1].username").value(organiser3.getUsername()));

        }

    

        /**
         * This test is responsible for testing that all available rooms can be
         * retrieved
         */
        @Test
        @Order(5)
        void shouldGetAllAvailableRooms() throws Exception {

                this.mockMvc.perform(
                                get("/organiser/available-rooms"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.[0].name").value("R1"))
                                .andExpect(jsonPath("$.[1].name").value("R2"));
        }

        /**
         * This test is responsible for testing that a booking can be created without
         * issue.
         */
        @Test
        @Order(6)
        void shouldCreateBookingWithoutIssue() throws Exception {
                int roomId = 1;
                Organiser org = organiserRepository.save(organiser1);
                int organiserId = org.getId().intValue();
     

                BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest("Event 1", Long.valueOf(roomId), new Date(), 2,
                                "Test");

                this.mockMvc.perform(
                                post("/organiser/create-booking" + "/" + organiserId)
                                                .content(new ObjectMapper().writeValueAsString(bookingRequest))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(content().string("SUCCESS!"));

        }

        /**
         * This test is responsible for testing that a booking cannot be retrieved if it
         * does not
         * exist in the database.
         * 
         * @throws Exception
         */
        @Test
        @Order(7)
        void shouldNotGetBookingDetailsIfBookingDoesNotExist() throws Exception {
                int organiserId = 1;
                int bookingId = 1;
                this.mockMvc.perform(
                                get("/organiser/" + organiserId + "my-bookings/" + bookingId))
                                .andExpect(status().isNotFound());
        }

        /**
         * This test is responsible for testing that a booking can be retrieved without
         * issue.
         */
        @Test
        @Order(8)
        void shouldGetBookingDetailsWithoutIssue() throws Exception {
                Organiser org = organiserRepository.save(organiser1);
                int organiserId = org.getId().intValue();
                
                BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest("Event 1", 1L, new Date(), 2,
                                "Testing 1");
                Booking b = bookingService.createBooking(bookingRequest, Long.valueOf(organiserId));
                this.mockMvc.perform(
                                get("/organiser/" + organiserId+ "/my-bookings/" + b.getId()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.eventName").value("Event 1"))
                                .andExpect(jsonPath("$.roomId").value(1L));

        }

        /**
         * This test is responsible for testing that a booking can be cancelled without
         * issue.
         * 
         * @throws Exception
         */
        @Test
        @Order(9)
        void shouldCancelBookingWithoutIssue() throws Exception {
                Organiser org = organiserRepository.save(organiser1);
                int organiserId = org.getId().intValue();

                BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest("Event 1", 1L, new Date(), 2,
                                "Test");

                Booking b = bookingService.createBooking(bookingRequest, 1L);

                this.mockMvc.perform(
                                delete("/organiser/cancel-booking/" + organiserId+"/"+b.getId()))
                                .andExpect(status().isNoContent())
                                .andExpect(content().string("SUCCESS!"));

        }

        /**
         * This test is responsible for testing that all bookings can be retrieved from
         * the
         * database without issue.
         * 
         * @throws Exception
         */
        @Test
        @Order(10)
        void shouldGetAllBookingsWithoutIssue() throws Exception {
                Organiser org = organiserRepository.save(organiser1);
                int organiserId = org.getId().intValue();
                BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest("Event 1", 1L, new Date(), 2,
                                "Testing 1");
                BookingDto.BookingRequest bookingRequest2 = new BookingDto.BookingRequest("Event 2", 2L, new Date(), 2,
                                "Testing 2");

                Booking b1 = bookingService.createBooking(bookingRequest, Long.valueOf(organiserId));
                Booking b2 = bookingService.createBooking(bookingRequest2, Long.valueOf(organiserId));

                this.mockMvc.perform(
                                get("/organiser/my-bookings/" + organiserId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.[0].eventName").value(b1.getName()))
                                .andExpect(jsonPath("$.[1].eventName").value(b2.getName()));

        }


        /**
         * This test is responsible for checking that all attendees can be retrieved for an organisers event.
         * @throws Exception
         */
        @Test
        @Order(11)
        void shouldGetAllAttendeesWithoutIssue() throws Exception {
                Organiser org = organiserRepository.save(organiser1);
                int organiserId = org.getId().intValue();
                BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest("Event 1", 1L, new Date(), 2,
                                "Testing 1");

                Booking b1 = bookingService.createBooking(bookingRequest, Long.valueOf(organiserId));

                Attendee attendee1 = new Attendee("John Doe", "john.doe", "password123");
                attendeeRepository.save(attendee1);     
                attendeeService.registerForBooking(attendee1.getId(), b1.getId());

                this.mockMvc.perform(
                                get("/organiser/"+org.getId()+"/my-bookings/" + b1.getId()+"/attendees"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.[0].name").value(attendee1.getName()));

        }

}
