package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton loadButton, saveButton, exitButton, exportButton;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new FlowLayout());

        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        exportButton = new JButton("Export PNG");
        exitButton = new JButton("Exit");

        loadButton.addActionListener(this::loadGame);
        saveButton.addActionListener(this::saveGame);
        exportButton.addActionListener(this::exportGame);
        exitButton.addActionListener(e -> System.exit(0));

        add(loadButton);
        add(saveButton);
        add(exportButton);
        add(exitButton);
    }

    private void saveGame(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game Files (*.game)", "game"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".game")) {
                filename += ".game";
            }
            frame.saveGame(filename);
        }
    }

    private void loadGame(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game Files (*.game)", "game"));
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            frame.loadGame(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void exportGame(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images (*.png)", "png"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".png")) {
                filename += ".png";
            }
            frame.exportToPNG(filename);
        }
    }
}
