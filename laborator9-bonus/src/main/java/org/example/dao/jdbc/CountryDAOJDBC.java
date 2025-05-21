package org.example.dao.jdbc;

import org.example.dao.CountryDAO;
import org.example.entity.Country;
import org.example.entity.Continent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CountryDAOJDBC implements CountryDAO {
    private static final Logger logger = Logger.getLogger(CountryDAOJDBC.class.getName());
    private final Connection connection;

    public CountryDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Country findById(int id) {
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCountry(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding country by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Country findByName(String name) {
        String sql = "SELECT * FROM countries WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCountry(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding country by name: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Country> findAll() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM countries";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                countries.add(mapRowToCountry(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding all countries: " + e.getMessage());
        }
        return countries;
    }

    @Override
    public List<Country> findByContinent(int continentId) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM countries WHERE continent_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, continentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                countries.add(mapRowToCountry(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding countries by continent: " + e.getMessage());
        }
        return countries;
    }

    @Override
    public void create(Country country) {
        String sql = "INSERT INTO countries (name, continent_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, country.getName());
            stmt.setInt(2, country.getContinent().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    country.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating country: " + e.getMessage());
            throw new RuntimeException("Failed to create country", e);
        }
    }

    @Override
    public void update(Country country) {
        String sql = "UPDATE countries SET name = ?, continent_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, country.getName());
            stmt.setInt(2, country.getContinent().getId());
            stmt.setInt(3, country.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating country: " + e.getMessage());
            throw new RuntimeException("Failed to update country", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM countries WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error deleting country: " + e.getMessage());
            throw new RuntimeException("Failed to delete country", e);
        }
    }

    private Country mapRowToCountry(ResultSet rs) throws SQLException {
        Country country = new Country();
        country.setId(rs.getInt("id"));
        country.setName(rs.getString("name"));

        Continent continent = new Continent();
        continent.setId(rs.getInt("continent_id"));
        country.setContinent(continent);

        return country;
    }
}