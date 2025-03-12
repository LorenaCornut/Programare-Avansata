import java.util.*;
public class Main {
    public static void main(String[] args) {
        Airport airport = new Airport("International Airport");
        airport.addRunway(new Runway("A1"));
        airport.addRunway(new Runway("B2"));

        List<Aircraft> aircraftList = new ArrayList<>(); ///creez o lista de Aircraft
        aircraftList.add(new Airliner("Ana", "N12345", 400, 2000));
        aircraftList.add(new Freighter("Mihail", "F67890", 5000));
        aircraftList.add(new Drone("Vasile", "D11111", 2.5, 50));
        aircraftList.add(new Airliner("Ion", "N54321", 180, 1000));
        Collections.sort(aircraftList);
        System.out.println("Ordonare:");
        for (Aircraft a : aircraftList) {
            System.out.println(a);
        }
        System.out.println(airport);
    }
}