package net.amoriconi;

import java.net.ServerSocket;

public class Main {
    private final static int SERVER_PORT = 10000;

    public static void main(String[] args) {
        ServerThread server = new ServerThread(SERVER_PORT);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
