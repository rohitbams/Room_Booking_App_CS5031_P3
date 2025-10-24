package com.stacs.cs5031.p3.client.gui.login;

import javax.swing.*;
import java.awt.Color;

import com.stacs.cs5031.p3.client.gui.Attendee.AttendeeEventsView;
import com.stacs.cs5031.p3.client.gui.organiser.OrganiserHomePage;
import com.stacs.cs5031.p3.server.dto.LoginRequest;
import com.stacs.cs5031.p3.server.dto.RegistrationRequest;
import com.stacs.cs5031.p3.server.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.stacs.cs5031.p3.client.gui.helper_classes.CustomFontLoader;
import com.stacs.cs5031.p3.client.gui.helper_classes.OnClickEventHelper;
import com.stacs.cs5031.p3.client.gui.helper_classes.RoundedBorder;

public class LoginGUI extends JFrame {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    // Colors
    private final Color BACKGROUND_COLOR = Color.decode("#f4c064");
    private final Color BUTTON_COLOR = Color.decode("#bca8e4");
    private final Color BUTTON_TEXT_COLOR = Color.decode("#000");
    private final Color BUTTON_PRESS_COLOR = Color.decode("#7c6f97");
    private final Color BORDER_COLOR = Color.decode("#3d364a");

    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDto currentUser;

    public LoginGUI() {
        setTitle("Room Booking System - Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiseUI();
    }

    private void initialiseUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Room Booking System");
        titleLabel.setBounds(125, 30, 250, 40);
        titleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 24));
        mainPanel.add(titleLabel);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 100, 100, 20);
        usernameLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 125, 300, 30);
        usernameField.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        usernameField.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        mainPanel.add(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 165, 100, 20);
        passwordLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 190, 300, 30);
        passwordField.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        passwordField.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        mainPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 240, 100, 30);
        loginButton.setBackground(BUTTON_COLOR);
        loginButton.setForeground(BUTTON_TEXT_COLOR);
        loginButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        loginButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        loginButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(loginButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(260, 240, 100, 30);
        registerButton.setBackground(BUTTON_COLOR);
        registerButton.setForeground(BUTTON_TEXT_COLOR);
        registerButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        registerButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        registerButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(registerButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);
        registerButton.addActionListener(e -> showRegistrationDialog());
        mainPanel.add(registerButton);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showCustomMessageDialog("Please enter username and password", "Error");
            return;
        }

        try {
            // Create login request DTO
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

            String loginUrl = BASE_URL + "/users/login";
            System.out.println("Attempting to connect to: " + loginUrl);

            UserDto response = restTemplate.postForObject(
                    loginUrl,
                    request,
                    UserDto.class
            );

            if (response != null) {
                currentUser = response;
                openAppropriateView();
            } else {
                showCustomMessageDialog("Login failed. Invalid credentials.", "Error");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            showCustomMessageDialog("Login failed: " + e.getMessage(), "Error");
        }
    }


//    private UserDto createTestUser(Integer id, String username, String name, String role) {
//        return new UserDto(id, username, name, role);
//    }

    private void openAppropriateView() {
        dispose();

        String role = currentUser.getRole();
        if ("ATTENDEE".equals(role)) {
            AttendeeEventsView attendeeGUI = new AttendeeEventsView(currentUser);
            attendeeGUI.setVisible(true);
        } else if ("ORGANISER".equals(role)) {
            new OrganiserHomePage(currentUser);
        } else if ("ADMIN".equals(role)) {
            // custom message for admin view not yet implemented
            showCustomMessageDialog("Admin view not yet implemented", "Information");
        }
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register", true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(BACKGROUND_COLOR);

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 20, 100, 20);
        nameLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(50, 45, 300, 30);
        nameField.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        nameField.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        panel.add(nameField);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 85, 100, 20);
        usernameLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 110, 300, 30);
        usernameField.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        usernameField.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        panel.add(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 150, 100, 20);
        passwordLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 175, 300, 30);
        passwordField.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        passwordField.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        panel.add(passwordField);

        // Role selection
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 215, 100, 20);
        roleLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(roleLabel);

        JRadioButton attendeeRadio = new JRadioButton("Attendee");
        attendeeRadio.setBounds(50, 240, 120, 30);
        attendeeRadio.setSelected(true);
        attendeeRadio.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        attendeeRadio.setBackground(BACKGROUND_COLOR);

        JRadioButton organiserRadio = new JRadioButton("Organiser");
        organiserRadio.setBounds(180, 240, 120, 30);
        organiserRadio.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        organiserRadio.setBackground(BACKGROUND_COLOR);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(attendeeRadio);
        roleGroup.add(organiserRadio);

        panel.add(attendeeRadio);
        panel.add(organiserRadio);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(140, 290, 120, 30);
        registerButton.setBackground(BUTTON_COLOR);
        registerButton.setForeground(BUTTON_TEXT_COLOR);
        registerButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        registerButton.setBorder(new RoundedBorder(4, BORDER_COLOR, 1));
        registerButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(registerButton, BUTTON_PRESS_COLOR, BUTTON_COLOR);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = attendeeRadio.isSelected() ? "ATTENDEE" : "ORGANISER";

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showCustomMessageDialog("Please fill in all fields", "Error");
                return;
            }

            try {
                // create registration request DTO
                RegistrationRequest registrationRequest = new RegistrationRequest(
                        name, username, password, role);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<RegistrationRequest> request = new HttpEntity<>(registrationRequest, headers);

                UserDto response = restTemplate.postForObject(
                        BASE_URL + "/users/register",
                        request,
                        UserDto.class
                );

                showCustomMessageDialog("Registration successful! Please login.", "Success");
                dialog.dispose();
            } catch (Exception ex) {
                dialog.dispose();
            }
        });

        panel.add(registerButton);

        dialog.getContentPane().add(panel);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}