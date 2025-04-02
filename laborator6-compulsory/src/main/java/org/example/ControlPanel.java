package org.example;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton loadButton, saveButton, exitButton;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout());

        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        exitButton.addActionListener(e -> System.exit(0));

        add(loadButton);
        add(saveButton);
        add(exitButton);
    }
}
