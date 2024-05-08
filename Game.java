import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Jackson Alexman
 * @version Updated: 5/8/2024
 */
public class Game extends JFrame {
    private Component placeholderSpace;
    private GuessPanel guessPanel;
    private RevealPanel revealPanel;
    private int difficulty;
    private int previousDifficulty;
    private String targetWord;
    private BufferedImage image;
    private File imageFile;
    private String imageFileName;
    /**
     * The list of images to be randomly selected from.
     */
    private File[] imagePool;
    /**
     * The list of images for the selected difficulty.
     */
    private File[] images;
    private int guessPanelID;
    private int revealPanelID;
    private int imageModeID;
    private static Game game;
    private Random random;
    private boolean doModularDifficulty;
    private static final int MAX_GUESSES = 5;
    private static final int MAX_REVEALS = 10;
    private static final int REVEAL_SWAP_THRESHOLD = 2;
    private static final int GUESS_SWAP_THRESHOLD = 1;
    /**
     * How many points are subtracted for each guess.
     */
    private static final int GUESS_COST = 20;
    /**
     * How Many points are subtracted for each reveal.
     */
    private static final int REVEAL_COST = 40;
    /**
     * How many points are subtracted for each manual hint reveal.
     */
    private static final int REVEAL_HINT_COST = 60;
    /**
     * Images that haven't been selected for revealing during this session. Resets when all potential images have been selected.
     */
    private Map<String, File> unselectedImages;
    /**
     * The player's cumulative score. Updated after each image.
     */
    private int totalScore;
    /**
     * The max number of images (rounds) per game. -1 if there is no limit.
     */
    private int imageLimit;
    /**
     * How many images (rounds) have been started.
     */
    private int imageCount;
    /**
     * True if points are enabled. False otherwise.
     */
    private boolean pointsEnabled;
    /**
     * The types of images used
     */
    private String imageMode;
    
