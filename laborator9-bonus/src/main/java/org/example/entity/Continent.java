package org.example.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "continents")
@NamedQueries({
        @NamedQuery(name = "Continent.findAll", query = "SELECT c FROM Continent c"),
        @NamedQuery(name = "Continent.findByName", query = "SELECT c FROM Continent c WHERE c.name = :name")
})
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "continents_seq")
    @SequenceGenerator(name = "continents_seq", sequenceName = "continents_seq", allocationSize = 1)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "continent")
    private List<Country> countries;

    public Continent() {}

    public Continent(String name) {
        this.name = name;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Country> getCountries() { return countries; }
    public void setCountries(List<Country> countries) { this.countries = countries; }
}