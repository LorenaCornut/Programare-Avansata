package org.example.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "continents")
public class Continent {

    @Id
    @SequenceGenerator(name = "continent_seq", sequenceName = "continent_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "continent_seq")
    private Long id;

    private String name;

    public Continent() {}

    public Continent(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
