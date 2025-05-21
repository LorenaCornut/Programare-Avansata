package org.example.repository;

import jakarta.persistence.TypedQuery;
import org.example.entity.City;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CityRepository extends AbstractRepository<City, Integer> {
    private static final Logger logger = Logger.getLogger(CityRepository.class.getName());

    public CityRepository() {
        super(City.class);
    }

    public City findByNameAndCountry(String cityName, String countryName) {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<City> query = em.createNamedQuery("City.findByNameAndCountry", City.class)
                    .setParameter("cityName", cityName)
                    .setParameter("countryName", countryName);
            City result = query.getResultList().stream().findFirst().orElse(null);
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findByNameAndCountry in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findByNameAndCountry", e);
            throw e;
        }
    }

    public List<City> findByCountry(int countryId) {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<City> query = em.createNamedQuery("City.findByCountry", City.class)
                    .setParameter("countryId", countryId);
            List<City> result = query.getResultList();
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findByCountry in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findByCountry", e);
            throw e;
        }
    }

    public List<City> findCapitals() {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<City> query = em.createNamedQuery("City.findCapitals", City.class);
            List<City> result = query.getResultList();
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findCapitals in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findCapitals", e);
            throw e;
        }
    }

    public List<City> findByNamePattern(String pattern) {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<City> query = em.createQuery(
                            "SELECT c FROM City c WHERE c.name LIKE :pattern", City.class)
                    .setParameter("pattern", "%" + pattern + "%");
            List<City> result = query.getResultList();
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findByNamePattern in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findByNamePattern", e);
            throw e;
        }
    }

    public double calculateDistance(City city1, City city2) {
        long startTime = System.currentTimeMillis();
        try {
            final int R = 6371; // Earth radius in km

            double latDistance = Math.toRadians(city2.getLatitude() - city1.getLatitude());
            double lonDistance = Math.toRadians(city2.getLongitude() - city1.getLongitude());

            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(city1.getLatitude())) * Math.cos(Math.toRadians(city2.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double result = R * c;
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Calculated distance in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error calculating distance", e);
            throw e;
        }
    }
}