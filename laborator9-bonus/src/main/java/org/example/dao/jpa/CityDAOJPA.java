package org.example.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.dao.CityDAO;
import org.example.entity.City;
import org.example.util.CitySolver;
import java.util.List;
import java.util.logging.Logger;

public class CityDAOJPA implements CityDAO {
    private static final Logger logger = Logger.getLogger(CityDAOJPA.class.getName());
    private final EntityManager em;

    public CityDAOJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public City findById(int id) {
        return em.find(City.class, id);
    }

    @Override
    public City findByNameAndCountry(String cityName, String countryName) {
        TypedQuery<City> query = em.createNamedQuery("City.findByNameAndCountry", City.class)
                .setParameter("cityName", cityName)
                .setParameter("countryName", countryName);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<City> findByCountry(int countryId) {
        return em.createNamedQuery("City.findByCountry", City.class)
                .setParameter("countryId", countryId)
                .getResultList();
    }

    @Override
    public List<City> findCapitals() {
        return em.createNamedQuery("City.findCapitals", City.class).getResultList();
    }

    @Override
    public List<City> findByNamePattern(String pattern) {
        return em.createQuery("SELECT c FROM City c WHERE c.name LIKE :pattern", City.class)
                .setParameter("pattern", "%" + pattern + "%")
                .getResultList();
    }

    @Override
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

    @Override
    public List<City> findCitiesWithSameStartingLetterAndPopulationBetween(int lowerBound, int upperBound) {
        List<City> allCities = em.createQuery("SELECT c FROM City c", City.class).getResultList();
        return CitySolver.findMatchingCities(allCities, lowerBound, upperBound);
    }

    @Override
    public List<City> findByPopulationRange(int min, int max) {
        return em.createNamedQuery("City.findByPopulationRange", City.class)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }

    @Override
    public void create(City city) {
        em.getTransaction().begin();
        em.persist(city);
        em.getTransaction().commit();
    }

    @Override
    public void update(City city) {
        em.getTransaction().begin();
        em.merge(city);
        em.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        em.getTransaction().begin();
        City city = em.find(City.class, id);
        if (city != null) {
            em.remove(city);
        }
        em.getTransaction().commit();
    }
}