package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner spinner;
    JButton newGameButton;
    JLabel scoreLabel;
    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout());

        label = new JLabel("Number of dots:");
        spinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        newGameButton = new JButton("New Game");
        scoreLabel = new JLabel("Scores: P1: 0 | P2: 0 | Optimal: 0");
        newGameButton.addActionListener(e -> frame.restartGame((int) spinner.getValue()));

        add(label);
        add(spinner);
        add(newGameButton);
        add(scoreLabel);

        scoreLabel.setPreferredSize(new Dimension(300, 20));
    }

    public void updateScoreDisplay(int player1Score, int player2Score, int optimalScore) {
        String text = String.format("Scores: P1: %.2f | P2: %.2f | Optimal: %.2f",
                (double)player1Score, (double)player2Score, (double)optimalScore);
        scoreLabel.setText(text);
        this.revalidate();
        this.repaint();
    }
}
