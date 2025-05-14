package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PersistenceManager {
    private static EntityManagerFactory emf;
    private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());

    static {
        try {
            InputStream config = PersistenceManager.class.getClassLoader()
                    .getResourceAsStream("logging.properties");
            if (config != null) {
                LogManager.getLogManager().readConfiguration(config);
            }
        } catch (Exception e) {
            System.err.println("Could not load logging configuration: " + e.getMessage());
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            long startTime = System.currentTimeMillis();
            emf = Persistence.createEntityManagerFactory("WorldPU");
            logger.log(Level.INFO, "EntityManagerFactory created in {0} ms",
                    System.currentTimeMillis() - startTime);
        }
        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            logger.info("EntityManagerFactory closed");
        }
    }
}