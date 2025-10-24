# Room Booking System

## Overview

This project implements a room booking system designed to enable organisers to book rooms for events and allow attendees to register for those events. It includes both a terminal-based client and a graphical user interface (GUI), which interact with the system via a RESTful API.

The system provides functionality such as creating user accounts, booking and cancelling events, signing up or withdrawing from events, and retrieving attendee lists. It aims to offer a simple and effective solution for coordinating events, managing room usage, and tracking attendance.

## Getting Started

To get a local copy of the project, clone the repository:

```bash
git clone https://git-ci.cs.st-andrews.ac.uk/cs5031-p3-group02/p3-code.git
cd p3-code
```

## Running the Application

1. Build the project:

    ```
    mvn clean install
    ```

2. Run the server:

    ```
    mvn spring-boot:run
    ```

3. Run the terminal client:

    ```
    mvn compile exec:java -Dexec.mainClass=com.stacs.cs5031.p3.client.terminal.TerminalClient
    ```

4. Run the GUI client:

    ```
    mvn compile exec:java -Dexec.mainClass=com.stacs.cs5031.p3.client.gui.login.LoginGUI
    ```

5. Run tests:

    ```
    mvn clean test
    ```

## Key Features

- User authentication with username and password
- Role-based access for organisers and attendees
- Room management with capacity and availability details
- Booking management: create, view, update, and delete bookings
- Attendee registration and unregistration
- Automatic room capacity enforcement
- Conflict detection to prevent double-bookings

## User Roles and Capabilities

### All Users

- Register as either an organiser or an attendee
- Log in via terminal or GUI client

### Organisers

- Create new bookings by selecting a room, date, time, and duration
- View and manage bookings they have created
- Edit booking details or delete bookings
- View attendees registered for each booking

### Attendees

- View available bookings
- Register for bookings with available capacity
- View bookings they are registered for
- Unregister from bookings

## Technical Stack

- **Backend**: Java 11, Spring Boot 2.6.7, Spring Data JPA
- **Database**: H2 in-memory database (development)
- **Frontend**: Java Swing (GUI client), Java console application (terminal client)
- **API Communication**: REST with JSON
