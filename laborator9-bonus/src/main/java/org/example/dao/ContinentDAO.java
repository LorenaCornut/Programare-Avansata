package org.example.dao;

import org.example.entity.Continent;
import java.util.List;

public interface ContinentDAO {
    Continent findById(int id);
    Continent findByName(String name);
    List<Continent> findAll();
    void create(Continent continent);
    void update(Continent continent);
    void delete(int id);
}