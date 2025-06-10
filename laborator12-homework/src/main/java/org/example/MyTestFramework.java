package org.example;

import org.example.TestRunner;
import org.example.TestStatistics;

import java.util.List;
import java.util.Scanner;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;

/**
 * The main entry point of the application.
 * <p>
 * Coordinates compilation (if source directory is given), class name collection,
 * class analysis, and test execution.
 */

public class MyTestFramework {

    /**
     * Main method that runs the entire pipeline:
     * <ul>
     *     <li>Reads input (class file, folder, or JAR)</li>
     *     <li>Compiles source code if needed</li>
     *     <li>Collects class names from input</li>
     *     <li>Analyzes each class and runs tests</li>
     *     <li>Prints test statistics</li>
     * </ul>
     *
     * @param args command-line arguments (not used)
     * @throws Exception if compilation, class loading, or test execution fails
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        ClassCollector classCollector = new ClassCollector();
        ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        TestRunner testRunner = new TestRunner();
        TestStatistics statistics = new TestStatistics();

        // Collect class names from the input (folder, .class file, or .jar)
        List<String> classNames = classCollector.collectClassNames(input);

        File inputFile = new File(input);
        URL[] urls = {inputFile.toURI().toURL()};
        URLClassLoader loader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        for (String className : classNames) {
            Class<?> cls = loader.loadClass(className);
            classAnalyzer.printPrototype(cls); // Print class prototype
            testRunner.runTests(cls, statistics); // Run tests on the class
        }

        statistics.printStats(); // Print test statistics
    }
}