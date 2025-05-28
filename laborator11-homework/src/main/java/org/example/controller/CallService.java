package org.example.controller;

import org.example.entity.City;
import org.example.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/client")
public class CallService {

    private final Logger log = LoggerFactory.getLogger(CallService.class);
    private final String baseUrl = "http://localhost:8081/api";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/countries")
    public List<Country> getCountries() {
        log.info("Start getCountries");
        ResponseEntity<List<Country>> response = restTemplate.exchange(
                baseUrl + "/countries",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<Country> result = response.getBody();
        if (result != null) {
            result.forEach(c -> log.info(c.toString()));
        }
        log.info("Stop getCountries");
        return result;
    }

    @GetMapping("/cities")
    public List<City> getCities() {
        log.info("Start getCities");
        ResponseEntity<List<City>> response = restTemplate.exchange(
                baseUrl + "/cities",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<City> result = response.getBody();
        if (result != null) {
            result.forEach(c -> log.info(c.toString()));
        }
        log.info("Stop getCities");
        return result;
    }

    @PostMapping("/cities")
    public City addCity(@RequestParam String name, @RequestParam Long countryId) {
        log.info("Adding city: " + name + " in country ID: " + countryId);
        String url = baseUrl + "/cities?name=" + name + "&countryId=" + countryId;
        City city = restTemplate.postForObject(url, null, City.class);
        log.info("Added city: " + city);
        return city;
    }

    @PutMapping("/cities/{id}")
    public City updateCity(@PathVariable Long id, @RequestParam String name) {
        log.info("Updating city ID: " + id + " with new name: " + name);
        String url = baseUrl + "/cities/" + id + "?name=" + name;
        restTemplate.put(url, null);
        return getCityById(id);
    }

    @DeleteMapping("/cities/{id}")
    public void deleteCity(@PathVariable Long id) {
        log.info("Deleting city ID: " + id);
        restTemplate.delete(baseUrl + "/cities/" + id);
        log.info("Deleted city ID: " + id);
    }

    private City getCityById(Long id) {
        List<City> cities = getCities();
        return cities.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
