import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class HintPanel extends InteractivePanel {
    /**
     * The hints to be given based on targetWord
     */
    private String[] hints;
    /**
     * The JLabels that hold the hints
     */
    private HintLabel[] hintLabels;
    /**
     * Buttons that allow the player to manually reveal hints
     */
    private JButton[] revealButtons;
    /**
     * The word types i.e. noun, verb, or adjective, for each possible targetWord
     */
    private static Map<String,String> targetWordTypes;
    private static final String[] vowels = new String[]{"a","e","i","o","u"};

    /**
     * @param targetWord The word to be guessed
     * @param MAX_GUESSES The max number of guesses
     */
    public HintPanel(String targetWord, int MAX_GUESSES, int REVEAL_HINT_COST, boolean pointsEnabled){
        super(new GridBagLayout(), targetWord.toLowerCase(), 0, false, MAX_GUESSES, REVEAL_HINT_COST, pointsEnabled);

        // Initialize Instance Variables
        this.hintLabels = new HintLabel[4];
        this.revealButtons = new JButton[4];

        // If targetWordTypes is uninitialized
        if(targetWordTypes == null){
            targetWordTypes = initializeTargetWordTypes();
        }

        // Create the hints
        this.hints = generateHints();

        setupContentArea();
    }

    @Override
    protected void resetPanel(String newTargetWord) {
        super.resetPanel(newTargetWord.toLowerCase());
    }

    @Override
    public void resetInstanceVariables() {
        this.hints = generateHints();
    }

    @Override
    public void resetContentArea() {
        for (int i = 0; i < hints.length; i++) {
            
            double threshold = ((double) i) * 0.2 + 0.1;
            int revealAt = (int) (threshold*MAX_ACTIONS) + 1;
            hintLabels[i].resetLabel(hints[i], revealAt);
            
            if(pointsEnabled){
                revealButtons[i].setEnabled(i==0);
            }
        }
    }

    @Override
    public void setupContentArea() {
        // Adjust the layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        constraints.weightx = 1; // Fill horizontally
        constraints.weighty = 1; // Fill vertically
        constraints.gridx = 0; // Horizontal position
        constraints.gridy = 0; // Vertical position

        // Add an empty panel for spacing
        JPanel emptyPanel = new JPanel();
        emptyPanel.setMinimumSize(new Dimension(100,20));
        emptyPanel.setPreferredSize(new Dimension(100,20));
        this.add(emptyPanel, constraints);

        // Add a header
        JLabel header = new JLabel("                           Hints                           ", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, HintLabel.TEXT_SIZE+10));
        constraints.gridwidth = 2;
        constraints.gridy++;
        this.add(header,constraints);
        
        constraints.gridwidth = 1;
        // Create a JLabel and JButton for each hint
        for (int i = 0; i < hints.length; i++) {
            constraints.gridy++;

            // Determine when it will reveal
            double threshold = ((double) i) * 0.2 + 0.1;
            int revealAt = (int) (threshold*MAX_ACTIONS) + 1;

            constraints.gridx = 0;
            hintLabels[i] = new HintLabel(hints[i], revealAt);
            this.add(hintLabels[i],constraints);

            if(pointsEnabled){
                constraints.gridx = 1;
                constraints.weightx = 0;
                revealButtons[i] = new JButton("Manual Reveal");
                revealButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
                int index = i; // Variables must be relatively final within anonymous declaration
                revealButtons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hintLabels[interactionCount++].reveal();
                        
                        Game.getCurrentGame().getScorePanel().updateImageScore(-ACTION_COST);
                        revealButtons[index].setEnabled(false);
                        if(index+1 < hints.length){
                            revealButtons[index+1].setEnabled(true);
                        }
                        
                    }    
                });
                revealButtons[i].setEnabled(i==0);
                this.add(revealButtons[i],constraints);
                constraints.weightx = 1;
            }
        }
    }

    /**
     * Called every time a guess is made. May or may not reveal a hint based on the guessNumber
     * @param guessNumber The number of guesses that have been made
     */
    public void checkReveal(int guessNumber){
        /**
         * What percentage of their guesses have they used
         */
        double percentGuessed = (double) guessNumber / MAX_ACTIONS;
        /**
         * The percent needed to reveal the next hint.
         */
        double revealThreshold = (double) interactionCount * 0.2 + 0.1;

        // Reveal the hint
        if(percentGuessed > revealThreshold){
            if(pointsEnabled){
                revealButtons[interactionCount].setEnabled(false);
            }
            
            hintLabels[interactionCount++].reveal();
            if(interactionCount < hintLabels.length){
                if(pointsEnabled){
                    revealButtons[interactionCount].setEnabled(true);
                }
                
                // Check if more than one hint needs to be revealed
                checkReveal(guessNumber);
            }
        }
    }

    /**
     * Create the hints
     * @return An array of String hints in the order they should be revealed
     */
    private String[] generateHints(){
        String[] hintsArray = new String[4];

        String firstLetterType = null;
        for (String vowel : vowels) {
            if(this.targetWord.substring(0, 1).equals(vowel)){
                firstLetterType = "vowel";
            }
        }
        if(firstLetterType == null){
            firstLetterType = "consonant";
        }
        hintsArray[0] = "The first letter of this word is a "+firstLetterType+".";

        String article = null;
        String wordType = targetWordTypes.get(this.targetWord);
        if(wordType.equals("adjective")){
            article = "an";
        }
        else{
            article = "a";
        }
        hintsArray[1] = "This word is "+article+" "+wordType+".";

        hintsArray[2] = "This word is "+targetWord.length()+" letters long.";

        hintsArray[3] = "The first letter of this word is '"+targetWord.substring(0, 1).toUpperCase()+"'.";

        return hintsArray;
    }

    /**
     * Gets the all the possible target words and their types and creates a dictionary to access the types
     * @return The dictionary containing the target words as the keys and types as the values
     */
    private Map<String,String> initializeTargetWordTypes(){
        Map<String,String> dict = new HashMap<String,String>();
        ArrayList<String> targetWords = new ArrayList<>();
        ArrayList<String> targetWordTypes = new ArrayList<>();

        try {
            // Open the file for reading
            BufferedReader reader = new BufferedReader(new FileReader("TargetWords.txt"));

            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Add each word to the ArrayList
                targetWords.add(line);
            }

            // Close the file reader
            reader.close();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(0);
        }

        try {
            // Open the file for reading
            BufferedReader reader = new BufferedReader(new FileReader("TargetWordTypes.txt"));

            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Add each word to the ArrayList
                targetWordTypes.add(line);
            }

            // Close the file reader
            reader.close();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(0);
        }

        // Check if one of the lists is not updated
        if(targetWords.size() != targetWordTypes.size()){
            System.err.println("Old TargetWord lists. Update TargetWords.txt and TargetWordTypes.txt");
            System.exit(0);
        }

        // Create the dictionary
        for (int i = 0; i < targetWords.size(); i++) {
            dict.put(targetWords.get(i).toLowerCase(),targetWordTypes.get(i).toLowerCase());
        }

        // Make sure the targetword is in the dictionary
        if(dict.get(this.targetWord) == null){
            System.err.println(this.targetWord+" not found. Update TargetWords.txt and TargetWordTypes.txt");
            System.exit(0);
        }

        return dict;
    }
}
