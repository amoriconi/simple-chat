package net.amoriconi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ServerThread implements Runnable {
    private ServerSocket socket;
    private CopyOnWriteArrayList<ClientThread> clients;
    private LinkedBlockingQueue<Message> messaqeQueue;

    public ServerThread(int port) {
        try {
            socket = new ServerSocket(port);
            clients = new CopyOnWriteArrayList<ClientThread>();
            messaqeQueue = new LinkedBlockingQueue<Message>();

            Thread queueThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Message message = messaqeQueue.take();
                            broadcastMessage(message);
                        } catch (InterruptedException ie) {
                            System.out.println("Queue thread interrupted!");
                        }
                    }
                }
            });

            queueThread.start();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create server socket!");
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSock = socket.accept();
                ClientThread clientThread = new ClientThread(clientSock, messaqeQueue);
                clients.add(clientThread);

                Thread thread = new Thread(clientThread);
                thread.start();
            } catch (IOException e) {
                System.out.println("Error!" + e.getMessage());
            }
        }
    }

    public synchronized void broadcastMessage(final Message message) {
        clients.stream().filter(new Predicate<ClientThread>() {
            @Override
            public boolean test(ClientThread clientThread) {
                return clientThread.hashCode() != message.getId();
            }
        }).forEach(new Consumer<ClientThread>() {
            @Override
            public void accept(ClientThread clientThread) {
                try {
                    clientThread.getWriter().println(message.getMessage());
                } catch (Throwable e) {
                    System.out.println("Something wrong...Cannot broadcast message to client! " + e.getMessage());
                }
            }
        });
    }
}
