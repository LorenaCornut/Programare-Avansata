package org.example;

import org.example.repository.Repository;
import org.example.repository.RepositoryService;
import org.example.image.Image;
import org.example.exceptions.ImageNotFoundException;

public class Main {
    public static void main(String[] args) {
        var repo = new Repository();
        repo.addImage(new Image("Duke", "/Java/Duck.jpg"));
        repo.addImage(new Image("Java-Logo", "/Java/Java-Logo.jpg"));
        var service = new RepositoryService();
       try {

            Image img = repo.findImageByName("Java-Logo");
            service.view(img);
        } catch (ImageNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}