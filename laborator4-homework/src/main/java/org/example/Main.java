package org.example;

import java.util.*;
import java.util.stream.*;
import com.github.javafaker.Faker;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


public class Main {
    private static final Faker faker = new Faker();
    public static LocationType randomType(){
        Random rand = new Random();
        LocationType[] enumTypes = LocationType.values();
        return enumTypes[rand.nextInt(enumTypes.length)];
    }
    public static String randomName() {
        return faker.address().cityName();
    }
    public static void main(String[] args) {
        ///slide 3
        Location[] locations = IntStream.rangeClosed(0, 10)
                .mapToObj(i -> new Location(randomName(), randomType()))
                .toArray(Location[]::new);
        ///-----
        System.out.println("Locatii:");
        for (Location location : locations) { ///o afisare
            System.out.println(location);
        }

        Graph<Location, DefaultWeightedEdge> g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (Location location : locations) {
            g.addVertex(location);
        }

        Random rand = new Random();
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (i != j && rand.nextBoolean()) {
                    DefaultWeightedEdge edge = g.addEdge(locations[i], locations[j]);
                    g.setEdgeWeight(edge, rand.nextInt(10) + 1);
                }
            }
        }

        DijkstraShortestPath<Location,DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(g);
        Location startLocation = locations[0];
        System.out.println("\nCel mai rapid timp pentru " + startLocation.getName() + ":");
        for (Location location : locations) {
            if (!location.equals(startLocation)) {
                double time = dijkstra.getPathWeight(startLocation, location);
                System.out.println(location.getName() + " (" + location.getType() + "): " + time);
            }
        }

        Map<LocationType, List<Location>> locations_pe_tip = Arrays.stream(locations)
                .collect(Collectors.groupingBy(Location::getType));

        System.out.println("\nCele mai rapide drumiri, dupa tip:");
        for (LocationType type : LocationType.values()) {
            System.out.println(type + ":");
            locations_pe_tip.get(type).forEach(loc -> {
                if (!loc.equals(startLocation)) {
                    double time = dijkstra.getPathWeight(startLocation, loc);
                    System.out.println("  " + loc.getName() + ": " + time);
                }
            });
        }
    }
}
