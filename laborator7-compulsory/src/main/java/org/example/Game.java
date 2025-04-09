package org.example;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {
    private final Bag bag = new Bag();
    private final Board board = new Board();
    private final MockDictionary dictionary = new MockDictionary();
    private final List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
        player.setGame(this);
    }

    public void play() {
        for (Player player : players) {
            new Thread(player).start();
        }
    }

    public Bag getBag() {
        return bag;
    }

    public Board getBoard() {
        return board;
    }

    public MockDictionary getDictionary() {
        return dictionary;
    }

    public static void main(String args[]) {
        Game game = new Game();
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        game.addPlayer(new Player("Player 3"));
        game.play();
    }
}