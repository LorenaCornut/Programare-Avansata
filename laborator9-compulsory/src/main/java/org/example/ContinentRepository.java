package org.example;

import jakarta.persistence.TypedQuery;
import java.util.List;

public class ContinentRepository extends AbstractRepository<Continent> {
    public ContinentRepository() {
        super(Continent.class);
    }

    public Continent findByName(String name) {
        TypedQuery<Continent> query = em.createNamedQuery("Continent.findByName", Continent.class)
                .setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<Continent> findAll() {
        return em.createNamedQuery("Continent.findAll", Continent.class).getResultList();
    }
}