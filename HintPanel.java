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

/**
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
public class HintPanel extends JPanel {
    /**
     * The word to be guessed
     */
    private String targetWord;
    /**
     * The max number of guesses
     */
    private int MAX_GUESSES;
    /**
     * The number of hints that have been revealed
     */
    private int hintsRevealed;
    /**
     * The hints to be given based on targetWord
     */
    private String[] hints;
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

        // Add a header
        this.add(new JLabel("Hints", SwingConstants.CENTER),constraints);

        // Create a JLabel for each hint and give it default text
        for (int i = 0; i < hints.length; i++) {
            constraints.gridy++;
            hintLabels[i] = new HintLabel(i+1, hints[i]);
            this.add(hintLabels[i],constraints);
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
        double percentGuessed = (double) guessNumber / MAX_GUESSES;
        /**
         * The percent needed to reveal the next hint.
         */
        double revealThreshold = (double) hintsRevealed * 0.2 + 0.1;

        if(percentGuessed > revealThreshold){
            // Reveal the hint
            hintLabels[hintsRevealed++].reveal();
            // Check if more than one hint needs to be revealed
            checkReveal(guessNumber);
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
