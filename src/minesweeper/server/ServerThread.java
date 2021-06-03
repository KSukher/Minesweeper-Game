package minesweeper.server;

import minesweeper.game.BoardConvertor;
import minesweeper.game.enums.Request;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(socket.getInputStream());
            Object obj = input.readObject();
            if (obj instanceof BoardConvertor) {
                BoardConvertor boardConvertor = (BoardConvertor) obj;
                DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
                databaseConnector.saveGame(boardConvertor);
            } else if (obj instanceof String) {
                Request request = Request.valueOf((String) obj);
                switch (request) {
                    case LOAD:
                        handleLoadGames();
                        break;

                    case HIGHSCORE:
                        handleHighScores();
                        break;

                    default:
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException exception) {
            exception.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void handleLoadGames() throws SQLException, IOException {
        System.out.println("Loading saved games");
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        List<byte[]> games = databaseConnector.loadGames();

        if (games == null) {
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(games);
        System.out.println("Converted Games to stream");
        socket.getOutputStream().write(bos.toByteArray());
        System.out.println("Send Games to Server");
        out.close();
    }

    private void handleHighScores() throws SQLException, IOException {
        System.out.println("Loading high scores");
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        List<Long> highscores = databaseConnector.loadHighScores();

        if (highscores == null) {
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(highscores);
        System.out.println("Converted High Scores to stream");
        socket.getOutputStream().write(bos.toByteArray());
        System.out.println("Send High Scores to Server");
        out.close();
    }
}
