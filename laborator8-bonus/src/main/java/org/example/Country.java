package org.example;

public class Country {
    private int id;
    private String name;
    private int continentId;

    public Country() {}
    public Country(String name, int continentId) {
        this.name = name;
        this.continentId = continentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getContinentId() { return continentId; }
    public void setContinentId(int continentId) { this.continentId = continentId; }
}