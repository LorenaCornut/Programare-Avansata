import java.time.LocalTime;

public class Zbor {
    private String id_zbor;
   /// private Aircraft aircraft; ///vehiculul
    private TimeInterval timp;
    private Runway runway; ///pista care ii este alocata
    /**
     * Constructor pentru Zbor
     * @param id_zbor
     * @param timp
     */
    public Zbor(String id_zbor,/* Aircraft aircraft,*/ TimeInterval timp) {
        this.id_zbor = id_zbor;
       /// this.aircraft = aircraft;
        this.timp = timp;
    }
    public String getId_zbor() {
        return id_zbor;
    }
    public void setId_zbor(String id_zbor) {
        this.id_zbor = id_zbor;
    }
   /* public Aircraft getAircraft() {
        return aircraft;
    }
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }*/
    public TimeInterval getTimp() { return timp; }
    public void setTimp(TimeInterval timp) { this.timp = timp; }
    public Runway getRunway() {
        return runway;
    }
    public void setRunway(Runway runway) {
        this.runway = runway;
    }

    @Override
    public String toString() {
        return "Zbor: " + id_zbor  +
               /// " vehicul de zbor: " + aircraft  +
                " timp: " + timp +
                " pista: " + runway + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zbor zbor = (Zbor) o;
        return id_zbor.equals(zbor.id_zbor);
    }

    @Override
    public int hashCode() {
        return id_zbor.hashCode();
    }
}
