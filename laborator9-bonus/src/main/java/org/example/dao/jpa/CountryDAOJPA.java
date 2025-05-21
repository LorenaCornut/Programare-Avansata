package org.example.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.dao.CountryDAO;
import org.example.entity.Country;
import java.util.List;

public class CountryDAOJPA implements CountryDAO {
    private final EntityManager em;

    public CountryDAOJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Country findById(int id) {
        return em.find(Country.class, id);
    }

    @Override
    public Country findByName(String name) {
        TypedQuery<Country> query = em.createNamedQuery("Country.findByName", Country.class)
                .setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Country> findAll() {
        return em.createNamedQuery("Country.findAll", Country.class).getResultList();
    }

    @Override
    public List<Country> findByContinent(int continentId) {
        return em.createNamedQuery("Country.findByContinent", Country.class)
                .setParameter("continentId", continentId)
                .getResultList();
    }

    @Override
    public void create(Country country) {
        em.getTransaction().begin();
        em.persist(country);
        em.getTransaction().commit();
    }

    @Override
    public void update(Country country) {
        em.getTransaction().begin();
        em.merge(country);
        em.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        em.getTransaction().begin();
        Country country = em.find(Country.class, id);
        if (country != null) {
            em.remove(country);
        }
        em.getTransaction().commit();
    }
}