package org.example.controller;

import org.example.controller.CallService;
import org.example.entity.City;
import org.example.entity.Country;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;
import org.springframework.mock.web.MockMultipartFile;
import org.example.service.CountryGraphBuilder;
import org.example.service.CountryColoringService;
import java.io.IOException;

public class ClientTester {

    private static String token; // Store the token

    public static void main(String[] args) {
        CallService service = new CallService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Client Menu ===");
            System.out.println("1. Get all countries");
            System.out.println("2. Get all cities");
            System.out.println("3. Add a city");
            System.out.println("4. Update a city");
            System.out.println("5. Delete a city");
            System.out.println("6. Build country graph and assign colors");
            System.out.println("7. Authenticate");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            try {
                int opt = scanner.nextInt();
                scanner.nextLine(); // clean newline

                switch (opt) {
                    case 1 -> {
                        List<Country> countries = service.getCountries();
                        countries.forEach(System.out::println);
                    }
                    case 2 -> {
                        List<City> cities = service.getCities();
                        cities.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Enter city name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter country ID: ");
                        long countryId = scanner.nextLong();
                        scanner.nextLine(); // clean newline


                        List<Country> countries = service.getCountries();
                        boolean found = countries.stream().anyMatch(c -> c.getId() == countryId);
                        if (!found) {
                            System.out.println("Invalid country ID.");
                            break;
                        }

                        City added = service.addCity(name, countryId);
                        System.out.println("City added: " + added);
                    }
                    case 4 -> {
                        System.out.print("Enter city ID to update: ");
                        long id = scanner.nextLong();
                        scanner.nextLine(); // clean newline
                        System.out.print("Enter new city name: ");
                        String newName = scanner.nextLine();
                        City updated = service.updateCity(id, newName);
                        System.out.println("Updated city: " + updated);
                    }
                    case 5 -> {
                        System.out.print("Enter city ID to delete: ");
                        long id = scanner.nextLong();
                        scanner.nextLine(); // clean newline
                        service.deleteCity(id);
                        System.out.println("City deleted.");
                    }
                    case 6 -> {
                        System.out.println("Enter CSV content (end with an empty line):");
                        StringBuilder csvContent = new StringBuilder();
                        while (true) {
                            String line = scanner.nextLine();
                            if (line.isEmpty()) break;
                            csvContent.append(line).append("\n");
                        }

                        try {
                            MockMultipartFile mockFile = new MockMultipartFile(
                                "file", // Name of the file
                                "graph.csv", // Original filename
                                "text/csv", // Content type
                                csvContent.toString().getBytes() // File content as bytes
                            );
                            CountryGraphBuilder graphBuilder = new CountryGraphBuilder();
                            CountryColoringService coloringService = new CountryColoringService();

                            Map<String, Set<String>> graph = graphBuilder.buildGraph(mockFile);
                            Map<String, Integer> colors = coloringService.assignColors(graph);

                            System.out.println("Graph:");
                            graph.forEach((country, neighbors) -> System.out.println(country + " -> " + neighbors));

                            System.out.println("Color Assignment:");
                            colors.forEach((country, color) -> System.out.println(country + " -> Color " + color));
                        } catch (IOException e) {
                            System.out.println("Error processing CSV: " + e.getMessage());
                        }
                    }
                    case 7 -> {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        try {
                            token = service.authenticate(username, password); // Store the token
                            service.setToken(token); // Pass the token to CallService
                            System.out.println("Authentication successful. Token: " + token);
                        } catch (Exception e) {
                            System.out.println("Authentication failed: " + e.getMessage());
                        }
                    }
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }

            } catch (HttpServerErrorException e) {
                System.out.println("Server error: " + e.getStatusCode() + " - " + e.getStatusText());
                System.out.println("Details: " + e.getResponseBodyAsString());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // clear input buffer
            }
        }
    }
}
