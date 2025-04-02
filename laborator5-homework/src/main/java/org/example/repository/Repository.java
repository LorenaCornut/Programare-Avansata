package org.example.repository;
import org.example.image.Image;
import java.util.*;
import org.example.exceptions.ImageNotFoundException;

public class Repository {
    private List<Image> images = new ArrayList<>();

    public Repository() {
        this.images = new ArrayList<>();
    }

    public List<Image> getImages() {
        return images;
    }

    public void addImage(Image image) {
        images.add(image);
    }
    public void removeImage(String name) throws ImageNotFoundException {
        if (!images.removeIf(img -> img.name().equals(name))) {
            throw new ImageNotFoundException("Image '" + name + "' not found");
        }
    }
    public Image findImageByName(String name) throws ImageNotFoundException {
        for (Image image : images) {
            if (image.name().equals(name)) {
                return image;
            }
        }
        throw new ImageNotFoundException("Image with name '" + name + "' not found.");

    }
}
