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
            } else if (!firstSelectedDot.equals(selected)) {
                Color color = frame.getCurrentPlayerColor();
                Line line = new Line(firstSelectedDot, selected, color);
                if (!lines.contains(line)) {
                    lines.add(line);
                    frame.addLine(line);
                    frame.switchPlayer();
                    if (!frame.isGameOver()) {
                        frame.makeAIMove();
                    }
                }
                firstSelectedDot = null;
            }
        }
        repaint();
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
        offscreen.setColor(Color.WHITE);
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

    /*public List<Line> generatePossibleMoves() {
        List<Line> possibleMoves = new ArrayList<>();
        for (int i = 0; i < dots.size(); i++) {
            for (int j = i + 1; j < dots.size(); j++) {
                Point p1 = dots.get(i);
                Point p2 = dots.get(j);
                Line line = new Line(p1, p2, Color.BLACK);
                if (!lines.contains(line)) {
                    possibleMoves.add(line);
                }
            }
        }
        possibleMoves.sort(Comparator.comparingDouble(Line::getLength));
        return possibleMoves;
    }*/
}