    public Game(int guessPanelID, int revealPanelID, int difficulty, boolean doModularDifficulty, int imageLimit, boolean pointsEnabled, int imageModeID) {
        this.difficulty = difficulty;
        this.previousDifficulty = -1; // Ensure it's different from difficulty
        this.totalScore = 0;
        this.imageCount = 0;
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.imageModeID = imageModeID;
        this.doModularDifficulty = doModularDifficulty;
        this.pointsEnabled = pointsEnabled;
        this.imageLimit = imageLimit;
        this.unselectedImages = new HashMap<String,File>();
        this.random = new Random();

        this.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setTitle("Pictur");
        BufferedImage iconImage = null;
        try {
            iconImage = ImageIO.read(new File("PicturLogo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setIconImage(iconImage);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        switch (imageModeID) {
            case 0:
                this.imageMode = "Images";
                break;
            case 1:
                this.imageMode = "Icon_Images";
                break;
            default:
                this.imageMode = "Images";
                break;
        }

        game = this;

        resetGame();
    }

    private void pickRandomImage() {
        updateImagePool(this.difficulty);

        try {
            // Pick a random image from the list of unselected images
            String randomImageName = (String) unselectedImages.keySet().toArray()[random.nextInt(unselectedImages.size())];
            imageFile = unselectedImages.get(randomImageName);
            image = ImageIO.read(imageFile);
            imageFileName = randomImageName; 

            // Remove it once it's been selected
            unselectedImages.remove(randomImageName);

        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Determines the pool of images to pick from for revealing. Prioritizes unsorted images if there are unsorted images left to sort.
     * @param difficulty Used to track if the recursive loop has checked all difficulties
     */
    private void updateImagePool(int difficulty){
        if(this.previousDifficulty != difficulty){
            String filePath = imageMode+File.separator;
            if(difficulty == 0){
                filePath+="Easy";
            }
            else if (difficulty == 1){
                filePath+="Medium";
            }
            else{
                filePath+="Hard";
            }
            
            this.images = new File(filePath+File.separator).listFiles();
            this.imagePool = new File(imageMode+File.separator+"Unsorted").listFiles();

            this.previousDifficulty = difficulty;
        }
        
        // No unsorted images to sort
        if(this.imagePool.length == 0){
            this.imagePool = this.images;
            // This difficulty has no images
            if(this.imagePool.length == 0){
                difficulty = (difficulty + 1)%3;
                if(difficulty == this.difficulty){
                    System.err.println("No Images Found");
                    System.exit(0);
                }
                // Check other difficulties
                updateImagePool(difficulty);
                return; // Prevent the code below from activating multiple times
            } 
        }

        this.difficulty = difficulty; // In case the difficulty was changed from the above code

        // Guessed all images in folder
        if(unselectedImages.size() == 0){
            unselectedImages = new HashMap<String,File>();
            for(File image : imagePool){
                unselectedImages.put(image.getName(),image);
            }
        }
    }

    public void resetGame() {
        this.imageCount++;

        pickRandomImage();

        try{
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf(' '));
        }
        catch(Exception e){
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf('.'));
        }

        // For cheating/debugging
        if(Main.skipMenu){
            System.out.println(this.targetWord);
        }

        try{
            revealPanel.resetPanel(this.targetWord, this.image);
            guessPanel.resetPanel(this.targetWord);
        }
        catch(Exception e){
            if(imageCount!=1){
                System.err.println(e);
                System.exit(0);
            }
            initializePanels();
        }

        if(this.revealPanel.doSwapThreshold && this.guessPanel.doSwapThreshold){
            this.guessPanel.setPanelEnabled(false);
        }

        else{
            SwingUtilities.invokeLater(() -> {this.guessPanel.setPanelEnabled(true);});
            this.revealPanel.doSwapThreshold = false;
            this.guessPanel.doSwapThreshold = false;
        }
        
    }

    /**
     * Updates the difficulty of the image based on the percentage of guesses used
     * @param guessesMade How many guesses it took to guess the image
     * @param MAX_GUESSES How many guesses were allowed in total
     */
    public void updateImageDifficulty(int guessesMade, int MAX_GUESSES, boolean wasCorrectGuess){
        if(!this.doModularDifficulty){
            return;
        }

        /**
         * The percent of MAX_GUESSES used
        */
        double guessPercentage = (double) guessesMade / MAX_GUESSES;
        double newAverageGuessPercentage;
        String newFileName;
        int firstPeriod = imageFileName.indexOf('.');
        int secondPeriod = imageFileName.lastIndexOf('.');
        // No average found
        if(firstPeriod == secondPeriod){
            newAverageGuessPercentage = guessPercentage;
            newFileName = imageFileName.substring(0,imageFileName.indexOf('.'))+" "+newAverageGuessPercentage+".jpg";
        }
        // Average found
        else{
            newAverageGuessPercentage = (Double.parseDouble(imageFileName.substring(firstPeriod, secondPeriod))+guessPercentage)/2.0;
            newFileName = imageFileName.substring(0,imageFileName.indexOf(' '))+" "+newAverageGuessPercentage+".jpg";
        }

        double totalAverageDifficulty = calculateAverageDifficulty(this.targetWord, newAverageGuessPercentage);

        String difficulty;
        if(newAverageGuessPercentage > 0.7 * totalAverageDifficulty){
            difficulty = "Hard";
        }
        else if(newAverageGuessPercentage > 0.3 * totalAverageDifficulty){
            difficulty = "Medium";
        }
        else{
            difficulty = "Easy";
        }
        String newFilePath = imageMode+File.separator+difficulty+File.separator;

        // Rename the file
        File newImageFile = new File(newFilePath+newFileName);
        if (imageFile.renameTo(newImageFile)) {
            // Do Nothing
        } else {
            System.err.println("Failed to rename the file.");
            System.exit(0);
        }
    }

    private void initializePanels(){
        switch (guessPanelID) {
            case 0:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, MAX_GUESSES, GUESS_COST, pointsEnabled);
                break;

            case 1:
                this.guessPanel = new ScoreGuess(this.targetWord, GUESS_SWAP_THRESHOLD, MAX_GUESSES, GUESS_COST, pointsEnabled);
                break;


            default:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, MAX_GUESSES, GUESS_COST, pointsEnabled);
                break;
        }

        switch (revealPanelID) {
            case 0:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, MAX_REVEALS, REVEAL_COST, pointsEnabled);
                break;

            case 1:
                this.revealPanel = new ColorReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, MAX_REVEALS, REVEAL_COST, 16, pointsEnabled);
                break;

            case 2:
                this.revealPanel = new SpotlightReveal(targetWord, REVEAL_SWAP_THRESHOLD, image, MAX_REVEALS, REVEAL_COST, pointsEnabled);
                break;

