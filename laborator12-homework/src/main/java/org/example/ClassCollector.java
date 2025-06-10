package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Responsible for collecting fully-qualified class names from different input sources.
 * Supported sources include:
 * <ul>
 *   <li>A directory containing compiled .class files</li>
 *   <li>A .jar archive</li>
 *   <li>A single .class file</li>
 * </ul>
 */
public class ClassCollector {

    /**
     * Collects all fully-qualified class names from the given input path.
     *
     * @param input the path to a directory, a JAR file, or a single .class file
     * @return a list of fully-qualified class names
     * @throws IOException if file access fails
     * @throws IllegalArgumentException if the input type is unsupported
     */
    public List<String> collectClassNames(String input) throws IOException {
        List<String> names = new ArrayList<>();
        Path path = Paths.get(input);

        if (Files.isDirectory(path)) {
            collectFromDirectory(path, names);
        } else if (input.endsWith(".jar")) {
            collectFromJar(input, names);
        } else if (input.endsWith(".class")) {
            collectFromSingleClass(path, names);
        } else {
            throw new IllegalArgumentException("Unknown input type: " + input);
        }

        return names;
    }

    /**
     * Collects class names from a directory tree by finding all .class files.
     *
     * @param path  the root directory
     * @param names the list to collect class names into
     * @throws IOException if directory traversal fails
     */
    private void collectFromDirectory(Path path, List<String> names) throws IOException {
        Files.walk(path)
                .filter(p -> p.toString().endsWith(".class"))
                .forEach(p -> names.add(pathToClassName(path, p)));
    }

    /**
     * Collects class names from a .jar file by reading its entries.
     *
     * @param jarPath the path to the .jar file
     * @param names   the list to collect class names into
     * @throws IOException if the jar file cannot be read
     */
    private void collectFromJar(String jarPath, List<String> names) throws IOException {
        try (JarFile jar = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replaceAll("\\.class$", "");
                    names.add(className);
                }
            }
        }
    }

    /**
     * Collects a class name from a single .class file.
     *
     * @param path  the path to the .class file
     * @param names the list to collect class names into
     */
    private void collectFromSingleClass(Path path, List<String> names) {
        names.add(pathToClassName(path.getParent(), path));
    }

    /**
     * Converts a path to a .class file into its fully-qualified class name,
     * using the given root as the base package path.
     *
     * @param root      the root path representing the base package directory
     * @param classFile the path to the .class file
     * @return the fully-qualified class name (e.g. com.example.MyClass)
     */
    private String pathToClassName(Path root, Path classFile) {
        String relative = root.relativize(classFile).toString();
        return relative.replace(File.separatorChar, '.')
                .replaceAll("\\.class$", "");
    }
}
