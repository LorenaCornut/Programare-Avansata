package org.example;

import java.io.*;
import java.net.*;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 8100;
    private volatile boolean running = true;

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            new Thread(() -> {
                try {
                    String response;
                    while (running && (response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    if (running) System.err.println("Connection error: " + e.getMessage());
                }
            }).start();

            String input;
            while (running && (input = console.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                    out.println("exit");
                    break;
                }
                out.println(input);
            }
        } catch (ConnectException e) {
            System.err.println("Cannot connect to server. Is it running?");
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new GameClient().start();
    }
}