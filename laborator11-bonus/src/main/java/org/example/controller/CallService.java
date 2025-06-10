package org.example.controller;

import org.example.entity.City;
import org.example.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class CallService {

    private final Logger log = LoggerFactory.getLogger(CallService.class);
    private final String baseUrl = "http://localhost:8081/api";
    private final RestTemplate restTemplate = new RestTemplate();
    private String token; // Store the token

    public void setToken(String token) {
        this.token = token;
        if (token != null && !token.isEmpty()) {
            log.info("Token successfully set: {}", token);
        } else {
            log.warn("Token is null or empty. Requests will fail.");
        }
    }

    private HttpEntity<?> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
            log.info("Authorization header set with token: Bearer " + token);
        } else {
            log.warn("Authorization header not set. Token is null.");
        }
        return new HttpEntity<>(headers);
    }

    @GetMapping("/countries")
    public List<Country> getCountries() {
        log.info("Start getCountries");
        ResponseEntity<List<Country>> response = restTemplate.exchange(
                baseUrl + "/countries",
                HttpMethod.GET,
                createHttpEntity(),
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
                createHttpEntity(),
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
        HttpEntity<?> entity = createHttpEntity();
        ResponseEntity<City> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                City.class
        );
        City city = response.getBody();
        log.info("Added city: " + city);
        return city;
    }

    @PutMapping("/cities/{id}")
    public City updateCity(@PathVariable Long id, @RequestParam String name) {
        log.info("Updating city ID: " + id + " with new name: " + name);
        String url = baseUrl + "/cities/" + id + "?name=" + name;
        HttpEntity<?> entity = createHttpEntity();
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
        return getCityById(id);
    }

    @DeleteMapping("/cities/{id}")
    public void deleteCity(@PathVariable Long id) {
        log.info("Deleting city ID: " + id);
        String url = baseUrl + "/cities/" + id;
        HttpEntity<?> entity = createHttpEntity();
        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
        log.info("Deleted city ID: " + id);
    }

    private City getCityById(Long id) {
        List<City> cities = getCities();
        return cities.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public String authenticate(String username, String password) {
        String url = "http://localhost:8081/auth/login";
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        return (String) restTemplate.postForObject(url, request, Map.class).get("token");
    }
}
