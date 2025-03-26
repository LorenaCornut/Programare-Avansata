import java.time.LocalTime;
import java.util.*;
public class Problema {
    private Airport airport; ///aeroportul
    private Set<Zbor> zboruri; ///lista de zboruri
    /**
     * Constructor pentru problema
     * @param airport
     * @param zboruri
     */
    public Problema(Airport airport, Set<Zbor> zboruri) {
        this.airport = airport;
        this.zboruri = zboruri;
    }
    public Airport getAirport() {
        return airport;
    }
    public Set<Zbor> getZboruri() {
        return zboruri;
    }
    public void setZboruri(Set<Zbor> zboruri) {
        this.zboruri = zboruri;
    }
    public void setAirport(Airport airport) {
        this.airport = airport;
    }
}
