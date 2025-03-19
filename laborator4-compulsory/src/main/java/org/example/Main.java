package org.example;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static LocationType randomType(){
        Random rand = new Random();
        LocationType[] enumTypes = LocationType.values();
        return enumTypes[rand.nextInt(enumTypes.length)];
    }
    public static void main(String[] args) {
        ///slide 3
        Location[] locations = IntStream.rangeClosed(0, 10)
                .mapToObj(i -> new Location("P" + i, randomType()))
                .toArray(Location[]::new);
        ///-----
        for (Location location : locations) { ///o afisare
            System.out.println(location);
        }
        Set<Location> friendlyLocations = Arrays.stream(locations)
                .filter(Location::isFriendly)
                .collect(Collectors.toCollection(TreeSet::new));
        ///slide 5 jos
        List<Location> enemyLocations = Arrays.stream(locations)
                .filter(Location::isEnemy)
                .sorted(Comparator.comparing(Location::getType).thenComparing(Location::getName))
                .collect(Collectors.toCollection(LinkedList::new));
        ///---------
        System.out.println("Friendly Locations:");
        friendlyLocations.forEach(System.out::println);

        System.out.println("\nEnemy Locations:");
        enemyLocations.forEach(System.out::println);

    }
}
