package org.example.repository;

import jakarta.persistence.TypedQuery;
import org.example.entity.Country;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountryRepository extends AbstractRepository<Country, Integer> {
    private static final Logger logger = Logger.getLogger(CountryRepository.class.getName());

    public CountryRepository() {
        super(Country.class);
    }

    public Country findByName(String name) {
        long startTime = System.currentTimeMillis();
        try {
            TypedQuery<Country> query = em.createNamedQuery("Country.findByName", Country.class)
                    .setParameter("name", name);
            Country result = query.getResultList().stream().findFirst().orElse(null);
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.INFO, "Executed findByName in {0} ms", duration);
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in findByName", e);
            throw e;
        }
    }

    public List<Country> findByContinent(int continentId) {
        logger.entering(getClass().getName(), "findByContinent", continentId);
        try {
            List<Country> result = em.createNamedQuery("Country.findByContinent", Country.class)
                    .setParameter("continentId", continentId)
                    .getResultList();
            logger.log(Level.INFO, "Găsite {0} țări pentru continentId {1}",
                    new Object[]{result.size(), continentId});
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Eroare la findByContinent", e);
            throw e;
        } finally {
            logger.exiting(getClass().getName(), "findByContinent");
        }
    }
}