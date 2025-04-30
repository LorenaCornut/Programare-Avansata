package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;

import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private PlayerConfigPanel playerConfigPanel;
    private ControlPanel controlPanel;
    public DrawingPanel canvas;
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
        playerConfigPanel = new PlayerConfigPanel();
        controlPanel = new ControlPanel(this);
        canvas = new DrawingPanel(this);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(configPanel, BorderLayout.NORTH);
        topPanel.add(playerConfigPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
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

        if (playerConfigPanel.isBothAI()) {
            makeAIMove();
        }
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

        if (canvas.isGraphConnected()) {
            gameOver();
        }

        this.revalidate();
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

    public void makeAIMove() {
        // Verifică dacă jocul s-a terminat sau graful este deja conex
        if (gameOver || canvas.isGraphConnected()) {
            gameOver();
            return;
        }

        // Verifică dacă mai sunt mutări disponibile
        if (canvas.getLines().size() >= canvas.getDots().size() * (canvas.getDots().size() - 1) / 2) {
            gameOver();
            return;
        }

        boolean isCurrentPlayerAI = isPlayer1Turn ?
                playerConfigPanel.isPlayer1AI() : playerConfigPanel.isPlayer2AI();
        if (!isCurrentPlayerAI) return;

        // Execută pe un thread separat pentru a nu bloca interfața
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Adaugă un mic delay pentru vizibilitate
                try { Thread.sleep(500); } catch (InterruptedException e) {}

                int difficulty = isPlayer1Turn ?
                        playerConfigPanel.getPlayer1Difficulty() : playerConfigPanel.getPlayer2Difficulty();

                // Obține toate muchiile posibile care nu au fost deja desenate
                List<Line> possibleMoves = new ArrayList<>();
                List<Point> dots = canvas.getDots();

                for (int i = 0; i < dots.size(); i++) {
                    for (int j = i + 1; j < dots.size(); j++) {
                        Line line = new Line(dots.get(i), dots.get(j), Color.BLACK);
                        if (!canvas.getLines().contains(line)) {
                            possibleMoves.add(line);
                        }
                    }
                }

                // Sortează muchiile după lungime (pentru algoritmul lui Kruskal)
                possibleMoves.sort(Comparator.comparingDouble(Line::getLength));

                // Alegerea mutării în funcție de dificultate
                Line chosenMove = null;
                Random random = new Random();

                switch (difficulty) {
                    case 0: // Easy - alegere aleatoare
                        if (!possibleMoves.isEmpty()) {
                            chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
                        }
                        break;

                    case 1: // Medium - 70% șansă de alegere optimă
                        if (!possibleMoves.isEmpty()) {
                            if (random.nextDouble() < 0.7) {
                                chosenMove = possibleMoves.get(0); // Cea mai scurtă muchie
                            } else {
                                // Alegere aleatoare din primele 30% muchii
                                int index = random.nextInt(Math.max(1, possibleMoves.size() / 3));
                                chosenMove = possibleMoves.get(index);
                            }
                        }
                        break;

                    case 2: // Hard - întotdeauna alegere optimă (Kruskal)
                        if (!possibleMoves.isEmpty()) {
                            // Folosește DisjointSet pentru a evita crearea de cicluri
                            DisjointSet ds = new DisjointSet(dots.size());

                            // Actualizează setul cu muchiile existente
                            for (Line line : canvas.getLines()) {
                                int u = dots.indexOf(line.getStart());
                                int v = dots.indexOf(line.getEnd());
                                if (u != -1 && v != -1) {
                                    ds.union(u, v);
                                }
                            }

                            // Caută prima muchie disponibilă care nu creează ciclu
                            for (Line line : possibleMoves) {
                                int u = dots.indexOf(line.getStart());
                                int v = dots.indexOf(line.getEnd());

                                if (ds.find(u) != ds.find(v)) {
                                    chosenMove = line;
                                    break;
                                }
                            }

                            // Dacă toate muchiile creează cicluri, alege una aleator
                            if (chosenMove == null && !possibleMoves.isEmpty()) {
                                chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
                            }
                        }
                        break;
                }

                // Execută mutarea pe Event Dispatch Thread
                if (chosenMove != null) {
                    Line finalMove = chosenMove;
                    SwingUtilities.invokeLater(() -> {
                        Color color = getCurrentPlayerColor();
                        Line newLine = new Line(finalMove.getStart(), finalMove.getEnd(), color);
                        canvas.getLines().add(newLine);
                        addLine(newLine); // Verifică automat conectivitatea
                        switchPlayer();
                        canvas.repaint();

                        // Continuă jocul dacă nu e terminat
                        if (!gameOver && !canvas.isGraphConnected()) {
                            // Dacă ambii jucători sunt AI, face următoarea mutare automat
                            if (playerConfigPanel.isBothAI()) {
                                makeAIMove();
                            }
                        }
                    });
                }
                return null;
            }
        }.execute();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}