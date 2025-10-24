package com.stacs.cs5031.p3.client.terminal;

import com.stacs.cs5031.p3.server.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TerminalClient class.
 * This is a command-line interface for the Room Booking application.
 */
public class TerminalClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);
    private static UserDto currentUser = null;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static boolean running = true;

    public static void main(String[] args) {
        while (running) {
            if (currentUser == null) {
                showWelcomeMenu();
            } else {
                String role = currentUser.getRole();
                if ("ORGANISER".equals(role)) {
                    showOrganiserMenu();
                } else if ("ATTENDEE".equals(role)) {
                    showAttendeeMenu();
                } else if ("ADMIN".equals(role)) {
                    showAdminMenu();
                }
            }
        }

        scanner.close();
    }

    /**
     * This method display user login menu.
     */
    private static void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = handleLogin(username, password);

        if (success) {
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
        } else {
            System.out.println("Login failed. Please check your username and password.");
        }
    }

    /**
     * This method handles user login.
     * @param username user's username
     * @param password user's password
     * @return true if login is successful and user data is received, false if login failed or an error occurred
     */
    static boolean handleLogin(String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            UserDto response = restTemplate.postForObject(
                    BASE_URL + "/users/login",
                    request,
                    UserDto.class
            );

            if (response != null) {
                currentUser = response;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method implements user logout.
     */
    private static void logout() {
        System.out.println("Logging out...");
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    /**
     * This method displays user registration menu.
     */
    private static void register() {
        System.out.println("\n=== Registration ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Select role:");
        System.out.println("1. Organiser");
        System.out.println("2. Attendee");
        System.out.print("Enter choice (1 or 2): ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        String role = roleChoice == 1 ? "ORGANISER" : "ATTENDEE";

        boolean success = handleRegistration(name, username, password, role);

        if (success) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    /**
     * This method handles user registration.
     * @param name user's name
     * @param username user's username
     * @param password user's password
     * @param role user's role (organiser or attendee)
     * @return true if registration is successful, false if registration failed or an error occurred
     */
    static boolean handleRegistration(String name, String username, String password, String role) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", name);
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("role", role);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            UserDto response = restTemplate.postForObject(
                    BASE_URL + "/users/register",
                    request,
                    UserDto.class
            );

            return response != null;
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method shows the welcome menu which is the first menu that is displayed
     * when the terminal is started.
     */
    private static void showWelcomeMenu() {
        System.out.println("=== Room Booking System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter the number next to your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * This method shows options for an organiser.
     */
    private static void showOrganiserMenu() {
        System.out.println("\n=== Organiser Menu ===");
        System.out.println("1. Create Booking");
        System.out.println("2. Cancel Booking");
        System.out.println("3. View Available Rooms");
        System.out.println("4. View My Bookings");
        System.out.println("5. Logout");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                createBooking();
                break;
            case 2:
                cancelBooking();
                break;
            case 3:
                viewAvailableRooms();
                break;
            case 4:
                viewMyBookings();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * This method gets all available rooms for an organiser to book.
     * @return list of available bookings
     */
    static List<RoomDto> getAvailableRooms() {
        try {
            RoomDto[] rooms = restTemplate.getForObject(
                    BASE_URL + "/rooms/available",
                    RoomDto[].class
            );

            if (rooms == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(rooms);
        } catch (Exception e) {
            System.out.println("Error retrieving rooms: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * This method handles displays available rooms to an organiser to register for a booking.
     */
    private static void viewAvailableRooms() {
        System.out.println("\n=== Available Rooms ===");
        List<RoomDto> rooms = getAvailableRooms();

        if (rooms.isEmpty()) {
            System.out.println("No available rooms found.");
            return;
        }

        System.out.println("Available Rooms:");
        for (RoomDto room : rooms) {
            System.out.println("ID: " + room.getId() +
                    ", Name: " + room.getName() +
                    ", Capacity: " + room.getCapacity());
        }
    }

    /**
     * This method handles creating a booking for an organiser
     */
    private static void createBooking() {
        System.out.println("\n=== Create Booking ===");

        try {
            RoomDto[] rooms = restTemplate.getForObject(
                    BASE_URL + "/rooms/available",
                    RoomDto[].class
            );

            // Check if there are rooms
            if (rooms == null || rooms.length == 0) {
                System.out.println("No available rooms found.");
                return;
            }

            // Show available rooms
            System.out.println("Available Rooms:");
            for (int i = 0; i < rooms.length; i++) {
                System.out.println((i+1) + ". " + rooms[i].getName() + " (Capacity: " + rooms[i].getCapacity() + ")");
            }

            // Room selection input
            System.out.print("Select room number: ");
            int roomIndex = scanner.nextInt();
            scanner.nextLine();

            if (roomIndex < 1 || roomIndex > rooms.length) {
                System.out.println("Invalid room selection.");
                return;
            }

            // Event name input
            System.out.print("Enter event name: ");
            String eventName = scanner.nextLine();

            // Date input
            System.out.print("Enter start date and time (DD-MM-YYYY HH:MM): ");
            String startTimeStr = scanner.nextLine();

            // Duration input with validation
            int durationHours;
            do {
                System.out.print("Enter duration in hours (1-8): ");
                durationHours = scanner.nextInt();
                scanner.nextLine();

                if (durationHours < 1 || durationHours > 8) {
                    System.out.println("Duration must be between 1 and 8 hours.");
                }
            } while (durationHours < 1 || durationHours > 8);

            // Create a BookingDto object
            BookingDto.BookingRequest bookingRequest = new BookingDto.BookingRequest();
            bookingRequest.setEventName(eventName);
            bookingRequest.setRoomId(Long.valueOf(rooms[roomIndex-1].getId())); // Convert to Long

            // Parse date from string to Date object
            try {
                Date parsedDate = DATE_FORMAT.parse(startTimeStr);
                bookingRequest.setStartTime(parsedDate);
                if (parsedDate.before(new Date())) {
                    System.out.println("Date cannot be in the past!");
                    return;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use DD-MM-YYYY HH:MM");
                return;
            }


            // Convert hours to minutes for duration
            int durationMinutes = durationHours * 60;
            bookingRequest.setDuration(durationMinutes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<BookingDto.BookingRequest> request = new HttpEntity<>(bookingRequest, headers);

            String response = restTemplate.postForObject(
                    BASE_URL + "/organiser/create-booking/" + currentUser.getId(),
                    request,
                    String.class
            );


            System.out.println("Booking created successfully!");


        } catch (Exception e) {
            System.out.println("Failed to create booking, please try again." + e.getMessage());
        }
    }

    /**
     * This method cancels made booking for an organiser
     */
    private static void cancelBooking() {
        System.out.println("\n=== Cancel Booking ===");
        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/organiser/my-bookings/" + currentUser.getId(),
                    BookingDto[].class
            );

            // Check if user has bookings
            if (bookings == null || bookings.length == 0) {
                System.out.println("You have no bookings to cancel.");
                return;
            }

            displayBookings(bookings, "Your Bookings:");

            // Get user selection
            System.out.print("Select booking to cancel (number): ");
            int bookingIndex = scanner.nextInt();
            scanner.nextLine();

            if (bookingIndex < 1 || bookingIndex > bookings.length) {
                System.out.println("Invalid booking selection.");
                return;
            }

            Long bookingId = bookings[bookingIndex-1].getId();

            // Send delete request
            restTemplate.delete(BASE_URL + "/organiser/cancel-booking/" + bookingId + "/" + currentUser.getId());

            System.out.println("Booking cancelled successfully.");

        } catch (Exception e) {
            System.out.println("Sorry, booking cancel request failed: " + e.getMessage());
        }
    }

    /**
     * This method shows registered bookings by an organiser.
     */
    private static void viewMyBookings() {
        System.out.println("\n=== My Bookings ===");
        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/organiser/my-bookings/" + currentUser.getId(),
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("You have no bookings.");
                return;
            }

            displayBookings(bookings, "Your Bookings:");

        } catch (Exception e) {
            System.out.println("Failed to retrieve your bookings: " + e.getMessage());
        }
    }

    /**
     * This method displays the menu of options for an Attendee.
     */
    private static void showAttendeeMenu() {
        System.out.println("\n=== Attendee Menu ===");
        System.out.println("1. View Available Bookings");
        System.out.println("2. View Unavailable Bookings");
        System.out.println("3. Register for a Booking");
        System.out.println("4. View My Registered Bookings");
        System.out.println("5. Deregister from a Booking");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewAvailableBookings();
                break;
            case 2:
                viewUnavailableBookings();
                break;
            case 3:
                registerForBooking();
                break;
            case 4:
                viewMyRegisteredBookings();
                break;
            case 5:
                deregisterFromBooking();
                break;
            case 6:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * This method shows all the available bookings that an attendee can register.
     */
    private static void viewAvailableBookings() {
        System.out.println("\n=== Available Bookings ===");

        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/available-bookings",
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("No available bookings found.");
                return;
            }

            displayBookings(bookings, "Available Bookings:");

        } catch (Exception e) {
            System.out.println("Failed to retrieve available bookings: " + e.getMessage());
        }
    }

    /**
     * This method shows the bookings that are currently unavailable for an attendee to register due to full capacity.
     */
    private static void viewUnavailableBookings() {
        System.out.println("\n=== Unavailable Bookings ===");

        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/unavailable-bookings",
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("No unavailable bookings found.");
                return;
            }

            displayBookings(bookings, "Unavailable Bookings (Full Capacity):");

        } catch (Exception e) {
            System.out.println("Failed to retrieve unavailable bookings: " + e.getMessage());
        }
    }

    /**
     * This method implements registering for an event for an attendee.
     */
    private static void registerForBooking() {
        System.out.println("\n=== Register for Booking ===");

        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/available-bookings",
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("No available bookings found.");
                return;
            }

            System.out.println("Available Bookings:");
            for (int i = 0; i < bookings.length; i++) {
                int durationHours = bookings[i].getDuration() / 60;
                System.out.println((i+1) + ". " + bookings[i].getEventName() +
                        " - " + DATE_FORMAT.format(bookings[i].getStartTime()) +
                        " - " + durationHours + " hours - " +
                        bookings[i].getCurrentAttendees() + "/" + bookings[i].getMaxCapacity() + " attendees");
            }

            System.out.print("Select booking to register for (number): ");
            int bookingIndex = scanner.nextInt();
            scanner.nextLine();

            if (bookingIndex < 1 || bookingIndex > bookings.length) {
                System.out.println("Invalid booking selection.");
                return;
            }

            Long bookingId = bookings[bookingIndex-1].getId();

            BookingDto response = restTemplate.postForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/register/" + bookingId,
                    null,
                    BookingDto.class
            );

            System.out.println("Successfully registered for booking!");

        } catch (Exception e) {
            System.out.println("Failed to register for booking: " + e.getMessage());
        }
    }

    /**
     * This method shows registered bookings to an attendee.
     */
    private static void viewMyRegisteredBookings() {
        System.out.println("\n=== My Registered Bookings ===");

        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/registered-bookings",
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("You are not registered for any bookings.");
                return;
            }

            displayBookings(bookings, "Your Registered Bookings:");

        } catch (Exception e) {
            System.out.println("Failed to retrieve your registered bookings: " + e.getMessage());
        }
    }

    /**
     * this method handles deregistering from a booking for an attendee.
     */
    private static void deregisterFromBooking() {
        System.out.println("\n=== Deregister from Booking ===");

        try {
            BookingDto[] bookings = restTemplate.getForObject(
                    BASE_URL + "/attendees/" + currentUser.getId() + "/registered-bookings",
                    BookingDto[].class
            );

            if (bookings == null || bookings.length == 0) {
                System.out.println("You are not registered for any bookings.");
                return;
            }

            System.out.println("Your Registered Bookings:");
            for (int i = 0; i < bookings.length; i++) {
                int durationHours = bookings[i].getDuration() / 60;
                System.out.println((i+1) + ". " + bookings[i].getEventName() +
                        " - " + DATE_FORMAT.format(bookings[i].getStartTime()) +
                        " - " + durationHours + " hours");
            }

            System.out.print("Select booking to deregister from (number): ");
            int bookingIndex = scanner.nextInt();
            scanner.nextLine();

            if (bookingIndex < 1 || bookingIndex > bookings.length) {
                System.out.println("Invalid booking selection.");
                return;
            }

            Long bookingId = bookings[bookingIndex-1].getId();

            restTemplate.delete(BASE_URL + "/attendees/" + currentUser.getId() + "/cancel/" + bookingId);

            System.out.println("Successfully deregistered from booking.");

        } catch (Exception e) {
            System.out.println("Failed to deregister from booking: " + e.getMessage());
        }
    }

    /**
     * This method shows options available for an admin, like viewing all rooms, adding rooms,
     * removing rooms, viewing all attendees, and viewing all organisers.
     */
    private static void showAdminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. View All Rooms");
        System.out.println("2. Add Room");
        System.out.println("3. Remove Room");
        System.out.println("4. View All Attendees");
        System.out.println("5. View All Organisers");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewAllRooms();
                break;
            case 2:
                addRoom();
                break;
            case 3:
                removeRoom();
                break;
            case 4:
                viewAllAttendees();
                break;
            case 5:
                viewAllOrganisers();
                break;
            case 6:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * This method shows all rooms to an admin.
     */
    private static void viewAllRooms() {
        System.out.println("\n=== All Rooms ===");

        try {
            RoomDto[] rooms = restTemplate.getForObject(
                    BASE_URL + "/admin/rooms",
                    RoomDto[].class
            );

            if (rooms == null || rooms.length == 0) {
                System.out.println("No rooms found.");
                return;
            }

            System.out.println("Rooms:");
            for (RoomDto room : rooms) {
                System.out.println("ID: " + room.getId() +
                        ", Name: " + room.getName() +
                        ", Capacity: " + room.getCapacity() +
                        ", Available: " + room.isAvailable());
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve rooms: " + e.getMessage());
        }
    }

    /**
     * This method allows an admin to add a room.
     */
    private static void addRoom() {
        System.out.println("\n=== Add Room ===");

        System.out.print("Enter room name: ");
        String name = scanner.nextLine();

        System.out.print("Enter room capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", name);
            requestBody.put("capacity", capacity);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(
                    BASE_URL + "/admin/rooms",
                    request,
                    String.class
            );

            System.out.println("Room added successfully: " + response);

        } catch (Exception e) {
            System.out.println("Failed to add room: " + e.getMessage());
        }
    }

    /**
     * This method allows an admin to remove a room from the database.
     */
    private static void removeRoom() {
        System.out.println("\n=== Remove Room ===");

        try {
            RoomDto[] rooms = restTemplate.getForObject(
                    BASE_URL + "/admin/rooms",
                    RoomDto[].class
            );

            if (rooms == null || rooms.length == 0) {
                System.out.println("No rooms found to remove.");
                return;
            }

            System.out.println("Rooms:");
            for (int i = 0; i < rooms.length; i++) {
                System.out.println((i+1) + ". " + rooms[i].getName() +
                        " (Capacity: " + rooms[i].getCapacity() +
                        ", Available: " + rooms[i].isAvailable() + ")");
            }

            System.out.print("Select room to remove (number): ");
            int roomIndex = scanner.nextInt();
            scanner.nextLine();

            if (roomIndex < 1 || roomIndex > rooms.length) {
                System.out.println("Invalid room selection.");
                return;
            }

            int roomId = rooms[roomIndex-1].getId();

            restTemplate.delete(BASE_URL + "/admin/rooms/" + roomId);

            System.out.println("Room removed successfully.");

        } catch (Exception e) {
            System.out.println("Failed to remove room: " + e.getMessage());
        }
    }

    /**
     * This method shows all attendee users to an admin.
     */
    private static void viewAllAttendees() {
        System.out.println("\n=== All Attendees ===");

        try {
            AttendeeDto[] attendees = restTemplate.getForObject(
                    BASE_URL + "/admin/attendees",
                    AttendeeDto[].class
            );

            if (attendees == null || attendees.length == 0) {
                System.out.println("No attendees found.");
                return;
            }

            System.out.println("Attendees:");
            for (AttendeeDto attendee : attendees) {
                System.out.println("ID: " + attendee.getId() +
                        ", Name: " + attendee.getName() +
                        ", Username: " + attendee.getUsername());
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve attendees: " + e.getMessage());
        }
    }

    /**
     * This method shows all organiser users to an admin.
     */
    private static void viewAllOrganisers() {
        System.out.println("\n=== All Organisers ===");

        try {
            OrganiserDto[] organisers = restTemplate.getForObject(
                    BASE_URL + "/admin/organisers",
                    OrganiserDto[].class
            );

            if (organisers == null || organisers.length == 0) {
                System.out.println("No organisers found.");
                return;
            }

            System.out.println("Organisers:");
            for (OrganiserDto organiser : organisers) {
                System.out.println("ID: " + organiser.getId() +
                        ", Name: " + organiser.getName() +
                        ", Username: " + organiser.getUsername());
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve organisers: " + e.getMessage());
        }
    }

    /**
     * Helper method to display bookings
     * @param bookings bookings to display
     * @param title display text before the bookings list
     */
    private static void displayBookings(BookingDto[] bookings, String title) {
        System.out.println(title);
        for (BookingDto booking : bookings) {
            int durationHours = booking.getDuration() / 60;
            System.out.println("ID: " + booking.getId() +
                    ", Event: " + booking.getEventName() +
                    ", Room: " + booking.getRoomName() +
                    ", Time: " + DATE_FORMAT.format(booking.getStartTime()) +
                    ", Duration: " + durationHours + " hours" +
                    ", Attendees: " + booking.getCurrentAttendees() + "/" + booking.getMaxCapacity());
        }
    }

    // Testing utility methods
    static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    static UserDto getCurrentUser() {
        return currentUser;
    }

    static void setCurrentUser(UserDto user) {
        currentUser = user;
    }

    static void setRestTemplate(RestTemplate template) {
        restTemplate = template;
    }

    static void setRunning(boolean isRunning) {
        running = isRunning;
    }
}