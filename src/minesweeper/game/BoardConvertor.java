package minesweeper.game;

import minesweeper.game.gui.BoardUnit;
import minesweeper.game.transfer.BoardUnitConvertor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardConvertor implements Serializable {
    private int height;
    private int length;
    private int timeRemaining;
    private List<BoardUnitConvertor> boardDescriber;

    private int totalBombs;
    private int activeBombs;
    private int flagsSet;
    private int closedUnits;

    private String statusMessage;

    private boolean isWin;
    private boolean gameOver;

    public BoardConvertor() {
    }

    public static BoardConvertor convertBoard(GameBoard gameBoard, int timeRemaining) {
        List<BoardUnitConvertor> boardUnitConvertors = new ArrayList<>();
        BoardUnit[][] board = gameBoard.getBoard();
        int length = board.length;
        int height = board[0].length;

        for (BoardUnit[] boardHeight : board) {
            for (BoardUnit boardUnit : boardHeight) {
                BoardUnitConvertor boardUnitConvertor = new BoardUnitConvertor(boardUnit.getXLocation(), boardUnit.getYLocation(), boardUnit.getIconId(), boardUnit.getValue(), boardUnit.isClosed());
                boardUnitConvertors.add(boardUnitConvertor);
            }
        }

        return new BoardConvertor(height, length, timeRemaining, boardUnitConvertors, gameBoard.getTotalBombs(),
                gameBoard.getActiveBombs(), gameBoard.getFlagsSet(), gameBoard.getClosedUnits(), gameBoard.isWin(), gameBoard.isGameOver(), gameBoard.getStatusBar().getStatusMessage());
    }

    public BoardConvertor(int height, int length, int timeRemaining, List<BoardUnitConvertor> board, int totalBombs,
                          int activeBombs, int flagsSet, int closedUnits, boolean isWin, boolean gameOver, String statusMessage) {
        this.height = height;
        this.length = length;
        this.timeRemaining = timeRemaining;
        this.boardDescriber = board;
        this.totalBombs = totalBombs;
        this.activeBombs = activeBombs;
        this.flagsSet = flagsSet;
        this.closedUnits = closedUnits;
        this.isWin = isWin;
        this.gameOver = gameOver;
        this.statusMessage = statusMessage;
    }

    public int getFlagsSet() {
        return flagsSet;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public List<BoardUnitConvertor> getBoardDescriber() {
        return boardDescriber;
    }

    public int getTotalBombs() {
        return totalBombs;
    }

    public int getActiveBombs() {
        return activeBombs;
    }

    public int getClosedUnits() {
        return closedUnits;
    }

    public boolean isWin() {
        return isWin;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
