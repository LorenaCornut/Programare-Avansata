package org.example;

import jakarta.persistence.TypedQuery;
import java.util.List;

public class CountryRepository extends AbstractRepository<Country> {
    public CountryRepository() {
        super(Country.class);
    }

    public Country findByName(String name) {
        TypedQuery<Country> query = em.createNamedQuery("Country.findByName", Country.class)
                .setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<Country> findByContinent(int continentId) {
        return em.createNamedQuery("Country.findByContinent", Country.class)
                .setParameter("continentId", continentId)
                .getResultList();
    }
}