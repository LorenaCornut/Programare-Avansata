package org.example;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Început program...");

            ContinentDAO continentDAO = new ContinentDAO();
            CountryDAO countryDAO = new CountryDAO();

            continentDAO.create("Europa1");
            Database.getConnection().commit();

            Integer europeId = continentDAO.findByName("Europa");
            if (europeId == null) {
                throw new SQLException("Continentul 'Europa' nu a fost găsit!");
            }

            countryDAO.create("Spania", europeId);
            countryDAO.create("Portugalia", europeId);
            Database.getConnection().commit();


            List<String> europeCountries = countryDAO.findByContinent(europeId);
            System.out.println("\nȚări din Europa:");
            for (String country : europeCountries) {
                System.out.println("• " + country);
            }

            System.out.println("\nProgram finalizat cu succes!");
        } catch (SQLException e) {
            System.err.println("EROARE: " + e.getMessage());
            Database.rollback();
        } finally {
            Database.closeConnection();
        }
    }
}