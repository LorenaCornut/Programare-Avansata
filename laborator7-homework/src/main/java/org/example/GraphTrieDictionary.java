package org.example;

import org.jgrapht.graph.*;
import java.io.*;
import java.util.*;

public class GraphTrieDictionary {
    private record TrieNode(String text, boolean isWord) {}

    private final DirectedPseudograph<TrieNode, DefaultEdge> graph =
            new DirectedPseudograph<>(DefaultEdge.class);
    private final TrieNode root = new TrieNode("", false);

    public GraphTrieDictionary() {
        graph.addVertex(root);
        loadWordsFromFile();
    }

    private void loadWordsFromFile() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("words.txt");
        if (input == null) {
            throw new RuntimeException("words.txt not found in resources");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            reader.lines()
                    .filter(line -> !line.isEmpty())
                    .map(String::toLowerCase)
                    .forEach(this::addWord);
        } catch (IOException e) {
            throw new RuntimeException("Error reading words.txt: " + e.getMessage());
        }
    }

    public void addWord(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode next = findChild(current, c);

            if (next == null) {
                String text = current.text() + c;
                boolean isFinal = (i == word.length() - 1);
                next = new TrieNode(text, isFinal);
                graph.addVertex(next);
                graph.addEdge(current, next);
            } else if (i == word.length() - 1) {
                graph.removeVertex(next);
                next = new TrieNode(next.text(), true);
                graph.addVertex(next);
                graph.addEdge(current, next);
            }
            current = next;
        }
    }

    private TrieNode findChild(TrieNode parent, char c) {
        for (DefaultEdge edge : graph.outgoingEdgesOf(parent)) {
            TrieNode child = graph.getEdgeTarget(edge);
            if (child.text().length() > parent.text().length()
                    && child.text().charAt(parent.text().length()) == c) {
                return child;
            }
        }
        return null;
    }

    public boolean isWord(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            current = findChild(current, word.charAt(i));
            if (current == null) return false;
        }
        return current.isWord();
    }
}