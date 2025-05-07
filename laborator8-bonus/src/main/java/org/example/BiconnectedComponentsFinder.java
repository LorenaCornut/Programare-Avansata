package org.example;

import java.sql.*;
import java.util.*;

public class BiconnectedComponentsFinder {
    private Map<Integer, List<Integer>> graph;
    private List<Set<Integer>> biconnectedComponents;
    private int[] discovery;
    private int[] low;
    private int time;
    private Stack<Edge> edgeStack;

    public List<Set<Integer>> findBiconnectedComponents() throws SQLException {
        loadGraphFromDatabase();
        findComponents();
        return biconnectedComponents;
    }

    private void loadGraphFromDatabase() throws SQLException {
        graph = new HashMap<>();

        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT city1_id, city2_id FROM sister_cities")) {
            while (rs.next()) {
                int city1 = rs.getInt(1);
                int city2 = rs.getInt(2);

                graph.computeIfAbsent(city1, k -> new ArrayList<>()).add(city2);
                graph.computeIfAbsent(city2, k -> new ArrayList<>()).add(city1);
            }
        }
    }

    private void findComponents() {
        biconnectedComponents = new ArrayList<>();
        if (graph.isEmpty()) return;

        int maxNodeId = graph.keySet().stream().max(Integer::compare).orElse(0);
        discovery = new int[maxNodeId + 1];
        low = new int[maxNodeId + 1];
        time = 1;
        edgeStack = new Stack<>();

        for (int node : graph.keySet()) {
            if (discovery[node] == 0) {
                iterativeDfs(node);
            }
        }
    }

    private void iterativeDfs(int start) {
        Stack<DfsFrame> dfsStack = new Stack<>();
        dfsStack.push(new DfsFrame(start, -1, false));

        while (!dfsStack.isEmpty()) {
            DfsFrame frame = dfsStack.peek();
            int u = frame.node;

            if (!frame.processed) {
                // First time visiting this node
                discovery[u] = low[u] = time++;
                frame.processed = true;

                // Process neighbors
                for (int v : graph.get(u)) {
                    if (v == frame.parent) continue;

                    if (discovery[v] == 0) {
                        // Tree edge
                        dfsStack.push(new DfsFrame(v, u, false));
                        edgeStack.push(new Edge(u, v));
                    } else if (discovery[v] < discovery[u]) {
                        // Back edge
                        edgeStack.push(new Edge(u, v));
                        low[u] = Math.min(low[u], discovery[v]);
                    }
                }
            } else {
                // After processing children
                dfsStack.pop();

                // Update low value from children
                if (frame.parent != -1) {
                    for (int v : graph.get(u)) {
                        if (v != frame.parent && discovery[v] > discovery[u]) {
                            low[u] = Math.min(low[u], low[v]);

                            // Check for articulation point
                            if (low[v] >= discovery[u]) {
                                Set<Integer> component = new HashSet<>();
                                Edge edge;
                                do {
                                    if (edgeStack.isEmpty()) break;
                                    edge = edgeStack.pop();
                                    component.add(edge.u);
                                    component.add(edge.v);
                                } while (edge.u != u || edge.v != v);
                                biconnectedComponents.add(component);
                            }
                        }
                    }
                }
            }
        }
    }

    private static class Edge {
        int u, v;
        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    private static class DfsFrame {
        int node;
        int parent;
        boolean processed;

        DfsFrame(int node, int parent, boolean processed) {
            this.node = node;
            this.parent = parent;
            this.processed = processed;
        }
    }
}
