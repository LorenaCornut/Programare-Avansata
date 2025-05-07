package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");

            // Clear existing data (optional)
            // clearDatabase();

            System.out.println("Importing data...");
            DataImport.importCapitals();
            System.out.println("Data import completed.");

            // Example usage
            CityDAO cityDAO = new CityDAO();

            City bucharest = cityDAO.findByNameAndCountry("Bucharest", "Romania");
            City paris = cityDAO.findByNameAndCountry("Paris", "France");

            if (bucharest != null && paris != null) {
                double distance = cityDAO.calculateDistance(bucharest, paris);
                System.out.printf("\nDistance between %s and %s: %.2f km\n",
                        bucharest.getName(), paris.getName(), distance);
            } else {
                System.out.println("\nCould not find one or both cities in database");
            }

            // List European countries
            ContinentDAO continentDAO = new ContinentDAO();
            CountryDAO countryDAO = new CountryDAO();

            Continent europe = continentDAO.findByName("Europe");
            if (europe != null) {
                System.out.println("\nEuropean countries:");
                countryDAO.findByContinent(europe.getId())
                        .forEach(c -> System.out.println("- " + c.getName()));
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Database.closeConnection();
            System.out.println("\nApplication finished.");
        }
    }
}