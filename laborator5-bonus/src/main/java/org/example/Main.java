package org.example;

import org.example.commands.*;
import org.example.repository.Repository;
import org.example.repository.RepositoryService;
import org.example.exceptions.*;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        RepositoryService service = new RepositoryService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Image Repository Manager (Bonus Edition) ===");
        printHelp();

        while (true) {
            System.out.print("\ncommand> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String[] arguments = parts.length > 1 ? parts[1].split(" ") : new String[0];

            try {
                switch (command) {
                    case "add":
                        new AddCommand(repository, arguments).execute();
                        break;
                    case "addall":
                        new AddAllCommand(repository, arguments).execute();
                        break;
                    case "remove":
                        new RemoveCommand(repository, arguments).execute();
                        break;
                    case "update":
                        new UpdateCommand(repository, arguments).execute();
                        break;
                    case "tags":
                        new TagsCommand(repository, arguments).execute();
                        break;
                    case "save":
                        new SaveCommand(service, repository, arguments).execute();
                        break;
                    case "load":
                        LoadCommand loadCommand = new LoadCommand(service, arguments);
                        loadCommand.execute();
                        repository = loadCommand.getLoadedRepository();
                        System.out.println("Repository loaded successfully. " +
                                repository.getImages().size() + " images available.");
                        break;
                    case "report":
                        new ReportCommand(repository, arguments).execute();
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "exit":
                        System.out.println("Exiting application...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid command. Type 'help' for available commands.");
                }
            } catch (InvalidCommandException e) {
                System.out.println("Error: Invalid command format. " + e.getMessage());
            } catch (ImageNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InvalidRepositoryException e) {
                System.out.println("Error loading repository: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace(); // Pentru depanare
            }
        }
    }

    private static void printHelp() {
        System.out.println("\n=== Available Commands ===");
        System.out.println("Comenzi de bază:");
        System.out.println("  add <name> <path>       - Adaugă o imagine nouă");
        System.out.println("  remove <name>           - Elimină o imagine");
        System.out.println("  update <name> <newPath> - Actualizează calea unei imagini");
        System.out.println("  list                    - Listează toate imaginile");

        System.out.println("\nComenzi bonus:");
        System.out.println("  addAll <folder>         - Adaugă toate imaginile dintr-un folder (cu tag-uri generate automat)");
        System.out.println("  tags                    - Afișează grupuri de imagini cu tag-uri comune");

        System.out.println("\nComenzi de persistare:");
        System.out.println("  save <path> <format>    - Salvează repository (formate: text/json/binary)");
        System.out.println("  load <path> <format>    - Încarcă repository");
        System.out.println("  report <output.html>    - Generează raport HTML");

        System.out.println("\nAltele:");
        System.out.println("  help                    - Afișează acest help");
        System.out.println("  exit                    - Ieșire din aplicație");

        System.out.println("\nExemple:");
        System.out.println("  addAll C:\\Photos");
        System.out.println("  tags");
        System.out.println("  save backup.json json");
    }
}