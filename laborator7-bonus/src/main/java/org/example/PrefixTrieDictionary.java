package org.example;

import org.graph4j.GraphBuilder;
import org.graph4j.Digraph;
import org.graph4j.traversal.DFSIterator;
import org.graph4j.traversal.SearchNode;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class PrefixTrieDictionary {
    private record TrieNode(String text, boolean isWord) {}

    private final Digraph<TrieNode, Character> trie;
    private final Set<String> wordSet;

    public PrefixTrieDictionary(String filePath) {
        wordSet = new ConcurrentSkipListSet<>();
        trie = GraphBuilder.empty().buildDigraph();
        trie.addLabeledVertex(0, new TrieNode("", false));

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                word = word.trim().toLowerCase();
                if (!word.isEmpty()) {
                    wordSet.add(word);
                    addWord(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void addWord(String word) {
        int v = 0;
        StringBuilder currentText = new StringBuilder();

        for (char c : word.toCharArray()) {
            currentText.append(c);
            int u = findChild(v, c);
            if (u == -1) {
                TrieNode node = new TrieNode(currentText.toString(), false);
                u = trie.addLabeledVertex(node);
                trie.addLabeledEdge(v, u, c);
            }
            v = u;
        }

        TrieNode finalNode = trie.getVertexLabel(v);
        trie.setVertexLabel(v, new TrieNode(finalNode.text(), true));
    }

    private int findChild(int v, char c) {
        var it = trie.neighborIterator(v);
        while (it.hasNext()) {
            int u = it.next();
            if (c == trie.getEdgeLabel(v, u)) {
                return u;
            }
        }
        return -1;
    }

    public boolean isWord(String word) {
        return wordSet.contains(word);
    }

    public List<String> lookupUsingTrie(String prefix) {
        int node = findWord(prefix);
        if (node == -1) {
            return List.of();
        }

        List<String> results = new ArrayList<>();

        for (var it = new DFSIterator(trie, node); it.hasNext(); ) {
            SearchNode searchNode = it.next();
            TrieNode trieNode = trie.getVertexLabel(searchNode.vertex());

            if (!trieNode.text().startsWith(prefix)) break;

            if (trieNode.isWord()) {
                results.add(trieNode.text());
            }
        }

        return results;
    }

    public List<String> lookupParallel(String prefix) {
        return wordSet.parallelStream()
                .filter(w -> w.startsWith(prefix))
                .collect(Collectors.toList());
    }

    private int findWord(String prefix) {
        int v = 0;
        for (char c : prefix.toCharArray()) {
            int u = findChild(v, c);
            if (u == -1) return -1;
            v = u;
        }
        return v;
    }
}