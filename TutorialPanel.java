import javax.swing.*;
import java.awt.*;

public class TutorialPanel extends JPanel {
    public TutorialPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JTextArea tutorialText = new JTextArea();
        tutorialText.setText("Welcome to the game! Here's how to play:\n1. Guess the word based on the revealed image.\n2. Use the guess panel to enter your guess.\n3. Click 'Submit' to check your guess.\n4. Continue guessing until you solve the word.");
        tutorialText.setEditable(false);
        tutorialText.setLineWrap(true);
        tutorialText.setWrapStyleWord(true);
        
        add(tutorialText, BorderLayout.CENTER);
    }
}