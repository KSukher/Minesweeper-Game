package minesweeper.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import minesweeper.game.BoardConvertor;

public class DatabaseConnector {
    private final Connection connection;

    private static DatabaseConnector INSTANCE;

    public static DatabaseConnector getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConnector();
        }
        return INSTANCE;
    }

    private DatabaseConnector() throws SQLException {
    	connection= DriverManager.getConnection("jdbc:sqlite::memory:");
        String sql = "Create TABLE IF NOT EXISTS MINESWEEPER_GAMES (SAVED_GAME BLOB NOT NULL, SCORE INTEGER NOT NULL, IS_WIN BOOLEAN NOT NULL)";
        System.out.println(sql);
        connection.createStatement().execute(sql);
    }


    public List<byte[]> loadGames() {
        try {
            List<byte[]> games = new ArrayList<>();
            String sql = "SELECT SAVED_GAME FROM MINESWEEPER_GAMES";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                games.add(resultSet.getBytes("SAVED_GAME"));
            }
            return games;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void saveGame(BoardConvertor boardConvertor) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(boardConvertor);

            byte[] game = bos.toByteArray();

            String sql = "INSERT INTO MINESWEEPER_GAMES(SAVED_GAME, SCORE, IS_WIN) VALUES (?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setBytes(1, game);
            stm.setLong(2, boardConvertor.getTimeRemaining());
            stm.setBoolean(3, boardConvertor.isWin());
            System.out.println(sql);

            stm.execute();
            stm.close();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Long> loadHighScores() {
        try {
            List<Long> highscores = new ArrayList<>();
            String sql = "SELECT SCORE FROM MINESWEEPER_GAMES WHERE IS_WIN = true ORDER BY SCORE DESC LIMIT 5 ";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                highscores.add(resultSet.getLong("SCORE"));
            }
            return highscores;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
