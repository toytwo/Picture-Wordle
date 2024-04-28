/**
 * The AdaptingStringList class manages word lists and user inputs for a guessing game or similar application.
 * It provides functionality to dynamically update the word list based on user input and retrieve relevant word lists
 * for different parts entered by the user.
 * 
 * Key Features:
 * - Uses a Dictionary to store parts of words and corresponding arrays of full words containing that part.
 * - Allows dynamic updating of the word list based on user input.
 * - Provides methods to retrieve relevant word lists for different parts entered by the user.
 * - Implements efficient filtering of word lists based on given parts.
 * 
 * Usage:
 * - Create an instance of AdaptingStringList to manage word lists and user inputs.
 * - Use updateLetters method to update the word list based on user input.
 * - Use getWordList method to retrieve the current word list based on the current part.
 * - Use getPart method to retrieve the current part.
 * - Use isPartInWordList method to check if the current part is in the word list.
 * 
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
import java.io.BufferedReader; // Used for reading data line by line from a text file
import java.io.FileReader; // Used to open a text file for reading.
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

<<<<<<< Updated upstream
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
=======
>>>>>>> Stashed changes
public class AdaptingStringList {
    /**
     * Parts of words typed by the user : The words from the wordbank that contain that part
     */
<<<<<<< Updated upstream
    private Map<String,String[]> parts;
=======
    private Dictionary<String, String[]> parts; // key: part of the word, value: full words that contain that part
>>>>>>> Stashed changes
    /**
     * The current part typed by the user 
     */
    private String part;
    /**
     * The entire unedited guessable word list
     */
