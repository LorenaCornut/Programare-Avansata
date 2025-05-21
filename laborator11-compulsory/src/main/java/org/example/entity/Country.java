package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @SequenceGenerator(name = "country_seq", sequenceName = "country_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")

    private Long id;

    private String name;

    private String code;

    @ManyToOne
    @JoinColumn(name = "continent_id", referencedColumnName = "id")
    private Continent continent;

    public Country() {}

    public Country(Long id, String name, String code, Continent continent) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.continent = continent;
    }

    // Getteri și setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}
