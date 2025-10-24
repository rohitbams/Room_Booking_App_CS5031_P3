package com.stacs.cs5031.p3.client.gui.helper_classes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EventNameCellRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1; // Track the hovered row

    public void setHoveredRow(int row) {
        this.hoveredRow = row;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Get the default renderer component
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Set custom font
        label.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));

        // Set text alignment
        label.setHorizontalAlignment(SwingConstants.LEFT);

        // Change font color or background color based on hover
        if (row == hoveredRow) {
            label.setBackground(Color.decode("#606c38")); // Change font color when hovered
            label.setForeground(Color.WHITE);  // Change background color when hovered
        } else {
            label.setBackground(Color.decode("#c2c5aa"));
            label.setForeground(Color.decode("#414833"));
        }

        return label;
    }
}
