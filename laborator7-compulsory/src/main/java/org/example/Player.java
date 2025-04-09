package org.example;

import java.util.*;

public class Player implements Runnable {
    private final String name;
    private Game game;
    private boolean running = true;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private boolean submitWord() {
        List<Tile> extracted = game.getBag().extractTiles(7);
        if (extracted.isEmpty()) {
            return false;
        }
        String letters = "";
        for (Tile tile : extracted) {
            letters += tile.getLetter();
        }
        for (int i = 0; i < letters.length(); i++) {
            for (int j = i+1; j < letters.length(); j++) {
                String word2 = "" + letters.charAt(i) + letters.charAt(j);
                if (game.getDictionary().isWord(word2)) {
                    game.getBoard().addWord(this, word2);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return true;
                }

                for (int k = j+1; k < letters.length(); k++) {
                    String word3 = word2 + letters.charAt(k);
                    if (game.getDictionary().isWord(word3)) {
                        game.getBoard().addWord(this, word3);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void run() {
        while (running && !game.getBag().isEmpty()) {
            boolean success = submitWord();
            if (!success && game.getBag().isEmpty()) {
                running = false;
            }
        }
        Map<String, Integer> scores = game.getBoard().getPlayerScores();
        int score = scores.getOrDefault(name, 0);
        System.out.println(name + " final score: " + score);
    }
}