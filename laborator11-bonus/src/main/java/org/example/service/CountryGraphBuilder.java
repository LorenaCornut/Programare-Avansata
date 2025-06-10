package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CountryGraphBuilder {

    public Map<String, Set<String>> buildGraph(MultipartFile csvFile) throws IOException {
        Map<String, Set<String>> graph = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String countryCode = parts[0].trim();
                    String neighborCode = parts[2].trim();

                    graph.putIfAbsent(countryCode, new HashSet<>());
                    graph.putIfAbsent(neighborCode, new HashSet<>());

                    graph.get(countryCode).add(neighborCode);
                    graph.get(neighborCode).add(countryCode); // bidirectional
                }
            }
        }

        return graph;
    }
}
