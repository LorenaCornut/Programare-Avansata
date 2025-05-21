package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceManager {
    private static EntityManagerFactory emf;
    private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                Properties props = new Properties();
                try (InputStream input = PersistenceManager.class.getClassLoader()
                        .getResourceAsStream("config.properties")) {
                    props.load(input);
                }

                emf = Persistence.createEntityManagerFactory("WorldPU", props);
                logger.info("EntityManagerFactory created successfully");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to create EntityManagerFactory", e);
                throw new RuntimeException("Failed to initialize JPA", e);
            }
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