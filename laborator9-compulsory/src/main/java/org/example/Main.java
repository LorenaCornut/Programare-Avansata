package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");

            // Initialize repositories
            ContinentRepository continentRepo = new ContinentRepository();
            CountryRepository countryRepo = new CountryRepository();
            CityRepository cityRepo = new CityRepository();

            // Example: Find entities by ID
            Continent europe = continentRepo.findById(1);
            Country romania = countryRepo.findById(1);
            City bucharest = cityRepo.findById(1);

            System.out.println("Continent with ID 1: " + (europe != null ? europe.getName() : "Not found"));
            System.out.println("Country with ID 1: " + (romania != null ? romania.getName() : "Not found"));
            System.out.println("City with ID 1: " + (bucharest != null ? bucharest.getName() : "Not found"));

            // Example: Calculate distance between cities
            City paris = cityRepo.findByNameAndCountry("Paris", "France");
            if (bucharest != null && paris != null) {
                double distance = cityRepo.calculateDistance(bucharest, paris);
                System.out.printf("Distance between %s and %s: %.2f km%n",
                        bucharest.getName(), paris.getName(), distance);
            }

            // Example: List European countries
            if (europe != null) {
                System.out.println("\nEuropean countries:");
                countryRepo.findByContinent(europe.getId())
                        .forEach(c -> System.out.println("- " + c.getName()));
            }

            // Example: List capital cities
            System.out.println("\nCapital cities:");
            cityRepo.findCapitals()
                    .forEach(c -> System.out.println("- " + c.getName() + ", " + c.getCountry().getName()));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            PersistenceManager.closeEntityManagerFactory();
            System.out.println("\nApplication finished.");
        }
    }
}