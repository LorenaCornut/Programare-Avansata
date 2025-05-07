/*package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDataGenerator {
    private static final int NUM_CITIES = 10000;
    private static final double SISTER_PROBABILITY = 0.001;
    private static final Random random = new Random();

    public static void generateFakeData() throws SQLException {
        try (Connection con = Database.getConnection()) {
            // Get all country IDs
            List<Integer> countryIds = getCountryIds(con);

            if (countryIds.isEmpty()) {
                System.out.println("No countries found in database. Please import real data first.");
                return;
            }

            // Generate fake cities
            generateCities(con, countryIds);

            // Generate sister relationships
            generateSisterRelationships(con);

            System.out.println("Generated " + NUM_CITIES + " fake cities with random sister relationships");
        }
    }

    private static List<Integer> getCountryIds(Connection con) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM countries")) {
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }
        return ids;
    }

    private static void generateCities(Connection con, List<Integer> countryIds) throws SQLException {
        String insertSql = "INSERT INTO cities (name, country_id, is_capital, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            con.setAutoCommit(false);

            for (int i = 1; i <= NUM_CITIES; i++) {
                pstmt.setString(1, "FakeCity" + i);
                pstmt.setInt(2, countryIds.get(random.nextInt(countryIds.size())));
                pstmt.setInt(3, 0); // Not a capital
                pstmt.setDouble(4, -90 + 180 * random.nextDouble()); // Latitude
                pstmt.setDouble(5, -180 + 360 * random.nextDouble()); // Longitude
                pstmt.addBatch();

                if (i % 1000 == 0) {
                    pstmt.executeBatch();
                    con.commit();
                }
            }
            pstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        }
    }

    private static void generateSisterRelationships(Connection con) throws SQLException {
        String insertSql = "INSERT INTO sister_cities (city1_id, city2_id) VALUES (?, ?)";
        int totalRelationships = 0;

        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            con.setAutoCommit(false);

            for (int i = 1; i <= NUM_CITIES; i++) {
                for (int j = i + 1; j <= NUM_CITIES; j++) {
                    if (random.nextDouble() < SISTER_PROBABILITY) {
                        pstmt.setInt(1, i);
                        pstmt.setInt(2, j);
                        pstmt.addBatch();
                        totalRelationships++;

                        if (totalRelationships % 1000 == 0) {
                            pstmt.executeBatch();
                            con.commit();
                        }
                    }
                }
            }
            pstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        }

        System.out.println("Created " + totalRelationships + " sister city relationships");
    }
}*/


/*package org.example;

import java.sql.*;
import java.util.*;

public class FakeDataGenerator {
    private static final int NUM_CITIES = 10000;
    private static final double SISTER_PROBABILITY = 0.001;
    private static final Random random = new Random();

    public static void generateFakeData() throws SQLException {
        try (Connection con = Database.getConnection()) {
            // Clean existing fake data first
            cleanExistingFakeData(con);

            // Get all country IDs
            List<Integer> countryIds = getCountryIds(con);

            if (countryIds.isEmpty()) {
                System.out.println("No countries found in database. Please import real data first.");
                return;
            }

            // Generate fake cities
            generateCities(con, countryIds);

            // Generate sister relationships
            generateSisterRelationships(con);

            System.out.println("Generated " + NUM_CITIES + " fake cities with random sister relationships");
        }
    }

    private static void cleanExistingFakeData(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            // Delete from sister_cities first (due to foreign key constraints)
            stmt.executeUpdate("DELETE FROM sister_cities WHERE city1_id <= " + NUM_CITIES +
                    " OR city2_id <= " + NUM_CITIES);
            stmt.executeUpdate("DELETE FROM cities WHERE id <= " + NUM_CITIES);
            con.commit();
        }
    }

    private static List<Integer> getCountryIds(Connection con) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM countries")) {
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }
        return ids;
    }

    private static void generateCities(Connection con, List<Integer> countryIds) throws SQLException {
        String insertSql = "INSERT INTO cities (id, name, country_id, is_capital, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            con.setAutoCommit(false);

            for (int i = 1; i <= NUM_CITIES; i++) {
                pstmt.setInt(1, i);
                pstmt.setString(2, "FakeCity" + i);
                pstmt.setInt(3, countryIds.get(random.nextInt(countryIds.size())));
                pstmt.setInt(4, 0); // Not a capital
                pstmt.setDouble(5, -90 + 180 * random.nextDouble()); // Latitude
                pstmt.setDouble(6, -180 + 360 * random.nextDouble()); // Longitude
                pstmt.addBatch();

                if (i % 1000 == 0) {
                    pstmt.executeBatch();
                    con.commit();
                }
            }
            pstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        }
    }

    private static void generateSisterRelationships(Connection con) throws SQLException {
        // First, load existing relationships to avoid duplicates
        Set<String> existingRelationships = loadExistingRelationships(con);

        String insertSql = "INSERT INTO sister_cities (city1_id, city2_id) VALUES (?, ?)";
        int totalRelationships = 0;

        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            con.setAutoCommit(false);

            for (int i = 1; i <= NUM_CITIES; i++) {
                for (int j = i + 1; j <= NUM_CITIES; j++) {
                    // Create a normalized key (smaller id first)
                    String relationshipKey = i + "-" + j;

                    if (random.nextDouble() < SISTER_PROBABILITY &&
                            !existingRelationships.contains(relationshipKey)) {

                        pstmt.setInt(1, i);
                        pstmt.setInt(2, j);
                        pstmt.addBatch();
                        existingRelationships.add(relationshipKey);
                        totalRelationships++;

                        if (totalRelationships % 1000 == 0) {
                            try {
                                pstmt.executeBatch();
                                con.commit();
                            } catch (BatchUpdateException e) {
                                con.rollback();
                                handleBatchError(e);
                                // Continue with next batch
                            }
                        }
                    }
                }
            }

            // Execute remaining batch items
            try {
                pstmt.executeBatch();
                con.commit();
            } catch (BatchUpdateException e) {
                con.rollback();
                handleBatchError(e);
            }

            con.setAutoCommit(true);
        }

        System.out.println("Created " + totalRelationships + " sister city relationships");
    }

    private static Set<String> loadExistingRelationships(Connection con) throws SQLException {
        Set<String> relationships = new HashSet<>();
        String query = "SELECT city1_id, city2_id FROM sister_cities";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int city1 = rs.getInt(1);
                int city2 = rs.getInt(2);
                // Store relationship in normalized form (smaller id first)
                relationships.add(Math.min(city1, city2) + "-" + Math.max(city1, city2));
            }
        }
        return relationships;
    }

    private static void handleBatchError(BatchUpdateException e) {
        System.err.println("Batch error: " + e.getMessage());
        int[] updateCounts = e.getUpdateCounts();
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.err.println("Error in statement " + i);
            }
        }
    }
}*/

