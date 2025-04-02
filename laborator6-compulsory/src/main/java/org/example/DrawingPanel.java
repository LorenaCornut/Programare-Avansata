package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingPanel extends JPanel {
    final MainFrame frame;
    List<Point> dots = new ArrayList<>();
    Random random = new Random();

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(500, 500));
        createNewGame(10); // Default number of dots
    }

    public void createNewGame(int numDots) {
        dots.clear();
        for (int i = 0; i < numDots; i++) {
            int x = random.nextInt(480) + 10;
            int y = random.nextInt(480) + 10;
            dots.add(new Point(x, y));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (Point p : dots) {
            g.fillOval(p.x - 5, p.y - 5, 10, 10); // Draw dots
        }
    }
}
