import java.util.*;

public class Graph {
    private Map<Zbor, Set<Zbor>> lista = new HashMap<>();
    public void addNode(Zbor zbor) {
        lista.putIfAbsent(zbor, new HashSet<>());
    }

    public void addEdge(Zbor zbor1, Zbor zbor2) {
        lista.get(zbor1).add(zbor2);
        lista.get(zbor2).add(zbor1);
    }

    public Set<Zbor> getNodes() { /// nodurile sunt cheile
        return lista.keySet();
    }

    public Set<Zbor> getNeighbors(Zbor zbor) {
        return lista.getOrDefault(zbor, new HashSet<>());
    }

    public int getDegree(Zbor zbor) { /// gradul unui nod= nr de elemente din lista lui de vecini
        return getNeighbors(zbor).size();
    }
}