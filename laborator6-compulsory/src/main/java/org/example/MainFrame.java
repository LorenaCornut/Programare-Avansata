package org.example;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    private DrawingPanel canvas;

    public MainFrame() {
        super("My Drawing Application");
        init();
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

    public void restartGame(int numDots) {
        canvas.createNewGame(numDots); // Restart the game with the new number of dots
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}