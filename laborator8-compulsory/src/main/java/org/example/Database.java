package org.example;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "lab8java";
    private static final String PASSWORD = "lab8java";
    private static Connection connection = null;

    private Database() {}

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("Conexiune la baza de date realizată cu succes!");
        } catch (SQLException e) {
            System.err.println("Eroare la conectare: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexiune închisă cu succes!");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la închidere conexiune: " + e.getMessage());
        }
    }

    public static void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
                System.out.println("Operațiune anulată - rollback efectuat!");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la rollback: " + e.getMessage());
        }
    }
}