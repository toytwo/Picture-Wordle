import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated: 4/30/2024
 */
public class Game extends JFrame {
    private GuessPanel guessPanel;
    private RevealPanel revealPanel;
    public SkipActionPanel skipActionPanel;
    public ScorePanel scorePanel;
    public int difficulty;
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
    public static Game game;
    private Random random;
    private boolean doModularDifficulty;
    private static final int MAX_GUESSES = 5;
    private static final int MAX_REVEALS = 10;
    public static final int REVEAL_SWAP_THRESHOLD = 2;
    public static final int GUESS_SWAP_THRESHOLD = 1;
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
    public static final int REVEAL_HINT_COST = 60;
    /**
     * Images that haven't been selected for revealing during this session. Resets when all potential images have been selected.
     */
    private Map<String, File> unselectedImages;
    /**
     * The player's cumulative score. Updated after each image.
     */
    private int totalScore;
    
    public Game(int guessPanelID, int revealPanelID, int difficulty, boolean doModularDifficulty) {
        this.difficulty = difficulty;
        this.previousDifficulty = -1; // Ensure it's different from difficulty
        this.totalScore = 0;
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.doModularDifficulty = doModularDifficulty;
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

        game = this;

        resetGame();
    }

    private void pickRandomImage() {
        updateImagePool();

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
     */
    private void updateImagePool(){
        if(this.previousDifficulty != this.difficulty){
            String filePath = "Images"+File.separator;
            if(this.difficulty == 0){
                filePath+="Easy";
            }
            else if (this.difficulty == 1){
                filePath+="Medium";
            }
            else{
                filePath+="Hard";
            }
            
            this.images = new File(filePath+File.separator).listFiles();
            this.imagePool = new File("Images"+File.separator+"Unsorted").listFiles();

            this.previousDifficulty = this.difficulty;
        }
        
        // No unsorted images to sort
        if(this.imagePool.length == 0){
            this.imagePool = this.images;
            this.scorePanel = new ScorePanel(difficulty, this.totalScore);
        }
        // Use unsorted images
        else{
            int UNSORTED_IMAGE_DIFFICULTY = 3;
            this.scorePanel = new ScorePanel(UNSORTED_IMAGE_DIFFICULTY, this.totalScore);
        }

        // Guessed all images in folder
        if(unselectedImages.size() == 0){
            unselectedImages = new HashMap<String,File>();
            for(File image : imagePool){
                unselectedImages.put(image.getName(),image);
            }
        }
    }

    public void resetGame() {
        if(this.revealPanel != null || this.guessPanel != null || this.scorePanel != null){
            this.remove(this.revealPanel);
            this.remove(this.guessPanel);
            this.guessPanel = null;
            this.revealPanel = null;
            this.scorePanel = null;
            this.revalidate();
            this.repaint();
        }

        pickRandomImage();

        try{
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf(' '));
        }
        catch(Exception e){
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf('.'));
        }
        

        switch (guessPanelID) {
            case 0:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES, GUESS_COST);
                break;

            default:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES, GUESS_COST);
                break;
        }

        switch (revealPanelID) {
            case 0:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS, REVEAL_COST);
                break;

            case 1:
                this.revealPanel = new ColorReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, true, MAX_REVEALS, REVEAL_COST, 16);
                break;

            case 2:
                this.revealPanel = new SpotlightReveal(targetWord, REVEAL_SWAP_THRESHOLD, true, image, MAX_REVEALS, REVEAL_COST);
                break;

            default:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS, REVEAL_COST);
                break;
        }

        this.skipActionPanel = new SkipActionPanel();

        this.guessPanel.setOtherPanel(this.revealPanel);
        this.revealPanel.setOtherPanel(this.guessPanel);

        this.guessPanel.setScorePanel(this.scorePanel);
        this.revealPanel.setScorePanel(this.scorePanel);

        if(this.guessPanel.doSwapThreshold){
            this.guessPanel.setSkipActionPanel(this.skipActionPanel);
        }

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

        this.revealPanel.setupContentArea();
        this.guessPanel.setupContentArea();
        this.skipActionPanel.setupContentArea();

        this.guessPanel.setPanelEnabled(false);
        this.skipActionPanel.setEnabledPanel(this.revealPanel);
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

        String difficulty;
        if(newAverageGuessPercentage > 0.8){
            difficulty = "Hard";
        }
        else if(newAverageGuessPercentage > 0.4){
            difficulty = "Medium";
        }
        else{
            difficulty = "Easy";
        }
        String newFilePath = "Images"+File.separator+difficulty+File.separator;

        // Rename the file
        File newImageFile = new File(newFilePath+newFileName);
        if (imageFile.renameTo(newImageFile)) {
            // Do Nothing
        } else {
            System.err.println("Failed to rename the file.");
            System.exit(0);
        }
    }

    /**
     * Updates totalScore based on the score recieved from an image
     * @param imageScore The score the player earned from an image
     */
    public void updateTotalScore(){
        this.totalScore += this.scorePanel.getImageScore();
    }

    /**
     * @return The current total score
     */
    public int getTotalScore(){
        return this.totalScore;
    }
}