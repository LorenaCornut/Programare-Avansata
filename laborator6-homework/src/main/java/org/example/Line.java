package org.example;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Line implements Serializable {
    private Point start;
    private Point end;
    private Color color;

    public Line(Point start, Point end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    public double getLength() {
        return Point2D.distance(start.x, start.y, end.x, end.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return start.equals(line.start) && end.equals(line.end) ||
                start.equals(line.end) && end.equals(line.start); // Consider lines bidirectional
    }

    @Override
    public int hashCode() {
        return start.hashCode() + end.hashCode(); // Order-independent hash
    }
}