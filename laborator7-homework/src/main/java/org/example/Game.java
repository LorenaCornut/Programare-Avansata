package org.example;

import java.util.*;
import java.util.concurrent.locks.*;

public class Game {
    private final Bag bag = new Bag();
    private final Board board = new Board();
    private final GraphTrieDictionary dictionary = new GraphTrieDictionary();
    private final List<Player> players = new ArrayList<>();
    private volatile boolean gameRunning = true;
    private int currentPlayerIndex = 0;
    private final Lock turnLock = new ReentrantLock();
    private static final long TIME_LIMIT = 120000; // 2 minute

    public void addPlayer(Player player) {
        players.add(player);
        player.setGame(this);
    }

    public void play() {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (gameRunning && !shouldEndGame()) {
                try {
                    Thread.sleep(1000);
                    long elapsed = System.currentTimeMillis() - startTime;
                    System.out.printf("[Timekeeper] Time: %d.%ds%n", elapsed/1000, elapsed%1000/100);
                    if (elapsed > TIME_LIMIT) endGame();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        players.forEach(player -> new Thread(player).start());
    }

    public synchronized void endGame() {
        gameRunning = false;
        System.out.println("Game over! Final scores:");
        System.out.println(board.getPlayerScores());
        System.out.println("Winner: " + board.getWinner());
    }

    public void nextTurn() {
        turnLock.lock();
        try {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } finally {
            turnLock.unlock();
        }
    }

    public boolean isMyTurn(Player player) {
        turnLock.lock();
        try {
            return gameRunning && players.get(currentPlayerIndex) == player;
        } finally {
            turnLock.unlock();
        }
    }

    public boolean shouldEndGame() {
        return bag.isEmpty() && players.stream().allMatch(p -> p.tiles.isEmpty());
    }

    public Bag getBag() { return bag; }
    public Board getBoard() { return board; }
    public GraphTrieDictionary getDictionary() { return dictionary; }
    public boolean isGameRunning() { return gameRunning; }
    public Lock getTurnLock() { return turnLock; }

    public static void main(String[] args) {
        Game game = new Game();
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        game.play();
    }
}