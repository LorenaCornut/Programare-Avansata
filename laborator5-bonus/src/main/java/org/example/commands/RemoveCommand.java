package org.example.commands;
import org.example.repository.Repository;
import org.example.exceptions.*;

public class RemoveCommand implements Command {
    private final Repository repository;
    private final String name;

    public RemoveCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 1) {
            throw new InvalidCommandException("Usage: remove <name>");
        }
        this.repository = repository;
        this.name = args[0];
    }

    @Override
    public void execute() throws ImageNotFoundException {
        repository.findImageByName(name);
        repository.removeImage(name);
        System.out.println("Removed image: " + name);
    }
}