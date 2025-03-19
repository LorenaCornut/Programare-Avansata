import java.util.*;

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
        List<Runway> runways = problema.getAirport().getRunways();
        Set<Zbor> zborurii=problema.getZboruri();
        //zboruri.sort(Comparator.comparing(zbor -> zbor.getTimp().getStart()));
        List<Zbor> zboruri=new ArrayList<>(zborurii);
        zboruri.sort(Comparator.comparing(zbor -> zbor.getTimp().getStart()));
        for(Zbor z : zboruri)
        {
            boolean atribuit=false;
            for(Runway r : runways)
            {
                boolean confilct=false;
                for(Map.Entry<Zbor, Runway> entry : mapare.entrySet())
                {
                    if (entry.getValue().equals(r))
                    {
                        Zbor zborurip = entry.getKey();
                         /// daca incepe inainte de terminarea zborului planificat si daca se termina dupa inceputul zborului planificat pe acea pista
                        if(z.getTimp().getStart().isBefore(zborurip.getTimp().getSecond()) && z.getTimp().getSecond().isAfter(zborurip.getTimp().getFirst()))
                        {
                            confilct=true;
                            break;
                        }
                    }
                }
                if(!confilct)
                {
                    mapare.put(z,r);
                    z.setRunway(r);
                    atribuit=true;
                    break;
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
