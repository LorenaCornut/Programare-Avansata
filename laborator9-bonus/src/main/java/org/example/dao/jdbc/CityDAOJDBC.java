package org.example.dao.jdbc;

import org.example.dao.CityDAO;
import org.example.entity.City;
import org.example.entity.Country;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.example.util.CitySolver;

public class CityDAOJDBC implements CityDAO {
    private static final Logger logger = Logger.getLogger(CityDAOJDBC.class.getName());
    private final Connection connection;

    public CityDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public City findById(int id) {
        String sql = "SELECT * FROM cities WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCity(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding city by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public City findByNameAndCountry(String cityName, String countryName) {
        String sql = "SELECT c.* FROM cities c JOIN countries co ON c.country_id = co.id " +
                "WHERE c.name = ? AND co.name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            stmt.setString(2, countryName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCity(rs);
            }
        } catch (SQLException e) {
            logger.severe("Error finding city by name and country: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<City> findByCountry(int countryId) {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities WHERE country_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(mapRowToCity(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding cities by country: " + e.getMessage());
        }
        return cities;
    }

    @Override
    public List<City> findCapitals() {
        List<City> capitals = new ArrayList<>();
        String sql = "SELECT * FROM cities WHERE is_capital = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                capitals.add(mapRowToCity(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding capital cities: " + e.getMessage());
        }
        return capitals;
    }

    @Override
    public List<City> findByNamePattern(String pattern) {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities WHERE name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + pattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(mapRowToCity(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding cities by name pattern: " + e.getMessage());
        }
        return cities;
    }

    @Override
    public double calculateDistance(City city1, City city2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(city2.getLatitude() - city1.getLatitude());
        double lonDistance = Math.toRadians(city2.getLongitude() - city1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(city1.getLatitude())) * Math.cos(Math.toRadians(city2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public List<City> findCitiesWithSameStartingLetterAndPopulationBetween(int lowerBound, int upperBound) {
        List<City> allCities = new ArrayList<>();
        String sql = "SELECT * FROM cities";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allCities.add(mapRowToCity(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching all cities: " + e.getMessage());
        }
        return CitySolver.findMatchingCities(allCities, lowerBound, upperBound);
    }

    @Override
    public List<City> findByPopulationRange(int min, int max) {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities WHERE population BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, min);
            stmt.setInt(2, max);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(mapRowToCity(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error finding cities by population range: " + e.getMessage());
        }
        return cities;
    }

    @Override
    public void create(City city) {
        String sql = "INSERT INTO cities (name, country_id, is_capital, latitude, longitude, population) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, city.getName());
            stmt.setInt(2, city.getCountry().getId());
            stmt.setBoolean(3, city.isCapital());
            stmt.setDouble(4, city.getLatitude());
            stmt.setDouble(5, city.getLongitude());
            stmt.setInt(6, city.getPopulation());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    city.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating city: " + e.getMessage());
            throw new RuntimeException("Failed to create city", e);
        }
    }

    @Override
    public void update(City city) {
        String sql = "UPDATE cities SET name = ?, country_id = ?, is_capital = ?, " +
                "latitude = ?, longitude = ?, population = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, city.getName());
            stmt.setInt(2, city.getCountry().getId());
            stmt.setBoolean(3, city.isCapital());
            stmt.setDouble(4, city.getLatitude());
            stmt.setDouble(5, city.getLongitude());
            stmt.setInt(6, city.getPopulation());
            stmt.setInt(7, city.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating city: " + e.getMessage());
            throw new RuntimeException("Failed to update city", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM cities WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error deleting city: " + e.getMessage());
            throw new RuntimeException("Failed to delete city", e);
        }
    }

    private City mapRowToCity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        city.setCapital(rs.getBoolean("is_capital"));
        city.setLatitude(rs.getDouble("latitude"));
        city.setLongitude(rs.getDouble("longitude"));
        city.setPopulation(rs.getInt("population"));

        Country country = new Country();
        country.setId(rs.getInt("country_id"));
        city.setCountry(country);

        return city;
    }
}