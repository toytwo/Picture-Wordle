import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

public class AdaptingLinkedStringList {
    private Node current;
    private String[] fullWordList;

    private class Node{
        public String letters;
        public String[] wordList;
        public Dictionary<String,Node> next;
        public Node previous;

        public Node(String letters, String[] wordList){
            this.letters = letters;
            this.wordList = wordList;
            this.next = new Hashtable<>();
            this.previous = null;
        }
    }

    public AdaptingLinkedStringList(){
        // Specify the path to your file
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
                for(int i=0; i<words.length; i++){
                    words[i] = words[i].toLowerCase();
                }
                
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

        reset();
    }

    public void addLetter(String letter){
        String newLetters = (current.letters+letter).toLowerCase();
        // New entry
        if(current.next.get(newLetters) == null){
            current.next.put(newLetters,new Node(newLetters, pareWordList(current.wordList,newLetters)));
            Node previous = current;
            current = current.next.get(newLetters);
            current.previous = previous;
        }
        else{
            current = current.next.get(newLetters);
        }
        
    }

    private String[] pareWordList(String[] wordList, String part) {
        ArrayList<String> newWordList = new ArrayList<>();
        // Show the user what they have typed so far but don't include the last letter typed because it will already be there
        newWordList.add(part.substring(0, part.length()-1));
        // Add all words with that part
        for(String word : wordList){
            if(word.contains(part)){
                newWordList.add(word);
            }
        }

        return newWordList.toArray(new String[0]);
    }

    public void removeLastLetter(){
        if(current.previous != null){
            current = current.previous;
        }
    }

    public void reset(){
        current = new Node("", fullWordList);
    }

    public String[] getWordList(){
        return this.current.wordList;
    }

    public String getLetters(){
        return this.current.letters;
    }
}
