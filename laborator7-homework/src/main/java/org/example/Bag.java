package org.example;

import java.util.*;

public class Bag {
    private final Queue<Tile> tiles = new LinkedList<>();

    public Bag() {
        Random random = new Random();
        for (char c = 'a'; c <= 'z'; c++) {
            int points = random.nextInt(10) + 1;
            for (int i = 0; i < 10; i++) {
                tiles.add(new Tile(c, points));
            }
        }
        Collections.shuffle((List<?>) tiles);
    }

    public synchronized List<Tile> extractTiles(int howMany) {
        List<Tile> extracted = new ArrayList<>();
        for (int i = 0; i < howMany && !tiles.isEmpty(); i++) {
            extracted.add(tiles.poll());
        }
        return extracted;
    }

    public synchronized boolean isEmpty() {
        return tiles.isEmpty();
    }
}