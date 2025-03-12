import java.util.*;
public class Airport {
    private String name;
    private List<Runway> runways = new ArrayList<>(); ///aeroporul contine o lista de piste
    /**
     * Constructor pentru Airport
     * @param name
     */
    public Airport(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Runway> getRunways() {
        return runways;
    }
    public void addRunway(Runway runway) {
        runways.add(runway);
    }

    /**
     * Suprascriem metoda toString pentru afisare
     * @return
     */
    @Override
    public String toString() {
        return "Airport: " + name + " with runways " + runways;
    }
}
