package minesweeper.game.gui;

import minesweeper.game.BoardConvertor;
import minesweeper.game.GameBoard;
import minesweeper.game.enums.Request;
import minesweeper.game.settings.Settings;

import javax.swing.*;

import static minesweeper.game.settings.Settings.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MineSweeper extends JFrame {
    private static GameBoard GAME_BOARD;
    private static MineSweeper mineSweeper;

    public static MineSweeper getInstance() {
        return mineSweeper;
    }

    public static void initialize() {
        mineSweeper = new MineSweeper("Minesweeper", TIME);
    }

    public static void load(BoardConvertor boardConvertor) {
        close(mineSweeper);
        mineSweeper = new MineSweeper("Minesweeper", boardConvertor);
    }

    private TimerPanel timerPanel;

    private MineSweeper(String title, int time) {
        super(title);
        initMineSweeper(time);
    }

    private MineSweeper(String title, BoardConvertor boardConvertor) {
        super(title);
        initMineSweeper(boardConvertor);
    }

    public void initMineSweeper(int time) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Settings.WIDTH, Settings.HEIGHT));

        newGame(time);
        initMenu();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    private void initMineSweeper(BoardConvertor boardConvertor) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Settings.WIDTH, Settings.HEIGHT));

        loadGame(boardConvertor);
        initMenu();

        pack();
        setVisible(true);
    }

    public void newGame(int time) {
        timerPanel = TimerPanel.initialize(time);
        GAME_BOARD = GameBoard.initialize(SIZE);
        timerPanel.start();

        addComponents();
    }

    public void loadGame(BoardConvertor boardConvertor) {
        timerPanel = TimerPanel.initialize(boardConvertor.getTimeRemaining());
        GAME_BOARD = GameBoard.initialize(boardConvertor);

        if (!GAME_BOARD.isGameOver()) {
            timerPanel.start();
        }

        addComponents();

    }

    public void addComponents() {
        add(timerPanel, BorderLayout.NORTH);
        add(GAME_BOARD, BorderLayout.CENTER);
        add(GAME_BOARD.getStatusBar(), BorderLayout.SOUTH);

    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenu loadMenu = new JMenu("Load");
        loadMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                load(loadMenu);
            }
        });

        menu.add(loadMenu);


        JMenu highscoreMenu = new JMenu("High Score");

        highscoreMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadHighScores(highscoreMenu);
            }
        });

        menu.add(highscoreMenu);


        JMenuItem newItem = addMenuItem("New", e -> newGame(), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        JMenuItem saveItem = addMenuItem("Save", e -> save(), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        JMenuItem exitItem = addMenuItem("Exit", e -> exit(), KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));


        menu.add(newItem);
        menu.add(saveItem);
        menu.add(exitItem);


        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void newGame() {
        close(this);
        initialize();
    }

    private void exit() {
        close(this);
        System.exit(0);
    }

    public void save() {
        Socket socket = null;
        try {
            System.out.println("Establishing connection to " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);
            System.out.println("Established connection to " + HOST + ":" + PORT);
            System.out.println("Converting Board to stream");
            BoardConvertor boardConvertor = BoardConvertor.convertBoard(GAME_BOARD, timerPanel.getElapsed());
            sendToServer(socket, boardConvertor);
            System.out.println("Saved board to sever.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("Successfully closed socket connection.");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

    private void loadHighScores(JMenu highscoreMenu) {
        highscoreMenu.removeAll();
        Socket socket = null;
        List<Long> highscores = new ArrayList<>();
        try {
            System.out.println("Establishing connection to " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);
            System.out.println("Established connection to " + HOST + ":" + PORT);
            System.out.println("Requesting High Scores From Server");

            sendToServer(socket, Request.HIGHSCORE.name());

            System.out.println("Received High Scores From Server");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Object obj = input.readObject();

            if (obj instanceof List) {
                List<Long> scores = (List<Long>) obj;
                highscores.addAll(scores);
            }

            for (Long highscore : highscores) {
                highscoreMenu.add(new JMenuItem(String.valueOf(highscore)));
            }

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("Successfully closed socket connection.");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void load(JMenu loadMenu) {
        loadMenu.removeAll();
        Socket socket = null;
        List<BoardConvertor> savedGames = new ArrayList<>();
        try {
            System.out.println("Establishing connection to " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);
            System.out.println("Established connection to " + HOST + ":" + PORT);
            System.out.println("Requesting Saved Games From Server");
            sendToServer(socket, Request.LOAD.name());
            System.out.println("Received Saved Games From Server");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Object obj = input.readObject();

            if (obj instanceof List) {
                List<byte[]> games = (List<byte[]>) obj;
                for (byte[] game : games) {
                    ByteArrayInputStream in = new ByteArrayInputStream(game);
                    ObjectInputStream is = new ObjectInputStream(in);
                    BoardConvertor convertor = (BoardConvertor) is.readObject();
                    savedGames.add(convertor);
                }
            }

            int gameCount = 1;
            for (BoardConvertor convertor : savedGames) {
                SavedGame game = new SavedGame("Game " + gameCount);
                game.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        load(convertor);
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
                });
                loadMenu.add(game);
                gameCount++;
            }

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("Successfully closed socket connection.");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void sendToServer(Socket socket, Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(object);
        System.out.println("Converted Object to stream");
        socket.getOutputStream().write(bos.toByteArray());
        System.out.println("Send Object to Server");
        out.close();
    }

    private JMenuItem addMenuItem(String name, ActionListener listener, KeyStroke keyStroke) {
        JMenuItem item = new JMenuItem(name);
        item.setFocusable(true);
        item.setAccelerator(keyStroke);
        item.addActionListener(listener);
        return item;
    }


    private static void close(MineSweeper mineSweeper) {
        mineSweeper.setVisible(false);
        mineSweeper.dispose();
    }
}