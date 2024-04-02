import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Jackson Alexman
 * @version Created: 3/27/2024 Updated: 4/01/2024
 */
public class AdaptingStringList {
    /**
     * Parts of words typed by the user : The words from the wordbank that contain that part
     */
    private Dictionary<String,String[]> parts;
    /**
     * The current part typed by the user 
     */
    private String part;
    /**
     * The entire unedited guessable word list
     */
    private String[] fullWordList;

    public AdaptingStringList(){
        // Get the wordlist
        String filePath = "WordList.txt";

        try {
            // Open the file for reading
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            // ArrayList to store the words
            ArrayList<String> wordsList = new ArrayList<>();

            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Split the line into words using whitespace as delimiter
                String[] words = line.split("\\s+");
                
                // Add each word to the ArrayList
                wordsList.addAll(Arrays.asList(words));
            }

            // Close the file reader
            reader.close();

            // Convert the ArrayList to an array
            fullWordList = wordsList.toArray(new String[0]);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // Initialize the instance variables
        this.parts = new Hashtable<String,String[]>();
        this.part = "";

        // Add "" to the start of array because the first element of the array needs to be what is currently typed
        ArrayList<String> tempArray = new ArrayList<>(Arrays.asList(fullWordList));
        tempArray.add(0,part);
        this.parts.put(this.part, tempArray.toArray(new String[0]));
    }

    /**
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
            String[] subPartWordList = null;
            while(i>=0){
                // Check to see if any subPart of this part has been pared
                subPartWordList = parts.get(newPart.substring(0,i));
                if(subPartWordList == null){
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
            part = newPart;
        }
        // Previous Entry
        else{
            part = newPart;
        }
    }

    /**
     * @param wordList The part of the word list to pare
     * @param partToPare The part to pare the word list with
     * @return A pared word list
     */
    private String[] pareWordList(String[] wordList, String partToPare) {
        ArrayList<String> finalWordList = new ArrayList<>();
        Dictionary<Integer,ArrayList<String>> partPositionInWords = new Hashtable<Integer,ArrayList<String>>();
        // The first word in the list should always be the part in order to display it in the editor
        finalWordList.add(partToPare);
        // Add all the words that contain the part
        for(String word : wordList){
            if(word.toLowerCase().contains(partToPare)){
                int index = word.toLowerCase().indexOf(partToPare);
                if(partPositionInWords.get(index) == null){
                    partPositionInWords.put(index, new ArrayList<String>());
                }
                partPositionInWords.get(index).add(word);
            }
        }
        // Add the words to the array sorted by how close the part is to the start of the word
        int i = 0;
        while(partPositionInWords.size()>0){
            // Get all the words with the part at index i in the word
            ArrayList<String> subFinalWordList = partPositionInWords.get(i);
            partPositionInWords.remove(i++);
            // Sort them alphabetically and add them to the word list
            try{
                // Sometimes this errors because there are no words with the part at that index but there are words with the part at a larger index
                Collections.sort(subFinalWordList);
            }
            catch(NullPointerException e){
                continue;
            }
            
            finalWordList.addAll(subFinalWordList);
        }

        return finalWordList.toArray(new String[0]);
    }

    /**
     * @return The current word list based on the current part
     */
    public String[] getWordList(){
        return this.parts.get(this.part);
    }

    /**
     * @return The current part
     */
    public String getPart(){
        return this.part;
    }

    /**
     * @return Check if the current part is in the wordlist 
     */
    public boolean isPartInWordList(){
        // Skip the first index because part is stored at the first index
        for (int i = 1; i < this.getWordList().length; i++) {
            if(this.part.equals(this.getWordList()[i].toLowerCase())){
                return true;
            }
        }

        return false;
    }
}
