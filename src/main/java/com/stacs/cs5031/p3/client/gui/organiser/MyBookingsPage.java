package com.stacs.cs5031.p3.client.gui.organiser;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.stacs.cs5031.p3.client.gui.helper_classes.*;
import com.stacs.cs5031.p3.server.dto.AttendeeDto;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.UserDto;
import javafx.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBookingsPage extends JFrame {
    private ArrayList<BookingDto> bookings;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    public MyBookingsPage(UserDto user) {
        int organiserId = user.getId();
        // Set up the frame
        setTitle("My Bookings Page");
        setSize(1143, 617);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#b2c590"));
        mainPanel.setLayout(null);
        
        // Add title label
        JLabel titleLabel = new JLabel("My Bookings", JLabel.CENTER);
        titleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 30));
        titleLabel.setForeground(Color.decode("#000"));
        titleLabel.setBounds(50, 20, 1000, 50);
        mainPanel.add(titleLabel);
        
        // Fetch bookings
        bookings = fetchBookings(organiserId);
        
        // Create the table panel
        JPanel tablePanel = addTablePanel();
        tablePanel.setBounds(50, 100, 1030, 400);
        mainPanel.add(tablePanel);
        addBackButton(user, mainPanel);

        // Add to frame
        add(mainPanel);
    }

    private JPanel addTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#c2c5aa")); // Set background color for the panel
        panel.setOpaque(false);
        panel.setBorder(new RoundedBorder(20, Color.decode("#414833"), 3));
        
        // Initialise table model
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        // Add columns
        tableModel.addColumn("Event Name");
        tableModel.addColumn("Date & Time");
        tableModel.addColumn("Organiser Name");

        // Create and configure table
        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.getTableHeader().setReorderingAllowed(false);
        bookingTable.setAutoCreateRowSorter(true);

        // Set custom font for table header
        Font headerFont = CustomFontLoader.loadFont("./resources/fonts/Lato.ttf", 20);
        bookingTable.getTableHeader().setFont(headerFont);

        // Set custom font for table cells
        Font cellFont = CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14);
        bookingTable.setFont(cellFont);
        bookingTable.setRowHeight(25); // Adjust row height for better readability

        // Set table background and font colours
        bookingTable.setBackground(Color.decode("#c2c5aa")); // Background color
        bookingTable.setForeground(Color.decode("#414833")); // Font color

        // Disable grid lines
        bookingTable.setShowGrid(false);
        // Set custom renderer for the first column (Event Name)
        EventNameCellRenderer renderer = new EventNameCellRenderer();
        bookingTable.getColumnModel().getColumn(0).setCellRenderer(renderer);

        // Add mouse listener to reset hover on exit
        bookingTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point hoverPoint = e.getPoint();
                int row = bookingTable.rowAtPoint(hoverPoint);
                renderer.setHoveredRow(row); // Reset the hovered row
                bookingTable.repaint(); // Repaint the table to remove the hover effect
            }
        });

        // Populate table with data
        populateTable();

        // Add click handling for the table rows
        bookingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookingTable.getSelectedRow();
                if (row != -1) {
                    // Convert to model row if table is sorted
                    if (bookingTable.getRowSorter() != null) {
                        row = bookingTable.getRowSorter().convertRowIndexToModel(row);
                    }

                    if (row < bookings.size()) {
                        BookingDto selectedBooking = bookings.get(row);
                        showBookingDetailsDialog(selectedBooking);
                    }
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.getViewport().setBackground(Color.decode("#c2c5aa"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void populateTable() {
        // Clear existing data
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        // Format for displaying the date/time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Add rows to the table model from the BookingDto objects
        for (BookingDto booking : bookings) {
            String formattedDate = booking.getStartTime() != null ? dateFormat.format(booking.getStartTime()) : "N/A";

            tableModel.addRow(new Object[] {
                    booking.getEventName(),
                    formattedDate,
                    booking.getOrganiserName()
            });
        }
    }

    private void showBookingDetailsDialog(BookingDto booking) {
        JDialog dialog = new JDialog(this);
        dialog.setTitle("Booking Details");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.decode("#d4d3b3"));
    
        // Add event name as title
        JLabel titleLabel = new JLabel(booking.getEventName() ,
                JLabel.CENTER);
        titleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 22));
        titleLabel.setForeground(Color.decode("#414833"));
        titleLabel.setBackground(Color.decode("#d4d3b3"));
        titleLabel.setOpaque(true);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Details panel with grid layout
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 15, 12));
        detailsPanel.setBackground(Color.decode("#d4d3b3"));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 5, 15, 5),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.decode("#414833"), 3),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15))));

        // Add all booking details with larger fonts
        Font detailFont = CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 16);
        
        // Add all booking details
        addDetailRow(detailsPanel, "Room:", booking.getRoomName() + " <" + booking.getRoomId() + ">", detailFont);

        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = booking.getStartTime() != null ? fullDateFormat.format(booking.getStartTime()) : "N/A";
        addDetailRow(detailsPanel, "Start Time:", startTimeStr, detailFont);

        addDetailRow(detailsPanel, "Duration:", booking.getDuration()/60 + " hrs", detailFont);
        addDetailRow(detailsPanel, "Organiser:", booking.getOrganiserName(), detailFont);
        addDetailRow(detailsPanel, "Attendees:", String.valueOf(booking.getCurrentAttendees()), detailFont);
        addDetailRow(detailsPanel, "Maximum Capacity:", String.valueOf(booking.getMaxCapacity()), detailFont);
        addViewAttendeeButton(detailsPanel, booking);

        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        // Attendees section if available
        if (booking.getAttendees() != null && !booking.getAttendees().isEmpty()) {
            JPanel attendeesPanel = new JPanel(new BorderLayout(5, 5));
            attendeesPanel.setBorder(BorderFactory.createTitledBorder("Attendees"));

            DefaultListModel<String> attendeeModel = new DefaultListModel<>();
            for (AttendeeDto attendee : booking.getAttendees()) {
                attendeeModel.addElement(attendee.getName() + " (" + attendee.getId() + ")");
            }

            JList<String> attendeeList = new JList<>(attendeeModel);
            JScrollPane attendeeScroll = new JScrollPane(attendeeList);
            attendeeScroll.setPreferredSize(new Dimension(400, 150));
            attendeesPanel.add(attendeeScroll, BorderLayout.CENTER);

            contentPanel.add(attendeesPanel, BorderLayout.SOUTH);
        }

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#d4d3b3"));
        JButton closeButton = new JButton("Close");

        // Use your OnClickEventHelper for the close button
        OnClickEventHelper.setOnClickColor(closeButton, new Color(220, 220, 220),
                UIManager.getColor("Button.background"));

        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        // Add button panel to dialog
        if (contentPanel.getComponentCount() > 2) {
            // If we have attendees, add at the very bottom
            JPanel southPanel = new JPanel(new BorderLayout());
            southPanel.add((Component) contentPanel.getComponent(2), BorderLayout.CENTER);
            southPanel.add(buttonPanel, BorderLayout.SOUTH);
            contentPanel.remove(2);
            contentPanel.add(southPanel, BorderLayout.SOUTH);
        } else {
            // Otherwise add directly to main panel
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value, Font font) {
        JLabel labelComponent = new JLabel(label, JLabel.RIGHT);
        labelComponent.setFont(font);
        labelComponent.setForeground(Color.decode("#414833"));

        JLabel valueComponent = new JLabel(value, JLabel.LEFT);
        valueComponent.setFont(font);
        valueComponent.setForeground(Color.decode("#414833"));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    // Method to fetch bookings from the backend
    private ArrayList<BookingDto> fetchBookings(int organiserId) {
        // Define the backend API URL
        String url = "http://localhost:8080/organiser/my-bookings/" + organiserId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<BookingDto[]> response = restTemplate.getForEntity(url, BookingDto[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                // Convert the array to an ArrayList and return it
                BookingDto[] bookingsArray = response.getBody();
                return new ArrayList<>(List.of(bookingsArray));
            } else {
                // Handle non-OK responses
                System.err.println("Failed to fetch bookings. HTTP Status: " + response.getStatusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., connection errors)
            System.err.println("Error fetching bookings: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void addBackButton(UserDto user, JPanel mainPanel) {
        JButton backButton = new JButton("<");
        backButton.setBounds(25, 20, 50, 25);
        backButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 20));
        backButton.setBackground(Color.decode("#b2c590"));
        backButton.setForeground(Color.decode("#73664e"));
        backButton.setBorder(new RoundedBorder(2, Color.decode("#000"), 1));
        
        // Add hover effect
        OnClickEventHelper.setOnClickColor(backButton, Color.decode("#9db580"), Color.decode("#b2c590"));
    
        backButton.addActionListener(e -> {
                new OrganiserHomePage(user);
                dispose(); // Use dispose directly since this class extends JFrame
        });
        
        mainPanel.add(backButton);
    }

    private ArrayList<AttendeeDto> fetchAttendees(Long bookingId, Long organiserId){
        // Define the backend API URL
        String url = "http://localhost:8080/organiser/"+organiserId.intValue()+"/my-bookings/" + bookingId.intValue()+ "/attendees";
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<AttendeeDto[]> response = restTemplate.getForEntity(url, AttendeeDto[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                // Convert the array to an ArrayList and return it
                AttendeeDto[] attendeesArray = response.getBody();
                return new ArrayList<>(List.of(attendeesArray));
            } else {
                // Handle non-OK responses
                System.err.println("Failed to fetch attendees. HTTP Status: " + response.getStatusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., connection errors)
            System.err.println("Error fetching attendees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void addViewAttendeeButton(JPanel panel, BookingDto booking) {
        panel.add(new JLabel());

        JButton viewAttendeesButton = new JButton("View Attendees");
        viewAttendeesButton.setBackground(Color.decode("#bca8e4"));
        viewAttendeesButton.setForeground(Color.decode("#000"));
        viewAttendeesButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        viewAttendeesButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
        viewAttendeesButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(viewAttendeesButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
  
        viewAttendeesButton.addActionListener(e -> {
            ArrayList<AttendeeDto> attendees = fetchAttendees(booking.getId(), booking.getOrganiserId());
            showAttendeeList(attendees);
        });
        panel.add(viewAttendeesButton);
    }

    private void showAttendeeList(ArrayList<AttendeeDto> attendees) {
        JDialog dialog = new JDialog(this, "Attendees List", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (AttendeeDto attendee : attendees) {
            listModel.addElement(attendee.getName());
        }

        JList<String> attendeeList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(attendeeList);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
    
    // // For testing purpose only
    // private ArrayList<BookingDto> fetchBookings(int organiserId) {
    //     // Mock data for testing
    //     ArrayList<BookingDto> mockBookings = new ArrayList<>();
    //     mockBookings.add(new BookingDto(1L, "Event A", 101L, "Room 1", new java.util.Date(), 60, 201L, "John Doe", null,
    //             10, 50));
    //     mockBookings.add(new BookingDto(2L, "Event B", 102L, "Room 2", new java.util.Date(), 120, 202L, "Jane Smith",
    //             null, 20, 100));
    //     mockBookings.add(new BookingDto(3L, "Event C", 103L, "Room 3", new java.util.Date(), 90, 203L, "Alice Johnson",
    //             null, 15, 75));
    //     return mockBookings;
    // }

    // // For testing or standalone usage
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         MyBookingsPage page = new MyBookingsPage(123);
    //         page.setVisible(true);
    //     });
    // }

}