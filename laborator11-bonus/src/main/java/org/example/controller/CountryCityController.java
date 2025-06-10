package org.example.controller;

import org.example.entity.City;
import org.example.entity.Country;
import org.example.repository.CountryRepository;
import org.example.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@Tag(name = "Country & City API", description = "Manage countries and cities")
public class CountryCityController {

    private static final Logger log = LoggerFactory.getLogger(CountryCityController.class);

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CityService cityService;

    @Operation(summary = "Get all countries")
    @GetMapping("/countries")
    public List<Country> getCountries() {
        log.info("Received request for /countries endpoint");
        try {
            log.info("Validating token for /countries endpoint");
            List<Country> countries = countryRepo.findAll();
            log.info("Returning {} countries", countries.size());
            return countries;
        } catch (Exception e) {
            log.error("Error while fetching countries: {}", e.getMessage(), e);
            throw e; // Re-throw the exception to ensure proper error handling
        }
    }


    @Operation(summary = "Get all cities")
    @GetMapping("/cities")
    public List<City> getCities() {
        return cityService.findAll();
    }

    @Operation(summary = "Add a new city")
    @PostMapping("/cities")
    public City addCity(@RequestParam String name, @RequestParam Long countryId) {
        return cityService.addCity(name, countryId);
    }

    @Operation(summary = "Update an existing city")
    @PutMapping("/cities/{id}")
    public City updateCity(@PathVariable Long id, @RequestParam String name) {
        return cityService.updateCity(id, name);
    }

    @Operation(summary = "Delete a city")
    @DeleteMapping("/cities/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }
}
