package org.example.commands;

import org.example.repository.Repository;
import org.example.image.Image;
import org.example.exceptions.*;
import java.io.File;

public class UpdateCommand implements Command {
    private final Repository repository;
    private final String name;
    private final String newPath;

    public UpdateCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: update <name> <new_path>");
        }
        this.repository = repository;
        this.name = args[0];
        this.newPath = args[1];
    }

    @Override
    public void execute() throws ImageNotFoundException, InvalidCommandException {
        File newFile = new File(newPath);
        if (!newFile.exists()) {
            throw new ImageNotFoundException("Fișierul nu există la calea specificată: " + newPath);
        }
        try {
            Image oldImage = repository.findImageByName(name);
            repository.removeImage(name);
            repository.addImage(new Image(name, newPath));
            System.out.println("Updated image: " + name + " (new path: " + newPath + ")");
        } catch (ImageNotFoundException e) {
            throw new ImageNotFoundException("Nu există nicio imagine cu numele '" + name + "' în repository");
        }
    }
}