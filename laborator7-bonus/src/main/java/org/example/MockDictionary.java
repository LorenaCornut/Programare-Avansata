package org.example;
import java.util.*;

public class MockDictionary {
    private final Set<String> words = new HashSet<>();

    public MockDictionary() {
        words.add("a");
        words.add("at");
        words.add("ate");
        words.add("tea");
        words.add("eat");
        words.add("tae");
        words.add("bat");
        words.add("bet");
        words.add("cat");
        words.add("dog");
        words.add("god");
        words.add("log");
        words.add("go");
        words.add("to");
        words.add("be");
        words.add("me");
        words.add("hi");
        words.add("it");
        words.add("in");
        words.add("on");
        words.add("no");
        words.add("an");
        words.add("ant");
        words.add("tan");
        words.add("can");
        words.add("car");
        words.add("arc");
        words.add("rat");
        words.add("tar");
        words.add("art");
    }

    public boolean isWord(String str) {
        return words.contains(str.toLowerCase());
    }
}