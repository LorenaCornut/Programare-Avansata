/*package org.example;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");

            // Import real data first
            System.out.println("Importing real capital cities...");
            DataImport.importCapitals();

            // Generate fake data
            System.out.println("Generating fake cities and relationships...");
            FakeDataGenerator.generateFakeData();

            // Find biconnected components
            System.out.println("Finding biconnected components...");
            BiconnectedComponentsFinder finder = new BiconnectedComponentsFinder();
            List<Set<Integer>> components = finder.findBiconnectedComponents();

            System.out.println("\nFound " + components.size() + " biconnected components:");
            for (int i = 0; i < components.size(); i++) {
                System.out.println("Component " + (i + 1) + ": " + components.get(i).size() + " cities");
            }

            // Visualize the results
            System.out.println("Launching visualization...");
            CityMapVisualizer.setBiconnectedComponents(components);
            javafx.application.Application.launch(CityMapVisualizer.class, args);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection();
            System.out.println("\nApplication finished.");
        }
    }
}*/

package org.example;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");

            // Import real data first
            System.out.println("Importing real capital cities...");
            DataImport.importCapitals();

            // Generate fake data
            System.out.println("Generating fake cities and relationships...");
            FakeDataGenerator.generateFakeData();

            // Find biconnected components
            System.out.println("Finding biconnected components...");
            BiconnectedComponentsFinder finder = new BiconnectedComponentsFinder();
            List<Set<Integer>> components = finder.findBiconnectedComponents();

            System.out.println("\nFound " + components.size() + " biconnected components:");
            for (int i = 0; i < components.size(); i++) {
                System.out.println("Component " + (i + 1) + ": " + components.get(i).size() + " cities");
            }

            // Visualize the results
            System.out.println("Launching visualization...");
            CityMapVisualizer.setBiconnectedComponents(components);
            javafx.application.Application.launch(CityMapVisualizer.class, args);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection();
            System.out.println("\nApplication finished.");
        }
    }
}