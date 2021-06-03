package minesweeper.game.listener;

import minesweeper.game.GameBoard;
import minesweeper.game.gui.BoardUnit;
import minesweeper.game.gui.MineSweeper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardUnitMouseListener implements MouseListener {
    private final BoardUnit boardUnit;

    public BoardUnitMouseListener(BoardUnit boardUnit) {
        this.boardUnit = boardUnit;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameBoard gameBoard = GameBoard.getGameBoard();
        if (gameBoard.isGameOver() || !boardUnit.isClosed()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            gameBoard.handleLeftClick(boardUnit);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            gameBoard.handleRightClick(boardUnit);
        }

        if (gameBoard.checkComplete()) {
            gameBoard.complete(true);
            MineSweeper.getInstance().save();
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}