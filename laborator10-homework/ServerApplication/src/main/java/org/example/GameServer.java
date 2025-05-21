package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    public static final int PORT = 8100;
    private volatile boolean running = true;
    private final ConcurrentMap<String, HexGame> activeGames = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Player> connectedPlayers = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public synchronized String createGame(Player player) {
        String gameId = UUID.randomUUID().toString().substring(0, 8);
        activeGames.put(gameId, new HexGame(gameId, player));
        return "Game created with ID: " + gameId;
    }

    public synchronized String joinGame(String gameId, Player player) {
        HexGame game = activeGames.get(gameId);
        if (game == null) return "Game not found";
        if (!game.isWaitingForPlayer()) return "Game is already full";
        return game.joinGame(player);
    }

    public synchronized String makeMove(String gameId, Player player, int x, int y) {
        HexGame game = activeGames.get(gameId);
        if (game == null) return "Game not found";
        if (game.isGameOver()) return "Game already ended";
        if (!game.isPlayerInGame(player)) return "You are not part of this game";
        return game.makeMove(player, x, y);
    }

    public synchronized String listGames() {
        StringBuilder sb = new StringBuilder("Available games:\n");
        activeGames.forEach((id, game) -> {
            if (game.isWaitingForPlayer()) {
                sb.append(id).append("\n");
            }
        });
        return sb.toString();
    }

    public synchronized void registerPlayer(String name, PrintWriter out) {
        connectedPlayers.put(name, new Player(name, out));
    }

    public synchronized Player getPlayer(String name) {
        return connectedPlayers.get(name);
    }

    public synchronized HexGame getPlayerGame(Player player) {
        return activeGames.values().stream()
                .filter(game -> game.isPlayerInGame(player))
                .findFirst()
                .orElse(null);
    }

    public synchronized void removeGame(String gameId) {
        activeGames.remove(gameId);
    }

    public void shutdown() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Hex Game Server started on port " + PORT);
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executorService.execute(new ClientThread(clientSocket, this));
                } catch (SocketException e) {
                    if (running) System.err.println("Accept error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    public static void main(String[] args) {
        new GameServer().start();
    }
}