package org.example.repository;

import jakarta.persistence.TypedQuery;
import org.example.entity.Continent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContinentRepository extends AbstractRepository<Continent, Integer> {
    private static final Logger logger = Logger.getLogger(ContinentRepository.class.getName());

    public ContinentRepository() {
        super(Continent.class);
    }

    public Continent findByName(String name) {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<Continent> query = em.createNamedQuery("Continent.findByName", Continent.class)
                    .setParameter("name", name);
            Continent result = query.getResultList().stream().findFirst().orElse(null);
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findByName in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findByName", e);
            throw e;
        }
    }

    public List<Continent> findAll() {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<Continent> query = em.createNamedQuery("Continent.findAll", Continent.class);
            List<Continent> result = query.getResultList();
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findAll in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findAll", e);
            throw e;
        }
    }
}