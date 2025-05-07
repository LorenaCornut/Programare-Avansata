package org.example;

import java.util.*;
import java.util.stream.*;

public class Player implements Runnable {
    private final String name;
    private Game game;
    public List<Tile> tiles = new ArrayList<>();
    private static final int MAX_TILES = 7;
    private final Random random = new Random();

    public Player(String name) {
        this.name = name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        while (game.isGameRunning() && !game.shouldEndGame()) {
            synchronized (game) {
                while (!game.isMyTurn(this) && game.isGameRunning() && !game.shouldEndGame()) {
                    try {
                        game.wait();
                        if (!game.isGameRunning() || game.shouldEndGame()) return;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (!game.isGameRunning() || game.shouldEndGame()) break;

                boolean success = submitWord();

                game.nextTurn();
                game.notifyAll();

                try {
                    Thread.sleep(random.nextInt(300) + 200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private boolean submitWord() {
        refillTiles();
        if (tiles.isEmpty()) {
            return false;
        }
        Optional<String> bestWord = findBestWord();

        if (bestWord.isPresent()) {
            String word = bestWord.get();
            int score = calculateWordScore(word);
            game.getBoard().addWord(this, word + " (Score: " + score + ")");
            removeUsedTiles(word);
            return true;
        } else {
            tiles.clear();
            return false;
        }
    }

    private void refillTiles() {
        if (tiles.size() < MAX_TILES) {
            int needed = MAX_TILES - tiles.size();
            List<Tile> extracted = game.getBag().extractTiles(needed);
            tiles.addAll(extracted);
        }
    }

    private Optional<String> findBestWord() {
        return generateAllPossibleWords().stream()
                .filter(word -> game.getDictionary().isWord(word))
                .max(Comparator.comparingInt(String::length))
                .map(String::toLowerCase);
    }

    private List<String> generateAllPossibleWords() {
        List<String> words = new ArrayList<>();
        for (int len = Math.min(tiles.size(), MAX_TILES); len >= 1; len--) {
            generateWordsOfLength(tiles, len, 0, new StringBuilder(), words);
        }
        return words;
    }

    private void generateWordsOfLength(List<Tile> tiles, int length, int start, StringBuilder current, List<String> result) {
        if (current.length() == length) {
            result.add(current.toString());
            return;
        }

        for (int i = start; i < tiles.size(); i++) {
            current.append(tiles.get(i).getLetter());
            generateWordsOfLength(tiles, length, i + 1, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

    private int calculateWordScore(String word) {
        Map<Character, Integer> letterCount = new HashMap<>();
        word.chars().forEach(c -> letterCount.merge((char)c, 1, Integer::sum));

        int score = 0;
        for (Tile tile : tiles) {
            char c = tile.getLetter();
            if (letterCount.containsKey(c) && letterCount.get(c) > 0) {
                score += tile.getPoints();
                letterCount.put(c, letterCount.get(c) - 1);
            }
        }
        return score;
    }

    private void removeUsedTiles(String word) {
        List<Character> charsToRemove = new ArrayList<>();
        for (char c : word.toCharArray()) {
            charsToRemove.add(c);
        }

        Iterator<Tile> iterator = tiles.iterator();
        while (iterator.hasNext() && !charsToRemove.isEmpty()) {
            Tile tile = iterator.next();
            if (charsToRemove.contains(tile.getLetter())) {
                iterator.remove();
                charsToRemove.remove((Character) tile.getLetter());
            }
        }
    }
}