package minesweeper.game.gui;

import minesweeper.game.enums.Icons;
import minesweeper.game.transfer.BoardUnitConvertor;

import javax.swing.*;

import static minesweeper.game.settings.Settings.BOMB_PROBABILITY;

import java.util.List;
import java.util.Random;

public class BoardUnit extends JButton {
    private final static Random randomGenerator = new Random();

    private Long iconId;
    private boolean closed = true;
    private String value;

    private List<BoardUnit> neighbors;

    private final boolean isBomb;
    private final int xLocation;
    private final int yLocation;

    public BoardUnit(int xLocation, int yLocation) {
        setIcon(Icons.CLOSED);
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.isBomb = shouldBeBomb();
    }

    public BoardUnit(int x, int y, BoardUnitConvertor boardUnitConvertor) {
        this.xLocation = x;
        this.yLocation = y;
        this.closed = boardUnitConvertor.isClosed();
        this.iconId = boardUnitConvertor.getIconId();
        this.value = boardUnitConvertor.getValue();
        this.isBomb = "B".equals(this.value);

        if (!closed) {
            reveal();
        } else if (iconId.equals(Icons.FLAG.getId())) {
            setIcon(Icons.FLAG);
        } else if (iconId.equals(Icons.CROSSED_FLAG.getId())) {
            setIcon(Icons.CROSSED_FLAG);
        } else {
            setIcon(Icons.CLOSED);
        }
    }

    public void reveal() {
        switch (value) {
            case "B":
                setIcon(Icons.BOMB);
                break;
            case "0":
                setIcon(Icons.OPEN);
                break;
            case "1":
                setIcon(Icons.ONE);
                break;
            case "2":
                setIcon(Icons.TWO);
                break;
            case "3":
                setIcon(Icons.THREE);
                break;
            case "4":
                setIcon(Icons.FOUR);
                break;
            case "5":
                setIcon(Icons.FIVE);
                break;
            case "6":
                setIcon(Icons.SIX);
                break;
            case "7":
                setIcon(Icons.SEVEN);
                break;
            case "8":
                setIcon(Icons.EIGHT);
                break;
        }
    }


    public void setIcon(Icons icon) {
        this.iconId = icon.getId();
        setIcon(icon.getIcon());
    }

    public Long getIconId() {
        return iconId;
    }

    public List<BoardUnit> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<BoardUnit> neighbors) {
        this.neighbors = neighbors;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    private boolean shouldBeBomb() {
        int generatedNumber = randomGenerator.nextInt(101);
        return generatedNumber > (100 - BOMB_PROBABILITY);
    }

}
