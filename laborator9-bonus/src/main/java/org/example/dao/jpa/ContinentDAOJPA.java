package org.example.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.dao.ContinentDAO;
import org.example.entity.Continent;
import java.util.List;

public class ContinentDAOJPA implements ContinentDAO {
    private final EntityManager em;

    public ContinentDAOJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Continent findById(int id) {
        return em.find(Continent.class, id);
    }

    @Override
    public Continent findByName(String name) {
        TypedQuery<Continent> query = em.createNamedQuery("Continent.findByName", Continent.class)
                .setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Continent> findAll() {
        return em.createNamedQuery("Continent.findAll", Continent.class).getResultList();
    }

    @Override
    public void create(Continent continent) {
        em.getTransaction().begin();
        em.persist(continent);
        em.getTransaction().commit();
    }

    @Override
    public void update(Continent continent) {
        em.getTransaction().begin();
        em.merge(continent);
        em.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        em.getTransaction().begin();
        Continent continent = em.find(Continent.class, id);
        if (continent != null) {
            em.remove(continent);
        }
        em.getTransaction().commit();
    }
}