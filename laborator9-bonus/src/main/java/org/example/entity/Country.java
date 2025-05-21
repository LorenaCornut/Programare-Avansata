package org.example.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "countries")
@NamedQueries({
        @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c"),
        @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name"),
        @NamedQuery(name = "Country.findByContinent",
                query = "SELECT c FROM Country c WHERE c.continent.id = :continentId")
})
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_seq", allocationSize = 1)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "continent_id", nullable = false)
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private List<City> cities;

    public Country() {}

    public Country(String name, Continent continent) {
        this.name = name;
        this.continent = continent;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Continent getContinent() { return continent; }
    public void setContinent(Continent continent) { this.continent = continent; }
    public List<City> getCities() { return cities; }
    public void setCities(List<City> cities) { this.cities = cities; }
}