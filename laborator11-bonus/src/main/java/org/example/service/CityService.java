package org.example.service;

import org.example.entity.City;
import org.example.entity.Country;
import org.example.repository.CityRepository;
import org.example.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private CountryRepository countryRepo;

    public List<City> findAll() {
        return cityRepo.findAll();
    }

    public City addCity(String name, Long countryId) {
        Country country = countryRepo.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found"));
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        return cityRepo.save(city);
    }

    public City updateCity(Long id, String newName) {
        City city = cityRepo.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        city.setName(newName);
        return cityRepo.save(city);
    }

    public void deleteCity(Long id) {
        cityRepo.deleteById(id);
    }
}

