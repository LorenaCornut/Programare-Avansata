package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Board {
    private final Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private final Set<String> usedWords = new HashSet<>();

    public synchronized void addWord(Player player, String word) {
        if (usedWords.contains(word)) {
            System.out.println(player.getName() + " tried to submit duplicate word: " + word);
            return;
        }
        usedWords.add(word);
        int score = calculateScore(word);
        playerScores.merge(player.getName(), score, Integer::sum);
        System.out.println(player.getName() + " submitted word: " + word + " (" + score + " points)");
    }

    private int calculateScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            score += (c == 'z' || c == 'q') ? 3 : 1;
        }
        return score;
    }

    public synchronized Map<String, Integer> getPlayerScores() {
        return new HashMap<>(playerScores);
    }

    public synchronized String getWinner() {
        return playerScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " with " + entry.getValue() + " points")
                .orElse("No winner");
    }

    @Override
    public String toString() {
        return usedWords.toString();
    }
}