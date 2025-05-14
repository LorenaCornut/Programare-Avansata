package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static final int PORT = 8100;
    private boolean running = true;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Hex Game Server started on port " + PORT);

            while (running) {
                System.out.println("Waiting for clients...");
                Socket clientSocket = serverSocket.accept();
                new ClientThread(clientSocket, this).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void stop() {
        this.running = false;
        System.out.println("Server is shutting down...");
    }

    public static void main(String[] args) {
        new GameServer().start();
    }
}