<<<<<<< Updated upstream
    private String[] fullWordList;

    public AdaptingStringList(){
=======
    private String[] fullWordList; // An array containing the entire unedited guessable word list

    /**
     * Constructor to initialize an AdaptingStringList instance.
     * Reads the word list from a text file and initializes the parts dictionary and part.
     */
    public AdaptingStringList() {
>>>>>>> Stashed changes
        try {
            // Open the file for reading
            BufferedReader reader = new BufferedReader(new FileReader("WordList.txt"));
            
            // ArrayList to store the words
            ArrayList<String> wordsList = new ArrayList<>();

            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Add each word to the ArrayList
                wordsList.add(line);
            }

            // Close the file reader
            reader.close();

            // Convert the ArrayList to an array
            fullWordList = wordsList.toArray(new String[0]);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Initialize the instance variables
        this.parts = new HashMap<String,String[]>();
        this.part = "";

        // Add "" to the start of array because the first element of the array needs to be what is currently typed
        ArrayList<String> tempArray = new ArrayList<>(Arrays.asList(fullWordList));
        tempArray.add(0,part);
        this.parts.put(this.part, tempArray.toArray(new String[0]));
    }

    /**
<<<<<<< Updated upstream
     * Checks the parts dictionary to see if the newPart has already been pared. If not, it pares it and all of it's subparts.
     * @param newPart The new part to be checked
     */
    public void updateLetters(String newPart){
        newPart = newPart.toLowerCase();
        // No Update
        if(part.equals(newPart)){
            return;
        }
        // New Entry
        if(parts.get(newPart) == null){
            int i = newPart.length()-1;
=======
     * Checks the parts dictionary to see if the newPart has already been pared. If
     * not, it pares it and all of its subparts.
     * 
     * @param newPart The new part to be checked
     */
    public void updateLetters(String newPart) {
        newPart = newPart.toLowerCase(); // newPart letter used typed by the user
        // No Update
        if (part.equals(newPart)) { // if newPart already exists in part
            return;
        }
        // New Entry
        if (parts.get(newPart) == null) { // If new part is not in parts
            int i = newPart.length() - 1;
>>>>>>> Stashed changes
            String[] subPartWordList = null;
            while(i>=0){
                // Check to see if any subPart of this part has been pared
<<<<<<< Updated upstream
                subPartWordList = parts.get(newPart.substring(0,i));
                if(subPartWordList == null){
=======
                // If newPart is not found in parts, it iterates through all possible subparts
                // i starts from the length of newPart minus 1 (to get the entire string) and
                // decrements down to 0 (to get the empty string).
                // of newPart
                subPartWordList = parts.get(newPart.substring(0, i));
                if (subPartWordList == null) {
>>>>>>> Stashed changes
                    i--;
                    continue;
                }
                else{
                    i++; // Pare the next index because paring found at current
                    break;
                }
            } 

            // Pare every part that makes up this part that hasn't already been pared
            String subPart = "";
            while(i<=newPart.length()){
                subPart = newPart.substring(0, i++);
                subPartWordList = pareWordList(subPartWordList, subPart);
                parts.put(subPart, subPartWordList);
            }
            part = newPart; // updates the part to the new newPart using part = newPart
        }
        // Previous Entry
        else{
            part = newPart;
        }
    }

    /**
<<<<<<< Updated upstream
     * @param wordList The part of the word list to pare
     * @param partToPare The part to pare the word list with
     * @return A pared word list
=======
     * Parses the word list to filter words containing a given part.
     * 
     * @param wordList   The part of the word list to filter
     * @param partToPare The part to filter the word list with
     * @return A filtered word list
>>>>>>> Stashed changes
     */
    private String[] pareWordList(String[] wordList, String partToPare) {
        ArrayList<String> finalWordList = new ArrayList<>();
        Map<Integer,ArrayList<String>> partPositionInWords = new HashMap<Integer,ArrayList<String>>();
        // The first word in the list should always be the part in order to display it in the editor
        finalWordList.add(partToPare.substring(0, 1).toUpperCase()+partToPare.substring(1));
        // Add all the words that contain the part
        for(String word : wordList){
            if(word.toLowerCase().contains(partToPare)){
                int index = word.toLowerCase().indexOf(partToPare);
                if(partPositionInWords.get(index) == null){
                    partPositionInWords.put(index, new ArrayList<String>());
                }
                partPositionInWords.get(index).add(word.substring(0, 1).toUpperCase()+word.substring(1));
            }
        }
        // Add the words to the array sorted by how close the part is to the start of the word
        int i = 0;
        while(partPositionInWords.size()>0){
            // Get all the words with the part at index i in the word
            ArrayList<String> subFinalWordList = partPositionInWords.get(i);
            partPositionInWords.remove(i++); // we already processed the index
            // Sort them alphabetically and add them to the word list
            try{
                // Sometimes this errors because there are no words with the part at that index but there are words with the part at a larger index
                Collections.sort(subFinalWordList);
<<<<<<< Updated upstream
            }
            catch(NullPointerException e){
                continue;
=======
            } catch (NullPointerException e) {
                continue; // go to the next index without processing empty string
>>>>>>> Stashed changes
            }
            
            finalWordList.addAll(subFinalWordList);
        }

        return finalWordList.toArray(new String[0]);
    }

    /**
     * Retrieves the current word list based on the current part.
     * 
     * @return The current word list
     */
<<<<<<< Updated upstream
    public String[] getWordList(){
        return this.parts.get(this.part);
=======
    public String[] getWordList() {
        return this.parts.get(this.part); // returns the current word list based on the current part
>>>>>>> Stashed changes
    }

    /**
     * Retrieves the current part.
     * 
     * @return The current part
     */
<<<<<<< Updated upstream
    public String getPart(){
        return this.part;
    }

    /**
     * @return Check if the current part is in the wordlist 
=======
    public String getPart() {
        return this.part; // returns the current part
    }

    /**
     * Checks if the current part is in the word list.
     * 
     * @return true if the current part is in the word list, false otherwise
>>>>>>> Stashed changes
     */
    public boolean isPartInWordList(){
        // Skip the first index because part is stored at the first index
        for (int i = 1; i < this.getWordList().length; i++) {
<<<<<<< Updated upstream
            if(this.part.equals(this.getWordList()[i].toLowerCase())){
=======
            // part is found in the word list
            if (this.part.equals(this.getWordList()[i].toLowerCase())) {
>>>>>>> Stashed changes
                return true;
            }
        }

        return false;
    }
}
