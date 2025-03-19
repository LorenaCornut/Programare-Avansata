package org.example;

public class Location implements Comparable<Location>{
    private String name;
    private LocationType type;
    public Location(String name, LocationType type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocationType getType() {
        return type;
    }
    public void setType(LocationType type) {
        this.type = type;
    }
    public boolean isFriendly(){
        return type == LocationType.FRIENDLY;
    }
    public boolean isEnemy(){
        return type == LocationType.ENEMY;
    }
    @Override
    public int compareTo(Location o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
