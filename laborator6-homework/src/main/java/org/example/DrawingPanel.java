package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Collections;

public class DrawingPanel extends JPanel {
    final MainFrame frame;
    List<Point> dots = new ArrayList<>();
    List<Line> lines = new ArrayList<>();
    private transient BufferedImage image;
    private transient Point firstSelectedDot = null;
    private transient Graphics2D offscreen;
    private int canvasWidth = 400;
    private int canvasHeight = 400;
    Random random = new Random();

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        createOffscreenImage();
        init();

        createNewGame(10);
    }

    public List<Point> getDots() { return dots; }
    public List<Line> getLines() { return lines; }

    private void init() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleDotSelection(e.getX(), e.getY());
            }
        });
    }

    public void handleDotSelection(int x, int y) {
        if (frame.isGameOver()) return;
        Point selected = findDotAt(x, y);
        if (selected != null) {
            if (firstSelectedDot == null) {
                firstSelectedDot = selected;
                repaint();
            } else if (!firstSelectedDot.equals(selected)) {
                Color color = frame.getCurrentPlayerColor();
                Line line = new Line(firstSelectedDot, selected, color);
                lines.add(line);
                frame.addLine(line);
                ///addLine(line);
                frame.switchPlayer();
                firstSelectedDot = null;
                repaint();

                if (isGraphConnected()) {
                    frame.gameOver();
                }
            }
        }
    }

    public void addLine(Line line) {
        lines.add(line);
        frame.addLine(line);
        repaint();
    }

    public Point findDotAt(int x, int y) {
        for (Point dot : dots) {
            if (Math.hypot(dot.x - x, dot.y - y) <= 10) {
                return dot;
            }
        }
        return null;
    }

    public void createOffscreenImage() {
        image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        offscreen = image.createGraphics();
        offscreen.setColor(Color.WHITE); // fill the image with white
        offscreen.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    public void createNewGame(int numDots) {
        dots.clear();
        lines.clear();
        firstSelectedDot = null;
        for (int i = 0; i < numDots; i++) {
            int x = random.nextInt(canvasWidth - 40) + 20;
            int y = random.nextInt(canvasHeight - 40) + 20;
            dots.add(new Point(x, y));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, canvasWidth, canvasHeight);
        for (Line line : lines) {
            offscreen.setColor(line.getColor());
            offscreen.setStroke(new BasicStroke(2));
            offscreen.drawLine(line.getStart().x, line.getStart().y,
                    line.getEnd().x, line.getEnd().y);
        }
        //paintLines();
        paintDots();
        if (firstSelectedDot != null) {
            offscreen.setColor(Color.GREEN);
            offscreen.drawOval(firstSelectedDot.x - 8, firstSelectedDot.y - 8, 16, 16);
        }
        g.drawImage(image, 0, 0, this);
    }

    public void paintLines() {
        for (Line line : lines) {
            drawLine(line.getStart(), line.getEnd());  // Use drawLine to paint each line
        }
    }

    public void paintDots() {
        for (Point dot : dots) {
            drawDot(dot.x, dot.y);
        }
    }

    public void drawDot(int x, int y) {
        offscreen.setColor(Color.BLACK);
        offscreen.fillOval(x - 5, y - 5, 10, 10);
    }

    public void drawLine(Point first, Point second) {
        offscreen.setColor(Color.BLACK);
        offscreen.setStroke(new BasicStroke(2));
        offscreen.drawLine(first.x, first.y, second.x, second.y);
    }
    /*public void setFrame(MainFrame frame) {
        this.frame = frame;
        init();
    }*/

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public int calculate_kruskal() {
        System.out.println("Calculating MST for " + dots.size() + " dots");

        List<Line> allPossibleLines = new ArrayList<>();
        for (int i = 0; i < dots.size(); i++) {
            for (int j = i + 1; j < dots.size(); j++) {
                // Use the actual dots from the list, not new Point objects
                allPossibleLines.add(new Line(dots.get(i), dots.get(j), Color.BLACK));
            }
        }

        System.out.println("Total possible lines: " + allPossibleLines.size());

        Collections.sort(allPossibleLines, Comparator.comparingDouble(Line::getLength));

        DisjointSet ds = new DisjointSet(dots.size());
        int mstWeight = 0;
        int edgesAdded = 0;

        for (Line line : allPossibleLines) {
            int u = dots.indexOf(line.getStart());
            int v = dots.indexOf(line.getEnd());

            // Debug output for each line
            System.out.printf("Line %s to %s (length %.2f) - indices %d,%d%n",
                    line.getStart(), line.getEnd(), line.getLength(), u, v);

            if (u == -1 || v == -1) {
                System.out.println("ERROR: Couldn't find point in dots list");
                continue;
            }

            if (ds.find(u) != ds.find(v)) {
                ds.union(u, v);
                mstWeight += line.getLength();
                edgesAdded++;
                if (edgesAdded == dots.size() - 1) break;
            }
        }

        System.out.println("Calculated MST weight: " + mstWeight);
        return mstWeight;
    }

    public boolean isGraphConnected() {
        if (dots.isEmpty()) return false;

        DisjointSet ds = new DisjointSet(dots.size());

        for (Line line : lines) {
            int u = dots.indexOf(line.getStart());
            int v = dots.indexOf(line.getEnd());
            if (u != -1 && v != -1) {
                ds.union(u, v);
            }
        }

        int root = ds.find(0);
        for (int i = 1; i < dots.size(); i++) {
            if (ds.find(i) != root) {
                return false;
            }
        }
        return true;
    }

    private static class DisjointSet {
        private final int[] parent;
        private final int[] rank;

        public DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot) return;

            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else if (rank[xRoot] > rank[yRoot]) {
                parent[yRoot] = xRoot;
            } else {
                parent[yRoot] = xRoot;
                rank[xRoot]++;
            }
        }
    }
}














