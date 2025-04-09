package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    private DrawingPanel canvas;
    private int player1Score = 0;
    private int player2Score = 0;
    private int optimalScore = 0;
    private boolean isPlayer1Turn = true;
    private boolean gameOver = false;
    public MainFrame() {
        super("My Drawing Application");
        init();
    }

    public Color getCurrentPlayerColor() {
        return isPlayer1Turn ? Color.BLUE : Color.RED;
    }
    public void switchPlayer() {
        isPlayer1Turn = !isPlayer1Turn;
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this);
        canvas = new DrawingPanel(this);

        add(configPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void gameOver() {
        gameOver = true;
        String winner;
        if (player1Score < player2Score) { winner = "Player 1 (Blue) wins!"; }
        else if (player2Score < player1Score) { winner = "Player 2 (Red) wins!"; }
        else { winner = "Remiza!"; }

        JOptionPane.showMessageDialog(this,
                "Game Over!\n" +
                        "Player 1 (Blue) score: " + player1Score + "\n" +
                        "Player 2 (Red) score: " + player2Score + "\n" +
                        "Optimal score: " + optimalScore + "\n\n" +
                        winner);
    }

    public void restartGame(int numDots) {
        canvas.createNewGame(numDots);
        calculateOptimalScore();
        player1Score = 0;
        player2Score = 0;
        gameOver = false;
        updateScoreDisplay();
    }
    public void addLine(Line line) {
        ///canvas.addLine(line);
        if (gameOver) return;
        if (line.getColor() == Color.BLUE) {
            player1Score += line.getLength();
        } else {
            player2Score += line.getLength();
        }
        updateScoreDisplay();
        this.revalidate(); // Adaugă această linie
        this.repaint();
    }
    private void calculateOptimalScore() {
        optimalScore = (int) Math.round(canvas.calculate_kruskal());
        System.out.println("Optimal score calculated: " + optimalScore); // Debug
    }
    private void updateScoreDisplay() {
        if (configPanel != null) {
            configPanel.updateScoreDisplay(player1Score, player2Score, optimalScore);
        }
    }

    public void saveGame(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeInt(canvas.dots.size()); // "canvas" este instanta DrawingPanel
            for (Point dot : canvas.dots) {
                out.writeInt(dot.x);
                out.writeInt(dot.y);
            }
            out.writeInt(canvas.lines.size());
            for (Line line : canvas.lines) {
                out.writeObject(line); // Line trebuie să fie Serializable
            }
            out.writeInt(player1Score);
            out.writeInt(player2Score);
            out.writeInt(optimalScore);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            canvas.dots.clear();
            int dotCount = in.readInt();
            for (int i = 0; i < dotCount; i++) {
                int x = in.readInt();
                int y = in.readInt();
                canvas.dots.add(new Point(x, y));
            }

            canvas.lines.clear();
            int lineCount = in.readInt();
            for (int i = 0; i < lineCount; i++) {
                canvas.lines.add((Line) in.readObject());
            }
            player1Score = in.readInt();
            player2Score = in.readInt();
            optimalScore = in.readInt();

            canvas.createOffscreenImage();
            updateScoreDisplay();
            repaint();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void exportToPNG(String filename) {
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        canvas.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}