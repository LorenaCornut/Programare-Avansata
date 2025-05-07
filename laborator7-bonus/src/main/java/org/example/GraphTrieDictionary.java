package org.example;

import org.example.PrefixTrieDictionary;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GraphTrieDictionary {
    private final PrefixTrieDictionary dictionary;
    private final String dictionaryPath;

    public GraphTrieDictionary(boolean useLargeDictionary) {
        if (useLargeDictionary) {
            List<String> largeDict = generateLargeDictionary(1_000_000);
            this.dictionaryPath = createTempDictionaryFile(largeDict);
        } else {
            this.dictionaryPath = getDictionaryFilePath();
        }
        this.dictionary = new PrefixTrieDictionary(dictionaryPath);
    }

    private String getDictionaryFilePath() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("words.txt");
        if (input == null) {
            throw new RuntimeException("words.txt not found in resources");
        }

        try {
            File tempFile = File.createTempFile("words", ".txt");
            tempFile.deleteOnExit();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Error creating temp file: " + e.getMessage());
        }
    }

    private List<String> generateLargeDictionary(int wordCount) {
        List<String> dictionary = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < wordCount; i++) {
            int length = 3 + random.nextInt(7);
            StringBuilder word = new StringBuilder();

            for (int j = 0; j < length; j++) {
                word.append((char)('a' + random.nextInt(26)));
            }

            dictionary.add(word.toString());
        }

        return dictionary;
    }

    private String createTempDictionaryFile(List<String> words) {
        try {
            File tempFile = File.createTempFile("large_dict", ".txt");
            tempFile.deleteOnExit();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                for (String word : words) {
                    writer.write(word);
                    writer.newLine();
                }
            }
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Error creating large dictionary file: " + e.getMessage());
        }
    }

    public boolean isWord(String word) {
        long startTime = System.nanoTime();
        boolean trieResult = dictionary.lookupUsingTrie(word).contains(word.toLowerCase());
        long trieTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        boolean parallelResult = dictionary.lookupParallel(word).contains(word.toLowerCase());
        long parallelTime = System.nanoTime() - startTime;

        System.out.printf("Word check: '%s' - Trie: %d ns, Parallel: %d ns%n",
                word, trieTime, parallelTime);

        return trieResult;
    }

    public List<String> findWordsWithPrefix(String prefix) {
        return dictionary.lookupUsingTrie(prefix.toLowerCase());
    }

    public void runLargeDictionaryPerformanceTest() {
        System.out.println("\nLarge Dictionary Performance Test");
        System.out.println("--------------------------------");

        System.out.println("Warming up...");
        for (int i = 0; i < 100; i++) {
            dictionary.isWord("test");
        }

        int[] prefixLengths = {1, 2, 3, 4, 5};
        int testIterations = 100;

        for (int length : prefixLengths) {
            long trieTotalTime = 0;
            long parallelTotalTime = 0;
            int trieResultCount = 0;
            int parallelResultCount = 0;

            for (int i = 0; i < testIterations; i++) {
                String prefix = generateRandomPrefix(length);

                long start = System.nanoTime();
                List<String> trieResults = dictionary.lookupUsingTrie(prefix);
                trieTotalTime += System.nanoTime() - start;
                trieResultCount += trieResults.size();

                start = System.nanoTime();
                List<String> parallelResults = dictionary.lookupParallel(prefix);
                parallelTotalTime += System.nanoTime() - start;
                parallelResultCount += parallelResults.size();
            }

            double trieAvgTime = trieTotalTime / (double) testIterations;
            double parallelAvgTime = parallelTotalTime / (double) testIterations;
            double trieAvgResults = trieResultCount / (double) testIterations;
            double parallelAvgResults = parallelResultCount / (double) testIterations;

            System.out.printf("\nPrefix length %d:\n", length);
            System.out.printf("Trie search: %.2f ns avg, %.1f results avg\n", trieAvgTime, trieAvgResults);
            System.out.printf("Parallel search: %.2f ns avg, %.1f results avg\n", parallelAvgTime, parallelAvgResults);
            System.out.printf("Speed ratio: %.2fx\n", parallelAvgTime / trieAvgTime);
        }
    }

    private String generateRandomPrefix(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char)('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
}