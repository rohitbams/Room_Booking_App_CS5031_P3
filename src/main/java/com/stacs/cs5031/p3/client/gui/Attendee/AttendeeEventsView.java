package com.stacs.cs5031.p3.client.gui.Attendee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.stacs.cs5031.p3.client.gui.helper_classes.CustomFontLoader;
import com.stacs.cs5031.p3.client.gui.helper_classes.OnClickEventHelper;
import com.stacs.cs5031.p3.client.gui.helper_classes.RoundedBorder;
import com.stacs.cs5031.p3.client.gui.login.LoginGUI;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.UserDto;

public class AttendeeEventsView extends JFrame {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    private UserDto currentUser;
    private JTable availableEventsTable;
    private JTable unavailableEventsTable;
    private DefaultTableModel availableEventsModel;
    private DefaultTableModel unavailableEventsModel;

    public AttendeeEventsView(UserDto user) {
        this.currentUser = user;
        setTitle("Event Booking System - Available Events");
        setSize(1143, 617);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialise UI
        initialiseUI();

        // Load initial data
        loadAvailableEvents();
        loadUnavailableEvents();
    }

    private void initialiseUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.decode("#f4c064"));

        PageElements pageElements = new PageElements(mainPanel);
        pageElements.addAllElements();

        add(mainPanel);
    }

    /**
     * This class is responsible for creating the elements of the Attendee Events view.
     * It contains methods to create and add the elements to the panel.
     */
    private class PageElements {
        private JPanel panel;

        public PageElements(JPanel panel) {
            this.panel = panel;
        }

        public void addAllElements() {
            addTitleLabel();
            addWelcomeLabel();
            addViewMyEventsButton();
            addLogoutButton();
            addAvailableEventsLabel();
            addAvailableEventsTable();
            addUnavailableEventsLabel();
            addUnavailableEventsTable();
        }

        private void addTitleLabel() {
            JLabel titleLabel = new JLabel("Available Events");
            titleLabel.setBounds(20, 20, 300, 30);
            titleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 24));
            panel.add(titleLabel);
        }

        private void addWelcomeLabel() {
            JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
            welcomeLabel.setBounds(20, 60, 300, 20);
            welcomeLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            panel.add(welcomeLabel);
        }

        private void addViewMyEventsButton() {
            JButton viewMyEventsButton = new JButton("View My Events");
            viewMyEventsButton.setBounds(850, 20, 150, 30);
            viewMyEventsButton.setBackground(Color.decode("#bca8e4"));
            viewMyEventsButton.setForeground(Color.decode("#000"));
            viewMyEventsButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            viewMyEventsButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
            viewMyEventsButton.setFocusPainted(false);
            OnClickEventHelper.setOnClickColor(viewMyEventsButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));

            viewMyEventsButton.addActionListener(e -> {
                try {
                    System.out.println("Opening My Events View...");
                    AttendeeMyEventsView myEventsView = new AttendeeMyEventsView(currentUser);
                    myEventsView.setVisible(true);
                    AttendeeEventsView.this.dispose();
                } catch (Exception ex) {
                    System.err.println("Error opening My Events view: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            panel.add(viewMyEventsButton);
        }

        private void addLogoutButton() {
            JButton logoutButton = new JButton("Logout");
            logoutButton.setBounds(1020, 20, 100, 30);
            logoutButton.setBackground(Color.decode("#bca8e4"));
            logoutButton.setForeground(Color.decode("#000"));
            logoutButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            logoutButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
            logoutButton.setFocusPainted(false);
            OnClickEventHelper.setOnClickColor(logoutButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));

            logoutButton.addActionListener(e -> {
                try {
                    System.out.println("Logging out...");
                    AttendeeEventsView.this.dispose();
                    LoginGUI loginScreen = new LoginGUI();
                    loginScreen.setVisible(true);
                } catch (Exception ex) {
                    System.err.println("Error during logout: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            panel.add(logoutButton);
        }

        private void addAvailableEventsLabel() {
            JLabel availableLabel = new JLabel("Available Events:");
            availableLabel.setBounds(20, 100, 200, 20);
            availableLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 16));
            panel.add(availableLabel);
        }

        private void addAvailableEventsTable() {
            String[] availableColumns = {"ID", "Event Name", "Room", "Start Time", "Duration", "Attendees", ""};
            availableEventsModel = new DefaultTableModel(availableColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 6;
                }
            };

            // Create and configure table
            availableEventsTable = new JTable(availableEventsModel);
            availableEventsTable.setRowHeight(35);
            availableEventsTable.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            availableEventsTable.getTableHeader().setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            availableEventsTable.setSelectionBackground(Color.decode("#e0d8f0"));

            availableEventsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());

            availableEventsTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = availableEventsTable.getColumnModel().getColumnIndexAtX(e.getX());
                    int row = e.getY() / availableEventsTable.getRowHeight();

                    if (row < availableEventsTable.getRowCount() && row >= 0 && column == 6) {
                        registerForBooking(row);
                    }
                }
            });

            JScrollPane availableScrollPane = new JScrollPane(availableEventsTable);
            availableScrollPane.setBounds(20, 130, 1100, 200);
            panel.add(availableScrollPane);
        }

        private void addUnavailableEventsLabel() {
            JLabel unavailableLabel = new JLabel("Unavailable Events:");
            unavailableLabel.setBounds(20, 350, 200, 20);
            unavailableLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 16));
            panel.add(unavailableLabel);
        }

        private void addUnavailableEventsTable() {
            String[] unavailableColumns = {"ID", "Event Name", "Room", "Start Time", "Duration", "Attendees", "Status"};
            unavailableEventsModel = new DefaultTableModel(unavailableColumns, 0);

            unavailableEventsTable = new JTable(unavailableEventsModel);
            unavailableEventsTable.setRowHeight(35);
            unavailableEventsTable.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            unavailableEventsTable.getTableHeader().setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            unavailableEventsTable.setSelectionBackground(Color.decode("#e0d8f0"));
            unavailableEventsTable.setEnabled(false);
            unavailableEventsTable.setForeground(Color.GRAY);

            JScrollPane unavailableScrollPane = new JScrollPane(unavailableEventsTable);
            unavailableScrollPane.setBounds(20, 380, 1100, 180);
            panel.add(unavailableScrollPane);
        }
    }

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Register");
            setBackground(Color.decode("#bca8e4"));
            setForeground(Color.decode("#000"));
            setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
            setFocusPainted(false);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private void loadAvailableEvents() {
        try {
            availableEventsModel.setRowCount(0);

            try {
                BookingDto[] events = restTemplate.getForObject(
                        BASE_URL + "/attendees/" + currentUser.getId() + "/available-bookings",
                        BookingDto[].class
                );

                if (events != null) {
                    for (BookingDto event : events) {
                        availableEventsModel.addRow(new Object[]{
                                event.getId(),
                                event.getEventName(),
                                event.getRoomName(),
                                event.getStartTime(),
                                event.getDuration() + " mins",
                                event.getCurrentAttendees() + "/" + event.getMaxCapacity(),
                                ""
                        });
                    }
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading available events: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error in loadAvailableEvents: " + e.getMessage());
        }
    }

    private void loadUnavailableEvents() {
        try {
            unavailableEventsModel.setRowCount(0);
            try {
                BookingDto[] events = restTemplate.getForObject(
                        BASE_URL + "/attendees/" + currentUser.getId() + "/unavailable-bookings",
                        BookingDto[].class
                );

                if (events != null) {
                    for (BookingDto event : events) {
                        unavailableEventsModel.addRow(new Object[]{
                                event.getId(),
                                event.getEventName(),
                                event.getRoomName(),
                                event.getStartTime(),
                                event.getDuration() + " mins",
                                event.getCurrentAttendees() + "/" + event.getMaxCapacity(),
                                "Full"
                        });
                    }
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading unavailable events: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error in loadUnavailableEvents: " + e.getMessage());
        }
    }

    private void registerForBooking(int row) {
        try {
            Long bookingId = (Long) availableEventsModel.getValueAt(row, 0);
            String eventName = (String) availableEventsModel.getValueAt(row, 1);

            boolean confirmed = ConfirmationDialog.showConfirmDialog(
                    this,
                    "Are you sure you want to register for event: " + eventName + "?"
            );

            if (confirmed) {
                try {
                    BookingDto response = restTemplate.postForObject(
                            BASE_URL + "/attendees/" + currentUser.getId() + "/register/" + bookingId,
                            null,
                            BookingDto.class
                    );

                    showCustomMessageDialog(
                            "Successfully registered for event: " + eventName,
                            "Success"
                    );

                    loadAvailableEvents();
                    loadUnavailableEvents();
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            showCustomMessageDialog(
                    "Error registering for event: " + e.getMessage(),
                    "Error");
        }
    }

    private void showCustomMessageDialog(String message, String title) {
        JDialog dialog = new JDialog(this, title, true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#f4c064"));

        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setBounds(20, 20, 360, 60);
        messageLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(messageLabel);

        JButton okButton = new JButton("OK");
        okButton.setBounds(150, 100, 100, 30);
        okButton.setBackground(Color.decode("#bca8e4"));
        okButton.setForeground(Color.decode("#000"));
        okButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        okButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
        okButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(okButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
        okButton.addActionListener(e -> dialog.dispose());
        panel.add(okButton);

        dialog.getContentPane().add(panel);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

//    // For testing purpose only - simplified
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // Create a test user as UserDto
//            UserDto testUser = new UserDto(1, "testuser", "Test User", "ATTENDEE");
//            AttendeeEventsView view = new AttendeeEventsView(testUser);
//            view.setVisible(true);
//        });
//    }
}