package minesweeper.game.gui;

import javax.swing.*;


public class StatusBar extends JPanel {
    private String statusMessage;
    private final JLabel status;

    public StatusBar(int numBombs) {
        this.statusMessage = String.valueOf(numBombs);
        this.status = new JLabel(this.statusMessage);
        add(status);
    }

    public StatusBar(String statusMessage) {
        this.statusMessage = statusMessage;
        this.status = new JLabel(this.statusMessage);
        add(status);
    }

    public void updateStatus(String message) {
        statusMessage = message;
        status.setText(statusMessage);
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
