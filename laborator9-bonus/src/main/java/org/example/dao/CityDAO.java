package org.example.dao;

import org.example.entity.City;
import java.util.List;

public interface CityDAO {
    City findById(int id);
    City findByNameAndCountry(String cityName, String countryName);
    List<City> findByCountry(int countryId);
    List<City> findCapitals();
    List<City> findByNamePattern(String pattern);
    double calculateDistance(City city1, City city2);
    List<City> findCitiesWithSameStartingLetterAndPopulationBetween(int lowerBound, int upperBound);
    List<City> findByPopulationRange(int min, int max);
    void create(City city);
    void update(City city);
    void delete(int id);
}