            default:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, MAX_REVEALS, REVEAL_COST, pointsEnabled);
                break;
        }

        this.revealPanel.setOtherPanel(this.guessPanel);
        this.guessPanel.setOtherPanel(this.revealPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0; // Y position in the grid
        constraints.fill = GridBagConstraints.BOTH; // Fill horizontally and vertically
        constraints.weighty = 1; // All of the height
        constraints.gridwidth = 1; // All of the width

        // Add the revealPanel
        constraints.gridx = 0; // X position in the grid
        constraints.weightx = this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(this.revealPanel,constraints);

        // Add the guessPanel
        constraints.gridx = 1; // X position in the grid
        constraints.weightx = 1.0-this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(this.guessPanel,constraints);
    }

    /**
     * Updates the difficulty database then finds the average.
     * @param targetWord The name of the image to update the difficulty of within the database
     * @param targetWordDifficulty The new difficulty of the image.
     * @return The average of every stored image difficulty
     */
    private double calculateAverageDifficulty(String targetWord, double targetWordDifficulty){
        targetWord = targetWord.toLowerCase();
        double difficultySum = 0;
        int difficultyCount = 0;
        boolean isTargetWordPresent = false;
        String fileName = "ImageDifficulties.txt";
        File file = new File(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                difficultyCount++;
                int separatorIndex = line.indexOf(" ");
                String word = line.substring(0, separatorIndex);
                double difficulty = Double.parseDouble(line.substring(separatorIndex + 1));

                if (word.equals(targetWord)) {
                    isTargetWordPresent = true;
                    difficulty = targetWordDifficulty;
                    line = targetWord + " " + targetWordDifficulty;
                }

                writer.write(line + System.lineSeparator());
                difficultySum += difficulty;
            }

            if(!isTargetWordPresent){
                line = targetWord + " " + targetWordDifficulty;
                writer.write(line + System.lineSeparator());
            }

            reader.close();
            writer.close();

            if (!file.delete()) {
                System.out.println("Could not delete file");
                return -1;
            }
            
            File tempFile = new File("temp.txt");
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename file");
                return -1;
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            return -1;
        }

        return difficultySum / difficultyCount;
    }

    /**
     * Updates totalScore based on the score recieved from an image if doSubtractPoints is true.
     * @param imageScore The score the player earned from an image
     */
    public void updateTotalScore(){
        if(pointsEnabled){
            this.totalScore += this.guessPanel.scorePanel.getImageScore();
        }
    }

    public void swapGuessPanel(boolean showGuessPanel){
        if(placeholderSpace == null){
            placeholderSpace = Box.createRigidArea(new Dimension(this.guessPanel.getSize()));
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH; 
        constraints.weighty = 1; 
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.weightx = 1.0-this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE();

        if(showGuessPanel){
            this.remove(placeholderSpace);
            this.add(this.guessPanel,constraints);
        }
        else{
            this.remove(this.guessPanel);
            this.add(placeholderSpace, constraints);
        }

        repaint();
    }

    public int getTotalScore(){
        return this.totalScore;
    }

    public int getImageLimit(){
        return this.imageLimit;
    }

    public boolean isPointsEnabled(){
        return this.pointsEnabled;
    }

    /**
     * @return Null if doSubtractPoints is false.
     */
    public SkipActionPanel getSkipActionPanel(){
        return this.guessPanel.skipActionPanel;
    }

    public static Game getCurrentGame(){
        return game;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * @return Null if doSubtractPoints is false.
     */
    public ScorePanel getScorePanel(){
        return this.guessPanel.scorePanel;
    }

    public static int getREVEAL_HINT_COST(){
        return REVEAL_HINT_COST;
    }

    public int getGuessPanelID(){
        return this.guessPanelID;
    }

    public int getRevealPanelID(){
        return this.revealPanelID;
    }

    public boolean getDoModularDifficulty(){
        return this.doModularDifficulty;
    }

    public int getImageMode(){
        return this.imageModeID;
    }

    /**
     * Sets the difficulty if it is valid
     * @param newDifficulty Must be between 0 and 2 inclusive
     * @return If the difficulty was set to newDifficulty 
     */
    public boolean setDifficulty(int newDifficulty){
        if(newDifficulty < 0 || newDifficulty > 2){
            return false;
        }
        
        this.difficulty = newDifficulty;
        return true;
    }
}