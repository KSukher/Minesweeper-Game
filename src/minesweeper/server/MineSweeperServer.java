package minesweeper.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import minesweeper.game.settings.Settings;

public class MineSweeperServer {
    public static void main(String[] args) throws IOException {
    	System.out.println("Starting server");
    	
        ServerSocket serverSocket = null;
        Socket socket;
        try {
            serverSocket = new ServerSocket(Settings.PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (true) {
            socket = serverSocket.accept();
            new ServerThread(socket).start();
        }

    }


}
