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

        System.out.println("=== Image Repository Manager ===");
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
                    case "remove":
                        new RemoveCommand(repository, arguments).execute();
                        break;
                    case "update":
                        new UpdateCommand(repository, arguments).execute();
                        break;
                    case "save":
                        new SaveCommand(service, repository, arguments).execute();
                        break;
                    case "load":
                        LoadCommand loadCommand = new LoadCommand(service, arguments);
                        loadCommand.execute();
                        repository = loadCommand.getLoadedRepository(); // Actualizează referința
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
            }
        }
    }

    private static void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  add <name> <path>       - Add new image");
        System.out.println("  remove <name>           - Remove image");
        System.out.println("  update <name> <newPath> - Update image path");
        System.out.println("  save <path> <format>    - Save repository (text/json/binary)");
        System.out.println("  load <path> <format>    - Load repository");
        System.out.println("  report <path.html>      - Generate HTML report");
        System.out.println("  help                    - Show this help");
        System.out.println("  exit                    - Exit application");
    }
}