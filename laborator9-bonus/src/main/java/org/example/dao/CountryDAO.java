package org.example.dao;

import org.example.entity.Country;
import java.util.List;

public interface CountryDAO {
    Country findById(int id);
    Country findByName(String name);
    List<Country> findAll();
    List<Country> findByContinent(int continentId);
    void create(Country country);
    void update(Country country);
    void delete(int id);
}