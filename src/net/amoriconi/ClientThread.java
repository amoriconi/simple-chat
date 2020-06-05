package net.amoriconi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final LinkedBlockingQueue<Message> queue;

    public ClientThread(Socket socket, LinkedBlockingQueue<Message> queue) throws IOException {
        this.socket = socket;
        this.queue = queue;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public PrintWriter getWriter() {
        return out;
    }

    @Override
    public void run() {
        System.out.println("New client accepted!");

        try {
            while (!socket.isClosed()) {
                if (in.ready()) {
                    String line = in.readLine();
                    System.out.println("Message from client: " + line);

                    queue.put(new Message(hashCode(), line));
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error in client thread! " + e.getMessage());
        }
    }
}
