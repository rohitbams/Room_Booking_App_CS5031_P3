package com.stacs.cs5031.p3.server.config;

import com.stacs.cs5031.p3.server.model.Admin;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.Room;
import com.stacs.cs5031.p3.server.repository.AdminRepository;
import com.stacs.cs5031.p3.server.repository.AttendeeRepository;
import com.stacs.cs5031.p3.server.repository.OrganiserRepository;
import com.stacs.cs5031.p3.server.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * The DataInitialiser class.
 * This class initialises the database with sample users and rooms at when running
 * the server. This allows the testing the client without having to register new users eah time.
 */
@Component
@Profile("!test")
public class DataInitialiser implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final AttendeeRepository attendeeRepository;
    private final OrganiserRepository organiserRepository;

    /**
     * Constructor.
     * @param adminRepository AdminRepository
     * @param attendeeRepository AttendeeRepository
     * @param organiserRepository OrganiserRepository
     * @param roomRepository RoomRepository
     */
    @Autowired
    public DataInitialiser(AdminRepository adminRepository, AttendeeRepository attendeeRepository, OrganiserRepository organiserRepository, RoomRepository roomRepository) {
        this.adminRepository = adminRepository;
        this.attendeeRepository = attendeeRepository;
        this.organiserRepository = organiserRepository;
        this.roomRepository = roomRepository;
    }

//    @Autowired
//    public DataInitialiser(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }

    /**
     * This method creates sample rooms and users for each user type.
     * @param args args
     */
    @Override
    public void run(String... args) {
        // create admin user
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin("Administrator", "admin", "admin");
            adminRepository.save(admin);
            System.out.println("Admin user created: admin/admin");
        }
        // create attendee user
        if (attendeeRepository.findByUsername("attendee") == null) {
            Attendee attendee = new Attendee("Attendee", "attendee", "password");
            attendeeRepository.save(attendee);
            System.out.println("Attendee user created: attendee/password");
        }

        if (organiserRepository.findByUsername("organiser") == null) {
            Organiser organiser = new Organiser("Organiser", "organiser", "password");
            organiserRepository.save(organiser);
            System.out.println("Organiser user created: organiser/password");
        }

        // creates rooms
        if (roomRepository.count() == 0) {
            roomRepository.save(new Room("JHB Main Lab", 100));
            roomRepository.save(new Room("JHB Silent Lab", 50));
            roomRepository.save(new Room("JCB Tutorial Room A", 10));
            roomRepository.save(new Room("WRL Meeting Room 1/2", 25));
            System.out.println("Sampple rooms created");
        }
    }
}
