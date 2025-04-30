package org.example;

import javax.swing.*;
import java.awt.*;

public class PlayerConfigPanel extends JPanel {
    private final JComboBox<String> player1Type, player2Type;
    private final JComboBox<String> player1Difficulty, player2Difficulty;
    public PlayerConfigPanel() {
        setLayout(new FlowLayout());

        add(new JLabel("Player 1:"));
        player1Type = new JComboBox<>(new String[]{"Human", "AI"});
        add(player1Type);
        player1Difficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        add(player1Difficulty);
        player1Difficulty.setEnabled(false);

        add(new JLabel("Player 2:"));
        player2Type = new JComboBox<>(new String[]{"Human", "AI"});
        add(player2Type);
        player2Difficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        add(player2Difficulty);
        player2Difficulty.setEnabled(false);

        player1Type.addActionListener(e ->
                player1Difficulty.setEnabled(player1Type.getSelectedItem().equals("AI")));
        player2Type.addActionListener(e ->
                player2Difficulty.setEnabled(player2Type.getSelectedItem().equals("AI")));
    }
    public boolean isPlayer1AI() {
        return player1Type.getSelectedItem().equals("AI");
    }

    public boolean isPlayer2AI() {
        return player2Type.getSelectedItem().equals("AI");
    }

    public int getPlayer1Difficulty() {
        return player1Difficulty.getSelectedIndex();
    }

    public int getPlayer2Difficulty() {
        return player2Difficulty.getSelectedIndex();
    }

    public boolean isBothAI() {
        return isPlayer1AI() && isPlayer2AI();
    }
}
