package org.example.examples;

import org.example.annotations.Test;

public class MyExampleTest {

    @Test
    public static void test1() {
        System.out.println("Test 1 OK");
    }

    @Test
    public static void test3(int x){
        System.out.println("Test 3 OK \"" + x + "\"");
    }

    @Test
    public static void test4(String s) {
        System.out.println("Test 4 OK \"" + s + "\"");
    }

    @Test
    public static void test2() {
        System.out.println("Test 2 OK");
    }
}