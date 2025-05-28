package org.example;

import org.example.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyTestFramework {

    public static void main(String[] args) throws Exception {
        String className = "org.example.examples.MyExampleTest";

        Class<?> clazz = Class.forName(className);
        int passed = 0, failed = 0;

        System.out.println("Testing class: " + className);

        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("Methods:");
        for (Method m : methods) {
            System.out.println(" - " + m);
        }

        System.out.println("\nRunning @Test annotated static no-arg methods:");

        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                if (m.getParameterCount() == 0 && (m.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
                    try {
                        m.invoke(null); // metoda statică, obiectul null
                        System.out.println("Test passed: " + m.getName());
                        passed++;
                    } catch (Throwable ex) {
                        System.out.println("Test failed: " + m.getName() + " - " + ex.getCause());
                        failed++;
                    }
                } else {
                    System.out.println("Skipping method " + m.getName() + " - must be static no-arg");
                }
            }
        }

        System.out.printf("\nSummary: Passed = %d, Failed = %d%n", passed, failed);
    }
}