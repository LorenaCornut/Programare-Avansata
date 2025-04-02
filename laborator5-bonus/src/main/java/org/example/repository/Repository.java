package org.example.repository;
import org.example.image.Image;
import java.util.*;
import org.example.exceptions.ImageNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;

public class Repository {
    private List<Image> images = new ArrayList<>();
    private static final List<String> PREDEFINED_TAGS = Arrays.asList("nature", "city", "people", "animal", "landscape", "abstract", "art", "food", "travel", "sport");
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
    public void addAll(String folderPath) {
        try {
            Files.walk(Path.of(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .forEach(path -> {
                        String name = path.getFileName().toString();
                        String pathStr = path.toString();
                        Set<String> tags = generateRandomTags();
                        images.add(new Image(name, pathStr, tags));
                        System.out.println("Added: " + name + " with tags: " + tags);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error scanning folder: " + e.getMessage());
        }
    }
    private boolean isImageFile(Path path) {
        String name = path.toString().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
    }
    private Set<String> generateRandomTags() {
        Random random = new Random();
        int tagCount = random.nextInt(3) + 1;
        Set<String> tags = new HashSet<>();
        while (tags.size() < tagCount) {
            tags.add(PREDEFINED_TAGS.get(random.nextInt(PREDEFINED_TAGS.size())));
        }
        return tags;
    }
    public List<Set<Image>> gasire_clica_maxima() {
        List<Set<Image>> cliques = new ArrayList<>();
        bronKerbosch(new HashSet<>(), new HashSet<>(images), new HashSet<>(), cliques);
        return filtrare_clica_maxim(cliques);
    }
    private void bronKerbosch(Set<Image> in_clica, Set<Image> pentru_extindere, Set<Image> X, List<Set<Image>> cliques) {
        if (pentru_extindere.isEmpty() && X.isEmpty() && in_clica.size() > 1) {
            cliques.add(new HashSet<>(in_clica));
            return;
        }

        Image pivot = selectPivot(pentru_extindere, X);
        Set<Image> candidates = new HashSet<>(pentru_extindere);
        candidates.removeAll(getNeighbors(pivot));

        for (Image v : candidates) {
            Set<Image> neighbors = getNeighbors(v);
            bronKerbosch(
                    union(in_clica, Set.of(v)),
                    intersection(pentru_extindere, neighbors),
                    intersection(X, neighbors),
                    cliques
            );
            pentru_extindere.remove(v);
            X.add(v);
        }
    }
    private Set<Image> getNeighbors(Image image) {
        return images.stream()
                .filter(other -> !other.equals(image) && image.sharesTagsWith(other))
                .collect(Collectors.toSet());
    }
    private Set<Image> union(Set<Image> a, Set<Image> b) {
        Set<Image> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }
    private Set<Image> intersection(Set<Image> a, Set<Image> b) {
        Set<Image> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }
    private List<Set<Image>> filtrare_clica_maxim(List<Set<Image>> cliques) {
        return cliques.stream()
                .filter(clique -> cliques.stream()
                        .noneMatch(other -> other != clique && other.containsAll(clique)))
                .collect(Collectors.toList());
    }
    /*private Image selectPivot(Set<Image> P, Set<Image> X) {
        return !P.isEmpty() ? P.iterator().next() : X.iterator().next();
    }*/
    private Image selectPivot(Set<Image> P, Set<Image> X) {
        if (!P.isEmpty()) {
            return P.iterator().next();
        } else if (!X.isEmpty()) {
            return X.iterator().next();
        } else {
            throw new IllegalArgumentException("Both sets are empty, cannot select a pivot.");
        }
    }
}
