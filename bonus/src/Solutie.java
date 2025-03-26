import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Solutie {
    private Problema problema;
    private Graph conflictGraph;
    private Map<Zbor, Runway> schedule;

    public Solutie(Problema problema) {
        this.problema = problema;
        this.conflictGraph = buildConflictGraph();
        this.schedule = new HashMap<>();
    }
    private Graph buildConflictGraph() {
        Graph graph = new Graph();
        List<Zbor> zboruri = new ArrayList<>(problema.getZboruri());

        for (Zbor zbor : zboruri) {
            graph.addNode(zbor);
        }

        for (int i = 0; i < zboruri.size(); i++) {
            for (int j = i + 1; j < zboruri.size(); j++) {
                Zbor z1 = zboruri.get(i);
                Zbor z2 = zboruri.get(j);
                if (isConflict(z1, z2)) {
                    graph.addEdge(z1, z2);
                }
            }
        }

        return graph;
    }

    private boolean isConflict(Zbor z1, Zbor z2) {
        TimeInterval t1 = z1.getTimp();
        TimeInterval t2 = z2.getTimp();
        return t1.getStart().isBefore(t2.getEnd()) && t1.getEnd().isAfter(t2.getStart());
    }

    public Map<Zbor, Runway> scheduleEquitably() {
        List<Runway> runways = problema.getAirport().getRunways();
        int numRunways = runways.size();
        List<Zbor> flights = new ArrayList<>(conflictGraph.getNodes());
        flights.sort((f1, f2) -> conflictGraph.getDegree(f2) - conflictGraph.getDegree(f1));
        Map<Zbor, Integer> colorAssignment = new HashMap<>();
        int maxColor = numRunways - 1;
        for (Zbor flight : flights) {
            Set<Integer> usedColors = new HashSet<>();
            for (Zbor neighbor : conflictGraph.getNeighbors(flight)) {
                if (colorAssignment.containsKey(neighbor)) {
                    usedColors.add(colorAssignment.get(neighbor));
                }
            }
            int selectedColor = -1;
            for (int color = 0; color < numRunways; color++) {
                if (!usedColors.contains(color)) {
                    selectedColor = color;
                    break;
                }
            }

            if (selectedColor == -1) {

                System.out.println("Nu se poate realiza distribuirea pe piste!");
                return null;
            }
            colorAssignment.put(flight, selectedColor);
        }

        balanceColors(colorAssignment, numRunways);

        for (Map.Entry<Zbor, Integer> entry : colorAssignment.entrySet()) {
            Zbor zbor = entry.getKey();
            int color = entry.getValue();
            Runway runway = runways.get(color % runways.size());
            schedule.put(zbor, runway);
            zbor.setRunway(runway);
        }
        return schedule;
    }

    private void balanceColors(Map<Zbor, Integer> colorAssignment, int numColors) {
        Map<Integer, Integer> colorCounts = new HashMap<>();
        for (int color : colorAssignment.values()) {
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
        }

        int totalFlights = colorAssignment.size();
        int baseCount = totalFlights / numColors;
        int extra = totalFlights % numColors;

        for (int i = 0; i < numColors; i++) {
            int target = i < extra ? baseCount + 1 : baseCount;
            int current = colorCounts.getOrDefault(i, 0);

            while (current > target) {
                for (Zbor flight : colorAssignment.keySet()) {
                    if (colorAssignment.get(flight) == i) {
                        for (int j = 0; j < numColors; j++) {
                            if (j != i && colorCounts.getOrDefault(j, 0) < target) {

                                boolean valid = true;
                                for (Zbor neighbor : conflictGraph.getNeighbors(flight)) {
                                    if (colorAssignment.get(neighbor) == j) {
                                        valid = false;
                                        break;
                                    }
                                }
                                if (valid) {
                                    colorAssignment.put(flight, j);
                                    colorCounts.put(i, colorCounts.get(i) - 1);
                                    colorCounts.put(j, colorCounts.getOrDefault(j, 0) + 1);
                                    current--;
                                    break;
                                }
                            }
                        }
                        if (current <= target) break;
                    }
                }
            }
        }
    }
}
