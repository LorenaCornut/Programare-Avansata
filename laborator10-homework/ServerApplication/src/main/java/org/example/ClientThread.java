package org.example;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private Player player;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Welcome to Hex Game Server! Enter your name:");
            String name = in.readLine();
            server.registerPlayer(name, out);
            this.player = server.getPlayer(name);
            out.println("Hello " + name + "! Commands: create, join <id>, move <id> <x> <y>, games, status, exit");

            new Thread(this::sendMessages).start();

            String input;
            while ((input = in.readLine()) != null) {
                try {
                    if (input.equalsIgnoreCase("exit")) {
                        messageQueue.put("Goodbye!");
                        break;
                    }
                    processCommand(input);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void sendMessages() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = messageQueue.take();
                player.notify(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processCommand(String command) throws InterruptedException {
        String[] parts = command.split(" ");
        String response = switch (parts[0].toLowerCase()) {
            case "help" -> "Commands: create, join <id>, move <id> <x> <y>, games, status, exit";
            case "create" -> server.createGame(player);
            case "join" -> parts.length == 2 ? server.joinGame(parts[1], player) : "Usage: join <gameID>";
            case "move" -> {
                if (parts.length != 4) yield "Usage: move <gameID> <x> <y>";
                try {
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);
                    yield server.makeMove(parts[1], player, x, y);
                } catch (NumberFormatException e) {
                    yield "Invalid coordinates";
                }
            }
            case "games" -> server.listGames();
            case "status" -> {
                HexGame game = server.getPlayerGame(player);
                yield game != null ? game.getBoardState() : "Not in any game";
            }
            default -> "Unknown command. Type 'help' for commands";
        };
        messageQueue.put(response);
    }

    private void cleanup() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}