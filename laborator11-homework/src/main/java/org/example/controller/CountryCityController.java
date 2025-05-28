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

@RestController
@RequestMapping("/api")
@Tag(name = "Country & City API", description = "Manage countries and cities")
public class CountryCityController {

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CityService cityService;

    @Operation(summary = "Get all countries")
    @GetMapping("/countries")
    public List<Country> getCountries() {
        return countryRepo.findAll();
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
