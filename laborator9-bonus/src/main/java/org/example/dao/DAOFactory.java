package org.example.dao;

import org.example.PersistenceManager;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.dao.jdbc.CityDAOJDBC;
import org.example.dao.jdbc.CountryDAOJDBC;
import org.example.dao.jpa.CityDAOJPA;
import java.sql.*;
import org.example.dao.jpa.CountryDAOJPA;
import org.example.dao.jpa.ContinentDAOJPA;
import org.example.dao.jdbc.CountryDAOJDBC;
import org.example.dao.jdbc.ContinentDAOJDBC;

public abstract class DAOFactory {
    private static final Logger logger = Logger.getLogger(DAOFactory.class.getName());
    private static String accessType;

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        try (InputStream input = DAOFactory.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            accessType = prop.getProperty("data.access", "JPA").toUpperCase();
            logger.log(Level.INFO, "Using data access type: {0}", accessType);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading config.properties", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static DAOFactory getInstance() {
        return accessType.equals("JDBC") ? new JDBCDAOFactory() : new JPADAOFactory();
    }

    public abstract CityDAO getCityDAO();
    public abstract CountryDAO getCountryDAO();
    public abstract ContinentDAO getContinentDAO();

    private static class JPADAOFactory extends DAOFactory {
        @Override
        public CityDAO getCityDAO() {
            return new CityDAOJPA(PersistenceManager.getEntityManagerFactory().createEntityManager());
        }

        @Override
        public CountryDAO getCountryDAO() {
            return new CountryDAOJPA(PersistenceManager.getEntityManagerFactory().createEntityManager());
        }

        @Override
        public ContinentDAO getContinentDAO() {
            return new ContinentDAOJPA(PersistenceManager.getEntityManagerFactory().createEntityManager());
        }
    }

    private static class JDBCDAOFactory extends DAOFactory {
        private Connection getConnection() throws SQLException {
            Properties props = new Properties();
            try (InputStream input = DAOFactory.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                props.load(input);
                Class.forName(props.getProperty("jdbc.driver"));
                return DriverManager.getConnection(
                        props.getProperty("jdbc.url"),
                        props.getProperty("jdbc.user"),
                        props.getProperty("jdbc.password"));
            } catch (IOException | ClassNotFoundException e) {
                throw new SQLException("Failed to create JDBC connection", e);
            }
        }

        @Override
        public CityDAO getCityDAO() {
            try {
                return new CityDAOJDBC(getConnection());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create CityDAO", e);
            }
        }

        @Override
        public CountryDAO getCountryDAO() {
            try {
                return new CountryDAOJDBC(getConnection());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create CountryDAO", e);
            }
        }

        @Override
        public ContinentDAO getContinentDAO() {
            try {
                return new ContinentDAOJDBC(getConnection());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create ContinentDAO", e);
            }
        }
    }
}