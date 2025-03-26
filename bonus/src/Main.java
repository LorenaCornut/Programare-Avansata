import java.time.LocalTime;
import java.util.*;
public class Main {
    public static void randomizare(List<Zbor> flights, List<Runway> runways) {
        Random rand = new Random();
        int numberRunways = rand.nextInt(3) + 3;
        int numberFlights = rand.nextInt(3) + 3;

        for (int i = 0; i < numberRunways; i++) {
            runways.add(new Runway(i));
        }

        for (int i = 0; i < numberFlights; i++) {
            int hour = rand.nextInt(24);
            int minute = rand.nextInt(60);
            LocalTime time = LocalTime.of(hour, minute);

            int hour2 = rand.nextInt(24 - hour) + hour;
            int minute2 = rand.nextInt(60 - minute) + minute;
            LocalTime time2 = LocalTime.of(hour2, minute2);

            TimeInterval timeInterval = new TimeInterval(time, time2);
            flights.add(new Zbor("P" + i, timeInterval));
        }
    }

    public static void main(String[] args) {
        List<Zbor> zboruri = new ArrayList<>();
        List<Runway> piste = new ArrayList<>();
        randomizare(zboruri, piste);
        Airport airport = new Airport("Aeroport International");
        for (Runway runway : piste) {
            airport.addRunway(runway);
        }
        Problema problema = new Problema(airport, new HashSet<>(zboruri));
        Solutie solutie = new Solutie(problema);
        Map<Zbor, Runway> programare = solutie.scheduleEquitably();
        if (programare != null) {
            System.out.println("Zbor -> Pista:");
            programare.forEach((zbor, runway) ->
                    System.out.println(zbor.getId_zbor() + " -> " + runway.getId()));
            Map<Runway, Integer> utilizare = new HashMap<>();
            piste.forEach(p -> utilizare.put(p, 0));
            programare.values().forEach(p -> utilizare.put(p, utilizare.get(p) + 1));
            System.out.println("\nUtilizare piste:");
            utilizare.forEach((p, n) -> System.out.println(p.getId() + ": " + n + " zboruri"));
        } else {
            System.out.println("Nu s-a putut realiza programarea!");
        }
    }
}