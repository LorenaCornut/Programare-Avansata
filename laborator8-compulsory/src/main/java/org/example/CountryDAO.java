package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    public void create(String name, int continentId) throws SQLException {
        Connection con = Database.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO countries (name, continent_id) VALUES (?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, continentId);
            pstmt.executeUpdate();
            System.out.println("Țara '" + name + "' adăugată cu succes!");
        }
    }

    public List<String> findByContinent(int continentId) throws SQLException {
        List<String> countries = new ArrayList<>();
        Connection con = Database.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM countries WHERE continent_id=" + continentId)) {
            while (rs.next()) {
                countries.add(rs.getString(1));
            }
        }
        return countries;
    }
}