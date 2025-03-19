import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class Solutie {
    private Problema problema;
    private Map<Zbor,Runway> mapare=new HashMap<>();
    public Solutie(Problema problema) {
        this.problema = problema;
    }

    /**
     * Functie care realizeaza atribuirea pistelor pentru vehiculele de zbor pe baza momentelor de star si de sosire a.i. sa nu avem conflicte
     * V-a returna maparea realizata
     */
    public Map<Zbor,Runway> rez(Problema problema)
    {
        List<Runway> runways = problema.getAirport().getRunways(); ///preluam lista de piste
        Set<Zbor> zboruri=problema.getZboruri(); ///preluam lista de zboruri
        for(Zbor z : zboruri) ///parcurgem lista de zboruri
        {
            boolean atribuit=false; ///momentan nu are o pista planificata
            for(Runway r : runways) ///parcurgem lista de piste ca sa vedem pe care i-o putem atribui
            {
                boolean confilct=false; ///pentru a verifica daca pe pista curenta am conflict
                for(Map.Entry<Zbor, Runway> entry : mapare.entrySet()) ///parcurgem zborurile care au lispa atribuita deja si le cautam pe cele planificate pe pista asta
                {
                    if (entry.getValue().equals(r)) /// daca are asociata pista asta
                    {
                        Zbor zborurip = entry.getKey(); ///iau zborul
                         /// daca incepe inainte de terminarea zborului planificat si daca se termina dupa inceputul zborului planificat pe acea pista
                        if(z.getTimp().getFirst().isBefore(zborurip.getTimp().getSecond()) && z.getTimp().getSecond().isAfter(zborurip.getTimp().getFirst()))
                        {
                            confilct=true; ///am conflict
                            break; ///e destul sa am conflict cu un zbor
                        }
                    }
                }
                if(!confilct) /// daca nu am conflict
                {
                    mapare.put(z,r);
                    z.setRunway(r); ///ii atribui pista asta
                    atribuit=true; ///am reusit sa ii atribuim
                    break; ///daca am reusit sa ii atribuim iesim
                }
            }
            if(!atribuit)
            {
                System.out.println("Zborului " + z.getId_zbor() + " nu i se poate aloca o pista.");
            }
        }
        return mapare;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Zbor, Runway> entry : mapare.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
