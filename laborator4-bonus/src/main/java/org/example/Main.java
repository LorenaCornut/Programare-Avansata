package org.example;

import java.util.*;
import java.util.stream.*;
import com.github.javafaker.Faker;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;


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
    public static Location[] generarelocatii(int ct){
        Location[] locations = IntStream.rangeClosed(0, ct-1)
                .mapToObj(i -> new Location(randomName(), randomType()))
                .toArray(Location[]::new);
        return locations;
    }
    public static void addRandomEdges(Graph<Location, DefaultWeightedEdge> g, Location[] locations) {
        Random rand = new Random();
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (i != j  && rand.nextBoolean()) {
                    DefaultWeightedEdge edge = g.addEdge(locations[i], locations[j]);
                    g.setEdgeWeight(edge, rand.nextDouble());
                }
            }
        }
    }
    public static Map<Ruta,Double> calculare_rute_safe (Graph<Location,DefaultWeightedEdge> g) {
        Graph<Location, DefaultWeightedEdge> g_log = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for(Location location : g.vertexSet()){
            g_log.addVertex(location);
        }
        for (DefaultWeightedEdge edge : g.edgeSet()) {
            Location start = g.getEdgeSource(edge); ///inceput
            Location end = g.getEdgeTarget(edge); ///final
            double probabilitate = g.getEdgeWeight(edge); ///pondere
            double logprob = -Math.log(probabilitate);
            DefaultWeightedEdge logEdge = g_log.addEdge(start, end);
            g_log.setEdgeWeight(logEdge, logprob);
        }
        FloydWarshallShortestPaths<Location, DefaultWeightedEdge> floydWarshall = new FloydWarshallShortestPaths<>(g_log);

        Map<Ruta, Double> safestRoutes = new HashMap<>();

        for (Location i : g.vertexSet()) {
            for (Location j : g.vertexSet()) {
                if (!i.equals(j)) {
                    double logSum = floydWarshall.getPathWeight(i, j);
                    double prob = Math.exp(-logSum);
                    List<Location> path = floydWarshall.getPath(i, j).getVertexList();
                    int friendlyct = 0;
                    int neutralct = 0;
                    int enemyct = 0;
                    for (Location location : path) {
                        switch (location.getType()) {
                            case FRIENDLY:
                                friendlyct++;
                                break;
                            case NEUTRAL:
                                neutralct++;
                                break;
                            case ENEMY:
                                enemyct++;
                                break;
                        }
                    }
                    Ruta ruta = new Ruta(i, j);
                    ruta.setProbabilitate(prob);
                    ruta.setct(friendlyct, neutralct, enemyct);
                    safestRoutes.put(ruta, prob);
                }
            }
        }
        return safestRoutes;
    }
    public static void calcul_statistici(Map<Ruta, Double> safestRoutes) {
        double medie = safestRoutes.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        System.out.println("Media probabilitatilor: " + medie);
        Ruta safestRoute = safestRoutes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        Ruta leastSafeRoute = safestRoutes.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Cea mai safe ruta: " + safestRoute);
        System.out.println("Cea mai putin safe ruta: " + leastSafeRoute);
    }
    public static void main(String[] args) {
        Location[] locations = generarelocatii(100);
        Graph<Location, DefaultWeightedEdge> g = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (Location location : locations) {
            g.addVertex(location);
        }
        addRandomEdges(g, locations);
        Map<Ruta, Double> safestRoutes = calculare_rute_safe(g);
        calcul_statistici(safestRoutes);
    }
}
