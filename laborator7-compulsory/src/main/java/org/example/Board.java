package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Board {
    private final List<String> words = new ArrayList<>();
    private final Map<String, Integer> playerScores = new ConcurrentHashMap<>();

    public synchronized void addWord(Player player, String word) {
        words.add(word);
        int score = calculateScore(word);
        playerScores.merge(player.getName(), score, Integer::sum);
        System.out.println(player.getName() + " submitted word: " + word + " (" + score + " points)");
    }

    private int calculateScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            score += 1;
        }
        return score;
    }

    public synchronized Map<String, Integer> getPlayerScores() {
        return new HashMap<>(playerScores);
    }

    @Override
    public String toString() {
        return words.toString();
    }
}