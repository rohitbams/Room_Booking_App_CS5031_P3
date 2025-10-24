package com.stacs.cs5031.p3.client.gui.Attendee;

import com.stacs.cs5031.p3.client.gui.helper_classes.CustomFontLoader;
import com.stacs.cs5031.p3.client.gui.helper_classes.OnClickEventHelper;
import com.stacs.cs5031.p3.client.gui.helper_classes.RoundedBorder;

import javax.swing.*;
import java.awt.*;

class ConfirmationDialog extends JDialog {
    private boolean confirmed = false;

    public ConfirmationDialog(JFrame parent, String message) {
        super(parent, "Confirm", true);

        // Set up the dialog
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.decode("#f4c064"));

        // Message
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setBounds(20, 20, 360, 60);
        messageLabel.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        panel.add(messageLabel);

        // Yes button
        JButton yesButton = new JButton("Yes");
        yesButton.setBounds(90, 100, 100, 30);
        yesButton.setBackground(Color.decode("#bca8e4"));
        yesButton.setForeground(Color.decode("#000"));
        yesButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        yesButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
        yesButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(yesButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        panel.add(yesButton);

        // No button
        JButton noButton = new JButton("No");
        noButton.setBounds(210, 100, 100, 30);
        noButton.setBackground(Color.decode("#bca8e4"));
        noButton.setForeground(Color.decode("#000"));
        noButton.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
        noButton.setBorder(new RoundedBorder(4, Color.decode("#3d364a"), 1));
        noButton.setFocusPainted(false);
        OnClickEventHelper.setOnClickColor(noButton, Color.decode("#7c6f97"), Color.decode("#bca8e4"));
        noButton.addActionListener(e -> dispose());
        panel.add(noButton);

        getContentPane().add(panel);
        setSize(400, 180);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public static boolean showConfirmDialog(JFrame parent, String message) {
        ConfirmationDialog dialog = new ConfirmationDialog(parent, message);
        dialog.setVisible(true);
        return dialog.isConfirmed();
    }
}