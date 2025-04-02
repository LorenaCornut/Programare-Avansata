package org.example.commands;

import org.example.repository.Repository;
import org.example.exceptions.InvalidCommandException;

public class AddAllCommand implements Command {
    private final Repository repository;
    private final String folderPath;

    public AddAllCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 1) {
            throw new InvalidCommandException("Usage: addAll <folder_path>");
        }
        this.repository = repository;
        this.folderPath = args[0];
    }

    @Override
    public void execute() {
        repository.addAll(folderPath);
        System.out.println("Added all images from: " + folderPath);
    }
}
