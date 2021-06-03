package minesweeper.game.transfer;

import java.io.Serializable;

public class BoardUnitConvertor implements Serializable {
    private final int x;
    private final int y;
    //External Value
    private final long iconId;
    //Internal Value
    private final String value;
    private final boolean closed;

    public BoardUnitConvertor(int x, int y, Long iconId, String value, boolean closed) {
        this.x = x;
        this.y = y;
        this.iconId = iconId;
        this.value = value;
        this.closed = closed;
    }

    public long getIconId() {
        return iconId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getValue() {
        return value;
    }

    public boolean isClosed() {
        return closed;
    }

}
