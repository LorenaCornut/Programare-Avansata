package org.example.commands;

import org.example.repository.Repository;
import org.example.repository.RepositoryService;
import org.example.exceptions.*;

public class LoadCommand implements Command {
    private final RepositoryService service;
    private final String path;
    private final String format;
    private Repository loadedRepository;
    public LoadCommand(RepositoryService service, String[] args) throws InvalidCommandException {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: load <path> <text|json|binary>");
        }
        this.service = service;
        this.path = args[0];
        this.format = args[1].toLowerCase();
    }

    @Override
    public void execute() throws InvalidRepositoryException {
        loadedRepository = switch (format) { // Modificare aici - salvează direct în loadedRepository
            case "text" -> service.loadFromText(path);
            case "json" -> service.loadFromJson(path);
            case "binary" -> service.loadFromBinary(path);
            default -> throw new InvalidRepositoryException("Unsupported format: " + format);
        };
        System.out.println("Repository loaded from " + path);
    }
    public Repository getLoadedRepository() {
        return loadedRepository;
    }
}