package org.example.image;
/// slide 4
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Image( @JsonProperty("name") String name, @JsonProperty("path") String path) {
    @JsonCreator
    public Image {}  // Pentru Jackson
}