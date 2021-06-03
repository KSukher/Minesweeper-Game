package minesweeper.game;

import minesweeper.game.enums.Icons;
import minesweeper.game.gui.BoardUnit;
import minesweeper.game.gui.StatusBar;
import minesweeper.game.gui.TimerPanel;
import minesweeper.game.listener.BoardUnitMouseListener;
import minesweeper.game.transfer.BoardUnitConvertor;

import javax.swing.*;

import static minesweeper.game.settings.Settings.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel {
    private final BoardUnit[][] board;
    private int flagsSet = 0;
    private int totalBombs = 0;
    private int activeBombs = 0;
    private int closedUnits = 0;

    private StatusBar statusBar;
    private boolean gameOver;

    private static GameBoard gameBoard;

    private boolean win = false;

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    public static GameBoard initialize(int size) {
        gameBoard = new GameBoard(size, size);
        return gameBoard;
    }

    public static GameBoard initialize(BoardConvertor boardConvertor) {
        gameBoard = new GameBoard(boardConvertor);
        return gameBoard;
    }

    private GameBoard(BoardConvertor boardConvertor) {
        super(new GridLayout(SIZE, SIZE));
        gameOver = boardConvertor.isGameOver();
        win = boardConvertor.isWin();
        
        int x = boardConvertor.getLength();
        int y = boardConvertor.getHeight();

        board = new BoardUnit[x][y];
        populateBoard(boardConvertor);

        statusBar = new StatusBar(boardConvertor.getStatusMessage());
    }

    private GameBoard(int x, int y) {
        super(new GridLayout(SIZE, SIZE));
        board = new BoardUnit[x][y];

        populateBoard();

        statusBar = new StatusBar(activeBombs);
    }

    private void populateBoard(BoardConvertor boardConvertor) {
        List<BoardUnitConvertor> boardUnitConvertors = boardConvertor.getBoardDescriber();
        boardUnitConvertors.forEach(boardUnitConvertor -> {
            int xLocation = boardUnitConvertor.getX();
            int yLocation = boardUnitConvertor.getY();
            BoardUnit boardUnit = new BoardUnit(xLocation, yLocation, boardUnitConvertor);

            if (!boardUnit.isBomb()) {
                List<BoardUnit> neighbors = getNeighbors(boardUnit);
                boardUnit.setNeighbors(neighbors);
            }

            boardUnit.addMouseListener(new BoardUnitMouseListener(boardUnit));

            board[xLocation][yLocation] = boardUnit;
            add(boardUnit);
        });

        this.totalBombs = boardConvertor.getTotalBombs();
        this.activeBombs = boardConvertor.getActiveBombs();
        this.flagsSet = boardConvertor.getFlagsSet();
        this.closedUnits = boardConvertor.getClosedUnits();
    }

    private void populateBoard() {
        for (int length = 0; length < board.length; length++) {
            BoardUnit[] boardHeight = board[length];
            for (int height = 0; height < boardHeight.length; height++) {
                BoardUnit boardUnit = new BoardUnit(length, height);
                incrementClosedUnits();
                board[length][height] = boardUnit;
                if (boardUnit.isBomb()) {
                    incrementActiveBombs();
                }
            }
        }
        totalBombs = activeBombs;

        for (BoardUnit[] boardUnits : board) {
            for (BoardUnit boardUnit : boardUnits) {
                if (boardUnit.isBomb()) {
                    boardUnit.setValue("B");
                } else {
                    List<BoardUnit> neighbors = getNeighbors(boardUnit);
                    boardUnit.setNeighbors(neighbors);
                    int neighborBombCount = 0;
                    for (BoardUnit neighbor : neighbors) {
                        if (neighbor.isBomb()) {
                            neighborBombCount++;
                        }
                    }

                    boardUnit.setValue(String.valueOf(neighborBombCount));
                }

                boardUnit.addMouseListener(new BoardUnitMouseListener(boardUnit));
                add(boardUnit);
            }
        }
    }

    public void open(BoardUnit boardUnit) {
        boardUnit.setClosed(false);

        if (Icons.FLAG.getId().equals(boardUnit.getIconId())) {
            decrementFlagsSet();
            statusBar.updateStatus(String.valueOf(GameBoard.getGameBoard().getTotalBombs() - GameBoard.getGameBoard().getFlagsSet()));
        } else {
            decrementClosedUnits();
        }

        boardUnit.reveal();

        if ("0".equals(boardUnit.getValue())) {
            openNeighbors(boardUnit.getNeighbors());
        } else if ("B".equals(boardUnit.getValue())) {
            gameBoard.complete(false);
        }

    }


    public void openNeighbors(List<BoardUnit> neighbors) {
        for (BoardUnit boardUnit : neighbors) {
            if (boardUnit.isClosed()) {
                open(boardUnit);
            }
        }
    }

    public List<BoardUnit> getNeighbors(BoardUnit boardUnit) {
        int x = boardUnit.getXLocation();
        int y = boardUnit.getYLocation();
        return getNeighbors(x, y);
    }

    public List<BoardUnit> getNeighbors(int x, int y) {
        return findNeighbors(x, y);
    }

    private List<BoardUnit> findNeighbors(int x, int y) {
        List<BoardUnit> neighbors = new ArrayList<>();

        addNeighbor(getUnit(x, y + 1), neighbors);
        addNeighbor(getUnit(x, y - 1), neighbors);
        addNeighbor(getUnit(x + 1, y), neighbors);
        addNeighbor(getUnit(x + 1, y + 1), neighbors);
        addNeighbor(getUnit(x + 1, y - 1), neighbors);
        addNeighbor(getUnit(x - 1, y), neighbors);
        addNeighbor(getUnit(x - 1, y + 1), neighbors);
        addNeighbor(getUnit(x - 1, y - 1), neighbors);

        return neighbors;
    }

    private BoardUnit getUnit(int x, int y) {
        try {
            return board[x][y];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    private void addNeighbor(BoardUnit boardUnit, List<BoardUnit> neighbors) {
        if (boardUnit != null) {
            neighbors.add(boardUnit);
        }
    }

    public void complete(boolean win) {
        gameOver = true;
        showIncorrectFlags();
        TimerPanel.stop();
        if (win) {
            this.win = true;
            statusBar.updateStatus(GAME_WON);
        } else {
            showBombs();
            statusBar.updateStatus(GAME_LOST);
        }
    }

    public void showIncorrectFlags() {
        for (BoardUnit[] boardHeight : board) {
            for (BoardUnit boardUnit : boardHeight) {
                if (boardUnit.getIconId().equals(Icons.FLAG.getId()) && !boardUnit.isBomb()) {
                    boardUnit.setIcon(Icons.CROSSED_FLAG);
                }
            }
        }
    }

    public void showBombs() {
        for (BoardUnit[] boardHeight : board) {
            for (BoardUnit boardUnit : boardHeight) {
                if (boardUnit.isBomb() && !boardUnit.getIconId().equals(Icons.FLAG.getId())) {
                    boardUnit.setIcon(Icons.BOMB);
                    boardUnit.setClosed(false);
                }
            }
        }
    }

    public int getClosedUnits() {
        return closedUnits;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public BoardUnit[][] getBoard() {
        return board;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getTotalBombs() {
        return totalBombs;
    }

    public boolean isWin() {
        return win;
    }

    public int getActiveBombs() {
        return activeBombs;
    }

    public void decrementClosedUnits() {
        this.closedUnits--;
    }

    public void incrementClosedUnits() {
        this.closedUnits++;
    }

    public void decrementActiveBombs() {
        this.activeBombs--;
    }

    public void incrementActiveBombs() {
        this.activeBombs++;
    }

    public int getFlagsSet() {
        return flagsSet;
    }

    public void incrementFlagsSet() {
        this.flagsSet++;
    }

    public void decrementFlagsSet() {
        this.flagsSet--;
    }

    public boolean checkComplete() {
        return activeBombs == 0 && closedUnits == 0;
    }

    public void handleLeftClick(BoardUnit boardUnit) {
        open(boardUnit);
    }

    public void handleRightClick(BoardUnit boardUnit) {
        if (boardUnit.getIconId().equals(Icons.FLAG.getId())) {
            removeFlag(boardUnit);

            GameBoard.getGameBoard().getStatusBar().updateStatus(String.valueOf(totalBombs - flagsSet));
        } else if (boardUnit.getIconId().equals(Icons.CLOSED.getId())) {
            if (flagsSet == totalBombs) {
                return;
            }

            addFlag(boardUnit);

            if (flagsSet == totalBombs) {
                statusBar.updateStatus(NO_MORE_FLAGS);
            } else {
                GameBoard.getGameBoard().getStatusBar().updateStatus(String.valueOf(totalBombs - flagsSet));
            }


        }
    }

    private void addFlag(BoardUnit boardUnit) {
        boardUnit.setIcon(Icons.FLAG);
        incrementFlagsSet();
        decrementClosedUnits();

        if (boardUnit.isBomb()) {
            decrementActiveBombs();
        }
    }

    private void removeFlag(BoardUnit boardUnit) {
        boardUnit.setIcon(Icons.CLOSED);
        decrementFlagsSet();
        incrementClosedUnits();

        if (boardUnit.isBomb()) {
            incrementActiveBombs();
        }
    }
}

