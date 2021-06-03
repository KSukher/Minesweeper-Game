package minesweeper.game.enums;

import javax.swing.*;

import minesweeper.game.settings.Settings;

public enum Icons {
    ONE(Settings.ONE_ICON, 1L),
    TWO(Settings.TWO_ICON, 2L),
    THREE(Settings.THREE_ICON, 3L),
    FOUR(Settings.FOUR_ICON, 4L),
    FIVE(Settings.FIVE_ICON, 5L),
    SIX(Settings.SIX_ICON, 6L),
    SEVEN(Settings.SEVEN_ICON, 7L),
    EIGHT(Settings.EIGHT_ICON, 8L),
    CLOSED(Settings.CLOSED_ICON, 9L),
    OPEN(Settings.OPEN_ICON, 10L),
    FLAG(Settings.FLAG_ICON, 11L),
    CROSSED_FLAG(Settings.CROSSED_FLAG_ICON, 12L),
    BOMB(Settings.BOMB_ICON, 13L);

    private final ImageIcon icon;
    private final Long id;

    Icons(ImageIcon icon, Long id) {
        this.icon = icon;
        this.id = id;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public Long getId() {
        return id;
    }
}
