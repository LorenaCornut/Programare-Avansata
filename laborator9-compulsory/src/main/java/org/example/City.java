package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "cities")
@NamedQueries({
        @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c"),
        @NamedQuery(name = "City.findByNameAndCountry",
                query = "SELECT c FROM City c WHERE c.name = :cityName AND c.country.name = :countryName"),
        @NamedQuery(name = "City.findByCountry",
                query = "SELECT c FROM City c WHERE c.country.id = :countryId"),
        @NamedQuery(name = "City.findCapitals",
                query = "SELECT c FROM City c WHERE c.capital = true")
})
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_seq", allocationSize = 1)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "is_capital")
    private boolean capital;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    // Constructors, getters, setters
    public City() {}

    public City(String name, Country country, boolean capital, double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public boolean isCapital() { return capital; }
    public void setCapital(boolean capital) { this.capital = capital; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}