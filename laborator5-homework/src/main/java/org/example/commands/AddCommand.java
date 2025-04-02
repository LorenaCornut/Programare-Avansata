package org.example.commands;
import org.example.repository.Repository;
import org.example.exceptions.InvalidCommandException;
import org.example.image.Image;
import org.example.exceptions.*;
import java.io.File;

public class AddCommand implements Command {
    private final Repository repository ;
    private final String name;
    private final String path;

    public AddCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: add <name> <path>");
        }
        this.repository = repository;
        this.name = args[0];
        this.path = args[1];
    }
    @Override
    public void execute() throws InvalidCommandException, ImageNotFoundException {
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new ImageNotFoundException("Fișierul nu există la calea specificată: " + path);
        }
        try {
            if (repository.findImageByName(name) != null) {
                throw new InvalidCommandException("Numele '" + name + "' este deja folosit");
            }
        } catch (ImageNotFoundException e) { }
        repository.addImage(new Image(name, path));
        System.out.println("Imagine adăugată: " + name + " (" + path + ")");
    }
}