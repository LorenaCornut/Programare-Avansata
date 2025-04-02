package org.example.commands;

import org.example.repository.Repository;
import org.example.exceptions.InvalidCommandException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.*;

public class TagsCommand implements Command {
    private final Repository repository;

    public TagsCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 0) {
            throw new InvalidCommandException("Usage: tags");
        }
        this.repository = repository;
    }

    @Override
    public void execute() {
        var cliques = repository.gasire_clica_maxima();
        ///System.out.println("\n=== Maximal Groups of Images ===");
        ///System.out.println("(Any two images in a group share at least one tag)");

        if (cliques.isEmpty()) {
            System.out.println("No groups found.");
            return;
        }

        for (int i = 0; i < cliques.size(); i++) {
            System.out.printf("\nGroup %d (%d images):\n", i+1, cliques.get(i).size());
            cliques.get(i).forEach(img ->
                    System.out.printf("- %s (Tags: %s)\n",
                            img.name(), String.join(", ", img.tags()))
            );
        }
    }
}