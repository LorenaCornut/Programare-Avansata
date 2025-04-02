package org.example.repository;
import org.example.image.Image;
import org.example.exceptions.ImageNotFoundException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.example.exceptions.InvalidRepositoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
/// slide 6
public class RepositoryService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    ///TEXT
    public void saveAsText(Repository repo, String path) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(path))) {
            for (Image img : repo.getImages()) {
                out.write(img.name() + "|" + img.path());
                out.newLine();
            }
        }
    }

    public Repository loadFromText(String path) throws InvalidRepositoryException {
        Repository repo = new Repository();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    repo.addImage(new Image(parts[0], parts[1]));
                }
            }
            return repo;
        } catch (IOException e) {
            throw new InvalidRepositoryException("Failed to load text file: " + e.getMessage());
        }
    }

    ///JSON
    public void saveAsJson(Repository repo, String path) throws IOException {
        objectMapper.writeValue(new File(path), repo);
    }

    public Repository loadFromJson(String path) throws InvalidRepositoryException {
        try {
            return objectMapper.readValue(new File(path), Repository.class);
        } catch (IOException e) {
            throw new InvalidRepositoryException("Invalid JSON file: " + e.getMessage());
        }
    }

    ///BINARY
    public void saveAsBinary(Repository repo, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(repo);
        }
    }

    public Repository loadFromBinary(String path) throws InvalidRepositoryException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Repository) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new InvalidRepositoryException("Invalid binary file: " + e.getMessage());
        }
    }
    public void view(Image img) {
        try {
            Desktop desktop = Desktop.getDesktop();
            File imageFile = new File(img.path());

            if (!imageFile.exists()) {
                throw new ImageNotFoundException("Image file not found: " + img.path());
            }

            desktop.open(imageFile);
        } catch (ImageNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error opening the image: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
