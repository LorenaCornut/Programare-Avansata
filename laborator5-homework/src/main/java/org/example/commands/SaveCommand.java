package org.example.commands;

import org.example.repository.Repository;
import org.example.repository.RepositoryService;
import org.example.exceptions.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SaveCommand implements Command {
    private final RepositoryService service;
    private final Repository repository;
    private final String path;
    private final String format;

    public SaveCommand(RepositoryService service, Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 2) {throw new InvalidCommandException("Usage: save <path> <text|json|binary>");}
        this.service = service;
        this.repository = repository;
        this.path = args[0];
        this.format = args[1].toLowerCase();
    }

    @Override
    public void execute() throws IOException {
        switch (format) {
            case "text" -> service.saveAsText(repository, path);
            case "json" -> service.saveAsJson(repository, path);
            case "binary" -> service.saveAsBinary(repository, path);
            default -> throw new IOException("Unsupported format: " + format);
        }
        System.out.println("Repository saved to " + path);
    }
}
