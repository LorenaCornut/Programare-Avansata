package org.example;

import java.util.*;

public class Bag {
    private final Queue<Tile> tiles = new LinkedList<>();

    public Bag() {
        Random random = new Random();
        List<Tile> tempList = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            int points = random.nextInt(10) + 1;
            for (int i = 0; i < 10; i++) {
                tempList.add(new Tile(c, points));
            }
        }
        Collections.shuffle(tempList);
        tiles.addAll(tempList);
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