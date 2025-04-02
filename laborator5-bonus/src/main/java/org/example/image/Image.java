package org.example.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public record Image(
        @JsonProperty("name") String name,
        @JsonProperty("path") String path,
        @JsonProperty("tags") Set<String> tags
) {
    @JsonCreator
    public Image(String name, String path, Set<String> tags) {
        this.name = name;
        this.path = path;
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    public Image(String name, String path) {
        this(name, path, new HashSet<>());
    }

    public boolean sharesTagsWith(Image other) {
        if (other == null) return false;
        return !Collections.disjoint(this.tags, other.tags);
    }
}