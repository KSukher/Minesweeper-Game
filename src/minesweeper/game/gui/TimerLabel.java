package minesweeper.game.gui;

import javax.swing.*;

public class TimerLabel extends JLabel {

    private final static String TIME_LEFT = "Time Left: ";

    public TimerLabel(int time) {
        super(TIME_LEFT + time);
    }

    public void updateTime(int time) {
        setText(TIME_LEFT + time);
    }

}