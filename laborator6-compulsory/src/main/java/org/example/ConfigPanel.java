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

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout());

        // Create label and spinner
        label = new JLabel("Number of dots:");
        spinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        newGameButton = new JButton("New Game");

        // ActionListener to restart the game
        newGameButton.addActionListener(e -> frame.restartGame((int) spinner.getValue()));

        add(label);
        add(spinner);
        add(newGameButton);
    }
}
