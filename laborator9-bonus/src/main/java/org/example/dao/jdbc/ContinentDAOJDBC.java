package org.example.dao.jdbc;

import org.example.dao.ContinentDAO;
import org.example.entity.Continent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ContinentDAOJDBC implements ContinentDAO {
    private static final Logger logger = Logger.getLogger(ContinentDAOJDBC.class.getName());
    private final Connection connection;

    public ContinentDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Continent findById(int id) {
        String sql = "SELECT * FROM continents WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToContinent(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding continent by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Continent findByName(String name) {
        String sql = "SELECT * FROM continents WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToContinent(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding continent by name: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Continent> findAll() {
        List<Continent> continents = new ArrayList<>();
        String sql = "SELECT * FROM continents";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                continents.add(mapRowToContinent(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding all continents: " + e.getMessage());
        }
        return continents;
    }

    @Override
    public void create(Continent continent) {
        String sql = "INSERT INTO continents (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, continent.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    continent.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating continent: " + e.getMessage());
            throw new RuntimeException("Failed to create continent", e);
        }
    }

    @Override
    public void update(Continent continent) {
        String sql = "UPDATE continents SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, continent.getName());
            stmt.setInt(2, continent.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating continent: " + e.getMessage());
            throw new RuntimeException("Failed to update continent", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM continents WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error deleting continent: " + e.getMessage());
            throw new RuntimeException("Failed to delete continent", e);
        }
    }

    private Continent mapRowToContinent(ResultSet rs) throws SQLException {
        Continent continent = new Continent();
        continent.setId(rs.getInt("id"));
        continent.setName(rs.getString("name"));
        return continent;
    }
}