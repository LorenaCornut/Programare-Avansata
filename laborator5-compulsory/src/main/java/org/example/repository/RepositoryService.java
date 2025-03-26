package org.example.repository;
import org.example.image.Image;
import org.example.exceptions.ImageNotFoundException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
/// slide 6
public class RepositoryService {
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
