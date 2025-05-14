package org.example;

import org.example.entity.City;
import org.example.entity.Continent;
import org.example.entity.Country;
import org.example.repository.CityRepository;
import org.example.repository.ContinentRepository;
import org.example.repository.CountryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        setupLogger();

        try {
            logger.info("Aplicația a pornit");

            ContinentRepository continentRepo = new ContinentRepository();
            CountryRepository countryRepo = new CountryRepository();
            CityRepository cityRepo = new CityRepository();

            Continent europe = continentRepo.findById(1);
            logger.info("Continent găsit: " + (europe != null ? europe.getName() : "null"));

            logger.info("Țări din Europa:");
            countryRepo.findByContinent(1).forEach(c ->
                    logger.info("- " + c.getName() + " (ID: " + c.getId() + ")"));

            logger.info("Orașe capitale:");
            cityRepo.findCapitals().forEach(c ->
                    logger.info("- " + c.getName() + ", " + c.getCountry().getName()));

            City bucharest = cityRepo.findById(1);
            City paris = cityRepo.findByNameAndCountry("Paris", "France");

            if(bucharest != null && paris != null) {
                double distance = cityRepo.calculateDistance(bucharest, paris);
                logger.info(String.format("Distanța %s - %s: %.2f km",
                        bucharest.getName(), paris.getName(), distance));
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Eroare gravă în aplicație", e);
        } finally {
            PersistenceManager.closeEntityManagerFactory();
            logger.info("Aplicația s-a închis");
        }
    }

    private static void setupLogger() {
        try {
            Files.deleteIfExists(Paths.get("application.log"));

            Logger rootLogger = Logger.getLogger("");

            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = new FileHandler("application.log", false); // false = nu adăuga la existent
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$-7s] %3$s: %5$s%6$s%n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            lr.getMillis(),
                            lr.getSourceClassName(),
                            lr.getSourceMethodName(),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage(),
                            lr.getThrown() != null ? "\n" + lr.getThrown() : "");
                }
            });
            rootLogger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(consoleHandler);

            logger.config("Logger configurat cu succes. Fișier log: " +
                    Paths.get("application.log").toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Eroare la configurarea logger-ului: " + e.getMessage());
            e.printStackTrace();
        }
    }
}