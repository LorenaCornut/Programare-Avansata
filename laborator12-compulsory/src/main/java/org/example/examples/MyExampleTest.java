package org.example.examples;

import org.example.annotations.Test;

public class MyExampleTest {

    @Test
    public static void testSuccess() {
        System.out.println("testSuccess executed");
    }

    @Test
    public static void testFailure() {
        throw new RuntimeException("Intentional failure");
    }

    public static void notATest() {
        System.out.println("This method is not a test.");
    }
}