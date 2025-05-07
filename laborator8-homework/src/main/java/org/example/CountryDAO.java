package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    public void create(Country country) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO countries (name, continent_id) VALUES (?, ?)")) {
            pstmt.setString(1, country.getName());
            pstmt.setInt(2, country.getContinentId());
            pstmt.executeUpdate();
        }
    }

    public Country findByName(String name) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT id, continent_id FROM countries WHERE name = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Country country = new Country();
                country.setId(rs.getInt("id"));
                country.setName(name);
                country.setContinentId(rs.getInt("continent_id"));
                return country;
            }
            return null;
        }
    }

    public List<Country> findByContinent(int continentId) throws SQLException {
        List<Country> countries = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT id, name FROM countries WHERE continent_id = ?")) {
            pstmt.setInt(1, continentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Country country = new Country();
                country.setId(rs.getInt("id"));
                country.setName(rs.getString("name"));
                country.setContinentId(continentId);
                countries.add(country);
            }
        }
        return countries;
    }
}