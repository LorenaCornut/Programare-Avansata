package org.example.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CountryColoringService {

    public Map<String, Integer> assignColors(Map<String, Set<String>> graph) {
        Map<String, Integer> colorAssignment = new HashMap<>();

        for (String country : graph.keySet()) {
            // Collect colors used by neighbors
            Set<Integer> usedColors = graph.get(country).stream()
                    .map(colorAssignment::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Assign the smallest non-used color
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }

            colorAssignment.put(country, color);
        }

        return colorAssignment;
    }
}
