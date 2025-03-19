import java.time.LocalTime;
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
        ///Collections.sort(aircraftList);
        Set<Zbor> zboruri = new HashSet<>();
        zboruri.add(new Zbor("F1", aircraftList.get(0), new TimeInterval(LocalTime.of(10,0),LocalTime.of(10,30))));
        zboruri.add(new Zbor("F2", aircraftList.get(1),  new TimeInterval(LocalTime.of(10,15),LocalTime.of(10,45))));
        zboruri.add(new Zbor("F3", aircraftList.get(2),  new TimeInterval(LocalTime.of(11,0),LocalTime.of(11,30))));
        zboruri.add(new Zbor("F4", aircraftList.get(3),  new TimeInterval(LocalTime.of(10,30),LocalTime.of(11,0))));
        Problema problema = new Problema(airport, zboruri);
        Solutie sol=new Solutie(problema);
        Map<Zbor, Runway> schedule = sol.rez(problema);
        System.out.println(sol);

    }
}