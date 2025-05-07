package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class DataImport {
    public static void importCapitals() throws IOException, SQLException {
        InputStream is = DataImport.class.getClassLoader().getResourceAsStream("world-capitals.csv");
        if (is == null) {
            throw new IOException("Could not find world-capitals.csv in resources");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // Skip header

            ContinentDAO continentDAO = new ContinentDAO();
            CountryDAO countryDAO = new CountryDAO();
            CityDAO cityDAO = new CityDAO();

            while ((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (data.length < 5) {
                        System.err.println("Skipping incomplete line: " + line);
                        continue;
                    }

                    String countryName = cleanField(data[0]);
                    String cityName = cleanField(data[1]);
                    String latitudeStr = cleanField(data[2]);
                    String longitudeStr = cleanField(data[3]);
                    String continentName = cleanField(data[4]);

                    if (!isNumeric(latitudeStr) || !isNumeric(longitudeStr)) {
                        System.err.println("Skipping line with invalid coordinates: " + line);
                        continue;
                    }

                    double latitude = Double.parseDouble(latitudeStr);
                    double longitude = Double.parseDouble(longitudeStr);

                    // Process continent
                    Continent continent = continentDAO.findByName(continentName);
                    if (continent == null) {
                        continent = new Continent(continentName);
                        continentDAO.create(continent);
                        continent = continentDAO.findByName(continentName);
                    }

                    // Process country
                    Country country = countryDAO.findByName(countryName);
                    if (country == null) {
                        country = new Country(countryName, continent.getId());
                        countryDAO.create(country);
                        country = countryDAO.findByName(countryName);
                    }

                    // Add city if it doesn't exist
                    City existingCity = cityDAO.findByNameAndCountry(cityName, countryName);
                    if (existingCity == null) {
                        cityDAO.create(new City(cityName, country.getId(), true, latitude, longitude));
                        System.out.println("Imported: " + cityName + ", " + countryName);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }
        }
    }

    private static String cleanField(String field) {
        return field.trim().replaceAll("^\"|\"$", "");
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}