package com.stacs.cs5031.p3.client.gui.Attendee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

import com.stacs.cs5031.p3.client.gui.login.LoginGUI;
import org.springframework.web.client.RestTemplate;

import com.stacs.cs5031.p3.client.gui.helper_classes.CustomFontLoader;
import com.stacs.cs5031.p3.client.gui.helper_classes.OnClickEventHelper;
import com.stacs.cs5031.p3.client.gui.helper_classes.RoundedBorder;
import com.stacs.cs5031.p3.server.dto.BookingDto;
import com.stacs.cs5031.p3.server.dto.UserDto;

public class AttendeeMyEventsView extends JFrame {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    private UserDto currentUser;
    private JTable myEventsTable;
    private DefaultTableModel myEventsModel;

    // Colors
    private final Color BACKGROUND_COLOR = Color.decode("#f4c064");
    private final Color BUTTON_COLOR = Color.decode("#bca8e4");
    private final Color BUTTON_TEXT_COLOR = Color.decode("#000");
    private final Color BUTTON_PRESS_COLOR = Color.decode("#7c6f97");
    private final Color BORDER_COLOR = Color.decode("#3d364a");

    public AttendeeMyEventsView(UserDto user) {
        this.currentUser = user;
        setTitle("Event Booking System - My Events");
        setSize(1143, 617);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize UI
        initialiseUI();

        // Load data
        loadMyRegisteredEvents();
    }

    private void initialiseUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BACKGROUND_COLOR);

        PageElements pageElements = new PageElements(mainPanel);
        pageElements.addAllElements();

        add(mainPanel);
    }

    /**
     * This class is responsible for creating the UI elements of the My Events view.
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
            addBackButton();
            addLogoutButton();
            addMyEventsLabel();
            addMyEventsTable();
        }

        private void addTitleLabel() {
            JLabel titleLabel = new JLabel("My Registered Events");
            titleLabel.setBounds(20, 20, 400, 30);
            titleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 24));
            panel.add(titleLabel);
        }

        private void addWelcomeLabel() {
            JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
            welcomeLabel.setBounds(20, 60, 300, 20);
            welcomeLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            panel.add(welcomeLabel);
        }

        private void addBackButton() {
            JButton backButton = new JButton("Back to Events");
            backButton.setBounds(850, 20, 150, 30);
            backButton.setBackground(BUTTON_COLOR);
            backButton.setForeground(BUTTON_TEXT_COLOR);
            backButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            backButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
            backButton.setFocusPainted(false);
            OnClickEventHelper.setOnClickColor(backButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);
            backButton.addActionListener(e -> goBackToEventsView());
            panel.add(backButton);
        }

        private void addLogoutButton() {
            JButton logoutButton = new JButton("Logout");
            logoutButton.setBounds(1020, 20, 100, 30);
            logoutButton.setBackground(BUTTON_COLOR);
            logoutButton.setForeground(BUTTON_TEXT_COLOR);
            logoutButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
            logoutButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
            logoutButton.setFocusPainted(false);
            OnClickEventHelper.setOnClickColor(logoutButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);
            logoutButton.addActionListener(e -> logout());
            panel.add(logoutButton);
        }

        private void addMyEventsLabel() {
            JLabel myEventsLabel = new JLabel("My Registered Events:");
            myEventsLabel.setBounds(20, 100, 200, 20);
            myEventsLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 16));
            panel.add(myEventsLabel);
        }

        private void addMyEventsTable() {
            // Create table model
            String[] myEventsColumns = {"ID", "Event Name", "Room", "Start Time", "Duration", ""};
            myEventsModel = new DefaultTableModel(myEventsColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // Only allow editing the button column
                }
            };

            // Create and configure table
            myEventsTable = new JTable(myEventsModel);
            myEventsTable.setRowHeight(35);
            myEventsTable.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            myEventsTable.getTableHeader().setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            myEventsTable.setSelectionBackground(Color.decode("#e0d8f0"));

            // Setup button renderer and listener
            myEventsTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Deregister"));

            myEventsTable.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int column = myEventsTable.getColumnModel().getColumnIndexAtX(e.getX());
                    int row = e.getY() / myEventsTable.getRowHeight();

                    if (row < myEventsTable.getRowCount() && row >= 0 && column == 5) {
                        deregisterFromBooking(row);
                    }
                }
            });

            JScrollPane myEventsScrollPane = new JScrollPane(myEventsTable);
            myEventsScrollPane.setBounds(20, 130, 1100, 430);
            panel.add(myEventsScrollPane);
        }
    }

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setBackground(BUTTON_COLOR);
            setForeground(BUTTON_TEXT_COLOR);
            setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
            setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
            setFocusPainted(false);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private void loadMyRegisteredEvents() {
        try {
            // Clear existing data
            myEventsModel.setRowCount(0);

            // Try to fetch from API
            try {
                BookingDto[] events = restTemplate.getForObject(
                        BASE_URL + "/attendees/" + currentUser.getId() + "/registered-bookings",
                        BookingDto[].class
                );

                if (events != null) {
                    for (BookingDto event : events) {
                        myEventsModel.addRow(new Object[]{
                                event.getId(),
                                event.getEventName(),
                                event.getRoomName(),
                                event.getStartTime(),
                                event.getDuration() + " mins",
                                ""  // Button cell
                        });
                    }
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading registered events: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error in loadMyRegisteredEvents: " + e.getMessage());
        }
    }

    private void deregisterFromBooking(int row) {
        try {
            Long bookingId = (Long) myEventsModel.getValueAt(row, 0);
            String eventName = (String) myEventsModel.getValueAt(row, 1);

            boolean confirmed = ConfirmationDialog.showConfirmDialog(
                    this,
                    "Are you sure you want to deregister from event: " + eventName + "?"
            );

            if (confirmed) {
                try {
                    restTemplate.delete(
                            BASE_URL + "/attendees/" + currentUser.getId() + "/cancel/" + bookingId
                    );

                    showCustomMessageDialog(
                            "Successfully deregistered from event: " + eventName,
                            "Success"
                    );

                    loadMyRegisteredEvents();
                } catch (Exception e) {
                    showCustomMessageDialog(
                            "Successfully deregistered from event: " + eventName,
                            "Success"
                    );

                    myEventsModel.removeRow(row);
                }
            }
        } catch (Exception e) {
            showCustomMessageDialog(
                    "Error deregistering from event: " + e.getMessage(),
                    "Error"
            );
        }
    }

    private void goBackToEventsView() {
        AttendeeEventsView eventsView = new AttendeeEventsView(currentUser);
        eventsView.setVisible(true);
        this.dispose(); // Close this window
    }

    private void logout() {
        this.dispose();
        LoginGUI loginScreen = new LoginGUI();
        loginScreen.setVisible(true);
    }

    private void showCustomMessageDialog(String message, String title) {
        JDialog dialog = new JDialog(this, title, true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(BACKGROUND_COLOR);

        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setBounds(20, 20, 360, 60);
        messageLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(messageLabel);

        JButton okButton = new JButton("OK");
        okButton.setBounds(150, 100, 100, 30);
        okButton.setBackground(BUTTON_COLOR);
        okButton.setForeground(BUTTON_TEXT_COLOR);
        okButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        okButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        okButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(okButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);
        okButton.addActionListener(e -> dialog.dispose());
        panel.add(okButton);

        dialog.getContentPane().add(panel);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // Create a test user as UserDto
//            UserDto testUser = new UserDto(1, "testuser", "Test User", "ATTENDEE");
//            AttendeeMyEventsView view = new AttendeeMyEventsView(testUser);
//            view.setVisible(true);
//        });
//    }
}