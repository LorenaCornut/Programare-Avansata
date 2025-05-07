package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;

public class CityMapVisualizer extends Application {
    private static List<Set<Integer>> biconnectedComponents = Collections.emptyList();
    private static final Color[] COMPONENT_COLORS = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.PURPLE, Color.CYAN, Color.MAGENTA, Color.YELLOW
    };

    public static void setBiconnectedComponents(List<Set<Integer>> components) {
        biconnectedComponents = components != null ? components : Collections.emptyList();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Canvas canvas = new Canvas(1200, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // Get all cities with coordinates
        Map<Integer, City> cities = getCitiesWithCoordinates();

        // Draw connections first (so cities appear on top)
        drawConnections(gc, cities);

        // Draw cities
        drawCities(gc, cities);

        // Draw legend
        drawLegend(gc);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("World Cities with Sister Relationships and Biconnected Components");
        primaryStage.show();
    }

    private Map<Integer, City> getCitiesWithCoordinates() throws SQLException {
        Map<Integer, City> cities = new HashMap<>();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, latitude, longitude FROM cities")) {
            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt("id"));
                city.setName(rs.getString("name"));
                city.setLatitude(rs.getDouble("latitude"));
                city.setLongitude(rs.getDouble("longitude"));
                cities.put(city.getId(), city);
            }
        }

        return cities;
    }

    private void drawConnections(GraphicsContext gc, Map<Integer, City> cities) throws SQLException {
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT city1_id, city2_id FROM sister_cities")) {
            while (rs.next()) {
                int city1Id = rs.getInt(1);
                int city2Id = rs.getInt(2);

                City city1 = cities.get(city1Id);
                City city2 = cities.get(city2Id);

                if (city1 != null && city2 != null) {
                    // Check if this connection is part of a biconnected component
                    int componentIndex = findComponentContaining(city1Id, city2Id);
                    Color color = (componentIndex >= 0) ?
                            COMPONENT_COLORS[componentIndex % COMPONENT_COLORS.length] :
                            Color.gray(0.7);

                    gc.setStroke(color);
                    gc.setLineWidth(0.5);

                    double x1 = longitudeToX(city1.getLongitude());
                    double y1 = latitudeToY(city1.getLatitude());
                    double x2 = longitudeToX(city2.getLongitude());
                    double y2 = latitudeToY(city2.getLatitude());

                    gc.strokeLine(x1, y1, x2, y2);
                }
            }
        }
    }

    private int findComponentContaining(int city1Id, int city2Id) {
        for (int i = 0; i < biconnectedComponents.size(); i++) {
            Set<Integer> component = biconnectedComponents.get(i);
            if (component.contains(city1Id) && component.contains(city2Id)) {
                return i;
            }
        }
        return -1;
    }

    private void drawCities(GraphicsContext gc, Map<Integer, City> cities) {
        gc.setFill(Color.BLACK);

        for (City city : cities.values()) {
            double x = longitudeToX(city.getLongitude());
            double y = latitudeToY(city.getLatitude());

            gc.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    private void drawLegend(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText("City Connections Map", 20, 20);

        if (!biconnectedComponents.isEmpty()) {
            gc.fillText("Biconnected Components:", 20, 40);

            for (int i = 0; i < Math.min(biconnectedComponents.size(), 8); i++) {
                gc.setFill(COMPONENT_COLORS[i % COMPONENT_COLORS.length]);
                gc.fillRect(20, 60 + i * 20, 15, 15);
                gc.fillText("Component " + (i + 1) + " (" + biconnectedComponents.get(i).size() + " cities)", 40, 72 + i * 20);
            }
        }
    }

    private double longitudeToX(double longitude) {
        return (longitude + 180) * (1200 / 360.0);
    }

    private double latitudeToY(double latitude) {
        return (90 - latitude) * (600 / 180.0);
    }
}