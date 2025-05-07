package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {
    public void create(City city) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO cities (name, country_id, is_capital, latitude, longitude) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, city.getName());
            pstmt.setInt(2, city.getCountryId());
            pstmt.setInt(3, city.isCapital() ? 1 : 0);
            pstmt.setDouble(4, city.getLatitude());
            pstmt.setDouble(5, city.getLongitude());
            pstmt.executeUpdate();
        }
    }

    public City findByNameAndCountry(String cityName, String countryName) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT c.id, c.name, c.country_id, c.is_capital, c.latitude, c.longitude " +
                             "FROM cities c JOIN countries co ON c.country_id = co.id " +
                             "WHERE c.name = ? AND co.name = ?")) {
            pstmt.setString(1, cityName);
            pstmt.setString(2, countryName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToCity(rs);
            }
        }
        return null;
    }

    public List<City> findByCountry(int countryId) throws SQLException {
        List<City> cities = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT id, name, is_capital, latitude, longitude FROM cities WHERE country_id = ?")) {
            pstmt.setInt(1, countryId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cities.add(mapToCity(rs));
            }
        }
        return cities;
    }

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

    private City mapToCity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        city.setCountryId(rs.getInt("country_id"));
        city.setCapital(rs.getInt("is_capital") == 1);
        city.setLatitude(rs.getDouble("latitude"));
        city.setLongitude(rs.getDouble("longitude"));
        return city;
    }
}