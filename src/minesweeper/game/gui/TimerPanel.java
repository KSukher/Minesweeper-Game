package minesweeper.game.gui;

import javax.swing.*;

import minesweeper.game.GameBoard;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerPanel extends JPanel {
    private static TimerPanel timerPanel;

    public static void stop() {
        timerPanel.timeHandler.stopTimer();
    }

    public void start() {
        timeHandler.startTimer();
        timeHandler.startActionTimer();
    }

    public static TimerPanel initialize(int time) {
        if (timerPanel == null) {
            timerPanel = new TimerPanel(time);
        } else {
            timerPanel.timeHandler.stopTimer();
            timerPanel = new TimerPanel(time);
        }
        return timerPanel;
    }

    private final TimeHandler timeHandler;

    private TimerPanel(int time) {
        TimerLabel timerLabel = new TimerLabel(time);
        timeHandler = new TimeHandler(timerLabel, time);
        add(timerLabel);
    }

    public Integer getElapsed() {
        return timeHandler.getUpdateListener().getElapsed();
    }


    public static class TimeHandler {
        private final Timer timer;
        private final javax.swing.Timer actionTimer;
        private final UpdateListener updateListener;

        public TimeHandler(TimerLabel timerLabel, int elapsed) {
            timer = new Timer();

            updateListener = new UpdateListener(elapsed, timerLabel);

            actionTimer = new javax.swing.Timer((int) TimeUnit.SECONDS.toMillis(elapsed), e -> {
                GameBoard.getGameBoard().complete(false);
                stopActionTimer();
                stopTimer();
            });
        }

        public UpdateListener getUpdateListener() {
            return updateListener;
        }

        public void startActionTimer() {
            actionTimer.start();
        }

        public void stopActionTimer() {
            actionTimer.stop();
        }

        public void startTimer() {
            timer.schedule(updateListener, 0, 1000);
        }

        public void stopTimer() {
            timer.cancel();
        }
    }

    public static class UpdateListener extends TimerTask {
        private final TimerLabel timerLabel;
        private int elapsed;

        public UpdateListener(int elapsed, TimerLabel timerLabel) {
            this.elapsed = elapsed;
            this.timerLabel = timerLabel;
        }


        @Override
        public void run() {
            elapsed = --elapsed;
            timerLabel.updateTime(elapsed);
        }

        public int getElapsed() {
            return elapsed;
        }
    }
}
