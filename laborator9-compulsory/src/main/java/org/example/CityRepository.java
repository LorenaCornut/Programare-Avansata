package org.example;

import jakarta.persistence.TypedQuery;
import java.util.List;

public class CityRepository extends AbstractRepository<City> {
    public CityRepository() {
        super(City.class);
    }

    public City findByNameAndCountry(String cityName, String countryName) {
        TypedQuery<City> query = em.createNamedQuery("City.findByNameAndCountry", City.class)
                .setParameter("cityName", cityName)
                .setParameter("countryName", countryName);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<City> findByCountry(int countryId) {
        return em.createNamedQuery("City.findByCountry", City.class)
                .setParameter("countryId", countryId)
                .getResultList();
    }

    public List<City> findCapitals() {
        return em.createNamedQuery("City.findCapitals", City.class).getResultList();
    }

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
}