package org.example;

import java.sql.*;
import java.util.*;

public class FakeDataGenerator {
    private static final int NUM_CITIES = 10000;
    private static final double SISTER_PROBABILITY = 0.001;
    private static final Random random = new Random();

    public static void generateFakeData() throws SQLException {
        try (Connection con = Database.getConnection()) {
            // Clean existing fake data first
            cleanExistingFakeData(con);

            // Get all country IDs
            List<Integer> countryIds = getCountryIds(con);

            if (countryIds.isEmpty()) {
                System.out.println("No countries found in database. Please import real data first.");
                return;
            }

            // Generate fake cities
            generateCities(con, countryIds);

            // Generate sister relationships
            generateSisterRelationships(con);

            System.out.println("Generated " + NUM_CITIES + " fake cities with random sister relationships");
        }
    }

    private static void cleanExistingFakeData(Connection con) throws SQLException {
        boolean originalAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            try (Statement stmt = con.createStatement()) {
                // Delete from sister_cities first (due to foreign key constraints)
                stmt.executeUpdate("DELETE FROM sister_cities WHERE city1_id <= " + NUM_CITIES +
                        " OR city2_id <= " + NUM_CITIES);
                stmt.executeUpdate("DELETE FROM cities WHERE id <= " + NUM_CITIES);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }

    private static List<Integer> getCountryIds(Connection con) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM countries")) {
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }
        return ids;
    }

    private static void generateCities(Connection con, List<Integer> countryIds) throws SQLException {
        String insertSql = "INSERT INTO cities (id, name, country_id, is_capital, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        boolean originalAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                for (int i = 1; i <= NUM_CITIES; i++) {
                    pstmt.setInt(1, i);
                    pstmt.setString(2, "FakeCity" + i);
                    pstmt.setInt(3, countryIds.get(random.nextInt(countryIds.size())));
                    pstmt.setInt(4, 0); // Not a capital
                    pstmt.setDouble(5, -90 + 180 * random.nextDouble()); // Latitude
                    pstmt.setDouble(6, -180 + 360 * random.nextDouble()); // Longitude
                    pstmt.addBatch();

                    if (i % 1000 == 0) {
                        pstmt.executeBatch();
                        con.commit();
                    }
                }
                pstmt.executeBatch();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }

    private static void generateSisterRelationships(Connection con) throws SQLException {
        // First, load existing relationships to avoid duplicates
        Set<String> existingRelationships = loadExistingRelationships(con);

        String insertSql = "INSERT INTO sister_cities (city1_id, city2_id) VALUES (?, ?)";
        int totalRelationships = 0;

        boolean originalAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                for (int i = 1; i <= NUM_CITIES; i++) {
                    for (int j = i + 1; j <= NUM_CITIES; j++) {
                        // Create a normalized key (smaller id first)
                        String relationshipKey = i + "-" + j;

                        if (random.nextDouble() < SISTER_PROBABILITY &&
                                !existingRelationships.contains(relationshipKey)) {

                            pstmt.setInt(1, i);
                            pstmt.setInt(2, j);
                            pstmt.addBatch();
                            existingRelationships.add(relationshipKey);
                            totalRelationships++;

                            if (totalRelationships % 1000 == 0) {
                                try {
                                    pstmt.executeBatch();
                                    con.commit();
                                } catch (BatchUpdateException e) {
                                    con.rollback();
                                    handleBatchError(e);
                                    // Continue with next batch
                                }
                            }
                        }
                    }
                }

                // Execute remaining batch items
                try {
                    pstmt.executeBatch();
                    con.commit();
                } catch (BatchUpdateException e) {
                    con.rollback();
                    handleBatchError(e);
                }
            }
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }

        System.out.println("Created " + totalRelationships + " sister city relationships");
    }

    private static Set<String> loadExistingRelationships(Connection con) throws SQLException {
        Set<String> relationships = new HashSet<>();
        String query = "SELECT city1_id, city2_id FROM sister_cities";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int city1 = rs.getInt(1);
                int city2 = rs.getInt(2);
                // Store relationship in normalized form (smaller id first)
                relationships.add(Math.min(city1, city2) + "-" + Math.max(city1, city2));
            }
        }
        return relationships;
    }

    private static void handleBatchError(BatchUpdateException e) {
        System.err.println("Batch error: " + e.getMessage());
        int[] updateCounts = e.getUpdateCounts();
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.err.println("Error in statement " + i);
            }
        }
    }
}