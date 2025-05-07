package org.example;

import java.sql.*;

public class ContinentDAO {
    public void create(Continent continent) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO continents (name) VALUES (?)")) {
            pstmt.setString(1, continent.getName());
            pstmt.executeUpdate();
        }
    }

    public Continent findByName(String name) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT id, name FROM continents WHERE name = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Continent continent = new Continent();
                continent.setId(rs.getInt("id"));
                continent.setName(rs.getString("name"));
                return continent;
            }
        }
        return null;
    }

    public String findById(int id) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT name FROM continents WHERE id = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }
}