<<<<<<< Updated upstream
import java.awt.Dimension;
import java.awt.Font;
=======
/**
 * The HintPanel class represents a panel that provides hints to aid users in guessing the target word in a game.
 * It dynamically reveals hints based on the user's progress in making guesses.
 * 
 * Instance Variables:
 * - targetWord: The word to be guessed.
 * - MAX_GUESSES: The maximum number of guesses allowed.
 * - hintsRevealed: The number of hints that have been revealed.
 * - hints: An array of String representing the hints to be given based on the targetWord.
 * - hintLabels: An array of JLabels that hold the hints.
 * - targetWordTypes: A static map mapping target words to their types (e.g., noun, verb, adjective).
 * - vowels: An array of strings representing vowels.
 * 
 * Methods:
 * - HintPanel(String targetWord, int MAX_GUESSES): Constructor to initialize a new HintPanel instance.
 * - checkReveal(int guessNumber): Method called every time a guess is made to potentially reveal a hint based on the guess number.
 * - reveal(): Method to reveal the next hint.
 * - generateHints(): Method to generate hints based on the targetWord.
 * - initializeTargetWordTypes(): Method to initialize the targetWordTypes map from external files.
 * 
 * Usage:
 * - Create a new instance of HintPanel with the target word and maximum guesses parameters.
 * - The panel dynamically reveals hints as the user makes guesses.
 * - Use the checkReveal(int guessNumber) method to check if a hint should be revealed based on the number of guesses made.
 * 
 * Author: Jackson Alexman
 * Version: Updated: 4/17/2024
 */
>>>>>>> Stashed changes
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

<<<<<<< Updated upstream
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
=======
>>>>>>> Stashed changes
public class HintPanel extends JPanel {
    /** The word to be guessed. */
    private String targetWord;
    /** The max number of guesses. */
    private int MAX_GUESSES;
    /** The number of hints that have been revealed. */
    private int hintsRevealed;
    /** The hints to be given based on targetWord. */
    private String[] hints;
<<<<<<< Updated upstream
    /**
     * The JLabels that hold the hints
     */
    private HintLabel[] hintLabels;
    /**
     * The word types i.e. noun, verb, or adjective, for each possible targetWord
     */
    private static Map<String,String> targetWordTypes;
    private static final String[] vowels = new String[]{"a","e","i","o","u"};

    /**
     * @param targetWord The word to be guessed
     * @param MAX_GUESSES The max number of guesses
=======
    /** The JLabels that hold the hints. */
    private JLabel[] hintLabels;
    /** The word types i.e. noun, verb, or adjective, for each possible targetWord. */
    private static Map<String, String> targetWordTypes; // maps targetWord to word type
    /** An array of strings representing vowels. */
    private static final String[] vowels = new String[] { "a", "e", "i", "o", "u" };

    /**
     * Constructor to initialize a new HintPanel instance.
     * 
     * @param targetWord  The word to be guessed.
     * @param MAX_GUESSES The max number of guesses.
>>>>>>> Stashed changes
     */
    public HintPanel(String targetWord, int MAX_GUESSES){
        // Use a GridBagLayout
        super(new GridBagLayout());

        // Initialize Instance Variables
        this.targetWord = targetWord.toLowerCase();
        this.MAX_GUESSES = MAX_GUESSES;
        this.hintsRevealed = 0;
        this.hintLabels = new HintLabel[4];

        // If targetWordTypes is uninitialized
        if(targetWordTypes == null){
            targetWordTypes = initializeTargetWordTypes();
        }

        // Create the hints
        this.hints = generateHints();

        // Adjust the layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        constraints.weightx = 1; // Fill horizontally
        constraints.weighty = 1; // Fill vertically
        constraints.gridx = 1; // Horizontal position
        constraints.gridy = 0; // Vertical position

        // Add an empty panel for spacing
        JPanel emptyPanel = new JPanel();
        emptyPanel.setMinimumSize(new Dimension(100,20));
        emptyPanel.setPreferredSize(new Dimension(100,20));
        this.add(emptyPanel, constraints);

        // Add a header
        JLabel header = new JLabel("                              Hints                              ", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.PLAIN, HintLabel.TEXT_SIZE));
        constraints.gridy++;
        this.add(header,constraints);
        

        // Create a JLabel for each hint and give it default text
        for (int i = 0; i < hints.length; i++) {
            constraints.gridy++;
            hintLabels[i] = new HintLabel(i+1, hints[i]);
            this.add(hintLabels[i],constraints);
        }
    }

    /**
<<<<<<< Updated upstream
     * Called every time a guess is made. May or may not reveal a hint based on the guessNumber
     * @param guessNumber The number of guesses that have been made
     */
    public void checkReveal(int guessNumber){
        /**
         * What percentage of their guesses have they used
         */
=======
     * Method called every time a guess is made. May or may not reveal a hint based on the guessNumber.
     * 
     * @param guessNumber The number of guesses that have been made.
     */
    public void checkReveal(int guessNumber) {
>>>>>>> Stashed changes
        double percentGuessed = (double) guessNumber / MAX_GUESSES;
        double revealThreshold = (double) hintsRevealed * 0.2 + 0.1;

<<<<<<< Updated upstream
        if(percentGuessed > revealThreshold){
            // Reveal the hint
            hintLabels[hintsRevealed++].reveal();
            // Check if more than one hint needs to be revealed
            checkReveal(guessNumber);
=======
        if (percentGuessed > revealThreshold) {
            reveal(); // Reveal the hint
            checkReveal(guessNumber); // Check if more than one hint needs to be revealed
>>>>>>> Stashed changes
        }
    }

    /**
<<<<<<< Updated upstream
     * Create the hints
     * @return An array of String hints in the order they should be revealed
=======
     * Reveal the next hint.
     */
    private void reveal() {
        // Make the label visible and increment the number of hints
        hintLabels[hintsRevealed].setText(hints[hintsRevealed++]);
    }

    /**
     * Generate hints based on the targetWord.
     * 
     * @return An array of String hints in the order they should be revealed.
>>>>>>> Stashed changes
     */
    private String[] generateHints(){
        String[] hintsArray = new String[4];

        // Determine if the first letter is a vowel or consonant
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

        // Determine the article and word type
        String article = null;
        System.out.println(this.targetWord);
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
<<<<<<< Updated upstream
     * Gets the all the possible target words and their types and creates a dictionary to access the types
     * @return The dictionary containing the target words as the keys and types as the values
=======
     * Initialize the targetWordTypes map from external files.
     * 
     * @return The dictionary containing the target words as the keys and types as the values.
>>>>>>> Stashed changes
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
