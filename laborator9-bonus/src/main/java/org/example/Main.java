package org.example;

import org.example.dao.CityDAO;
import org.example.dao.ContinentDAO;
import org.example.dao.CountryDAO;
import org.example.dao.DAOFactory;
import org.example.entity.City;
import org.example.entity.Continent;
import org.example.entity.Country;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            // Get DAO instances using factory
            CityDAO cityDAO = DAOFactory.getInstance().getCityDAO();
            CountryDAO countryDAO = DAOFactory.getInstance().getCountryDAO();
            ContinentDAO continentDAO = DAOFactory.getInstance().getContinentDAO();

            // Example usage
            Continent europe = continentDAO.findById(1);
            logger.info("Continent found: " + (europe != null ? europe.getName() : "null"));

            // Find countries in Europe
            List<Country> europeanCountries = countryDAO.findByContinent(1);
            logger.info("Countries in Europe:");
            europeanCountries.forEach(c ->
                    logger.info("- " + c.getName() + " (ID: " + c.getId() + ")"));

            // Find capital cities
            List<City> capitals = cityDAO.findCapitals();
            logger.info("Capital cities:");
            capitals.forEach(c ->
                    logger.info("- " + c.getName() + ", " + c.getCountry().getName()));

            // Calculate distance between two cities
            City bucharest = cityDAO.findById(1);
            City paris = cityDAO.findByNameAndCountry("Paris", "France");
            if (bucharest != null && paris != null) {
                double distance = cityDAO.calculateDistance(bucharest, paris);
                logger.info(String.format("Distance %s - %s: %.2f km",
                        bucharest.getName(), paris.getName(), distance));
            }

            // Find cities with same starting letter and population between bounds
            int lowerBound = 1_000_000;
            int upperBound = 10_000_000;
            logger.info(String.format(
                    "Searching for cities with same starting letter and population between %,d and %,d",
                    lowerBound, upperBound));

            List<City> matchingCities = cityDAO.findCitiesWithSameStartingLetterAndPopulationBetween(
                    lowerBound, upperBound);
            if (!matchingCities.isEmpty()) {
                logger.info("Found matching cities:");
                matchingCities.forEach(c ->
                        logger.info(String.format("- %s (%s), population: %,d",
                                c.getName(), c.getCountry().getName(), c.getPopulation())));
                int totalPop = matchingCities.stream().mapToInt(City::getPopulation).sum();
                logger.info(String.format("Total population: %,d", totalPop));
            } else {
                logger.info("No matching cities found for the given criteria");
            }

        } catch (Exception e) {
            logger.severe("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            PersistenceManager.closeEntityManagerFactory();
            logger.info("Application closed");
        }
    }
}