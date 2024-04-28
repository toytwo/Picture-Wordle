
/**
 * The Game class represents the main game window of a guessing game or similar
 * application.
 * It manages the interaction between the user and the game components,
 * including guess and reveal panels.
 * 
 * Instance Variables:
 * - guessPanel: Reference to the GuessPanel object where the user interacts to
 * guess the word.
 * - revealPanel: Reference to the RevealPanel object where the image is
 * displayed, possibly with some parts revealed.
 * - difficulty: An integer representing the difficulty level of the game.
 * - targetWord: The word associated with the chosen image.
 * - image: The BufferedImage object storing the loaded image.
 * - imageFileName: The filename of the loaded image.
 * - images: An array of File objects representing the available image files.
 * - guessPanelID: An integer specifying the type of GuessPanel to create.
 * - score: An integer representing the score of the game.
 * - revealPanelID: An integer specifying the type of RevealPanel to create.
 * - random: Random object used for generating random numbers, likely for
 * picking random images.
 * 
 * Methods:
 * - Game(int guessPanelID, int revealPanelID, int difficulty): Constructor to
 * initialize a new game instance.
 * - pickRandomImage(int difficulty): Method to pick a random image based on the
 * difficulty level.
 * - setupGame(): Method to set up the game by creating guess and reveal panels,
 * loading images, and initializing game components.
 * 
 * Usage:
 * - Create an instance of Game with appropriate parameters to start a new game.
 * - Call setupGame() method to set up the game components.
 * - Interact with the game through the guessPanel and revealPanel.
 * - Modify game parameters such as difficulty and panel types as needed.
 * 
 * Author: Jackson Alexman
 * Version: Updated: 4/17/2024
 */
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

<<<<<<< Updated upstream
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
=======
>>>>>>> Stashed changes
public class Game extends JFrame {
    // Reference to the GuessPanel object (where the user interacts to guess the
    // word).
    private GuessPanel guessPanel;
    // Reference to the RevealPanel object (where the image is displayed, possibly
    // with some parts revealed).
    private RevealPanel revealPanel;
<<<<<<< Updated upstream
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
=======
    private int difficulty;
    // The word associated with the chosen image
    private String targetWord; // The word associated with the chosen image
    private BufferedImage image;// stord loaded lmage
    // The filename of the loaded image
    private String imageFileName; // The filename of the loaded image
    // An array of File objects representing the available image files
>>>>>>> Stashed changes
    private File[] images;
    // An integer specifying the type of GuessPanel to create.
    private int guessPanelID;
    public int score;
    // that might be used to specify the type of RevealPanel to create.
    // An integer specifying the type of RevealPanel to create
    private int revealPanelID;
    // This likely holds a reference to the current instance of the Game class
    public static Game game;
    // used for generating random numbers (likely for picking random images).
    private Random random;
<<<<<<< Updated upstream
    private boolean doModularDifficulty;
    private static final int MAX_GUESSES = 5;
    private static final int MAX_REVEALS = 10;
    public static final int REVEAL_SWAP_THRESHOLD = 2;
    public static final int GUESS_SWAP_THRESHOLD = 1;
    /**
     * Images that haven't been selected for revealing during this session. Resets when all potential images have been selected.
     */
    private Map<String, File> unselectedImages;
=======
>>>>>>> Stashed changes

    public Game(int guessPanelID, int revealPanelID, int difficulty, boolean doModularDifficulty) {
        this.difficulty = difficulty;
        this.previousDifficulty = -1; // Ensure it's different from difficulty
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
<<<<<<< Updated upstream
        this.doModularDifficulty = doModularDifficulty;
        this.unselectedImages = new HashMap<String,File>();
        this.random = new Random();

=======
        // Creates array of File objects containing references to all files in the
        // "Images" directory.
        this.images = new File("Images").listFiles();
        this.random = new Random();
        // Sets the layout manager of the JFrame to GridBagLayout for flexible component
        // placemet
>>>>>>> Stashed changes
        this.setLayout(new GridBagLayout());
        // Maximizes the window size using setExtendedState(JFrame.MAXIMIZED_BOTH)
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Removes decorations (title bar, etc.) using setUndecorated(true).
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
        score = 0; // Set the score to 0
        // Sets the static game reference to the current Game object instance.
        game = this; // Set the reference to the current Game object

<<<<<<< Updated upstream
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
=======
        // Calls the setupGame method to set up the game.
        setupGame();

    }

    private void pickRandomImage(int difficulty) {
        try {
            // Pick a random file from the images array using random.nextInt(images.length).
            File file = images[random.nextInt(images.length)];
            // Read the image file into the image object using ImageIO.read(file)
            image = ImageIO.read(file);
            // Extract the filename from the chosen file using file.getName().
            imageFileName = file.getName();
>>>>>>> Stashed changes

        } catch (Exception e) {
            // If any exception occurs during image loading, it prints the error message and
            // exits the program using System.exit(1).
            System.out.println(e);
            System.exit(1);
        }
    }

<<<<<<< Updated upstream
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
        if(this.revealPanel != null || this.guessPanel != null){
=======
    public void setupGame() {
        // check if (guessPanel and revealPanel) are present. If they are, it disables
        // them, removes them from the frame, and sets them to null. It then
        // revalidates and repaints the frame.
        if (this.revealPanel != null || this.guessPanel != null) {
>>>>>>> Stashed changes
            this.guessPanel.setEnabled(false);
            this.revealPanel.setEnabled(false);
            // Removes the existing revealPanel from the frame
            this.remove(this.revealPanel);
            this.remove(this.guessPanel);
            // Sets guessPanel reference to null to indicate not currently displayed.
            this.guessPanel = null;
            this.revealPanel = null;
            this.revalidate();
            this.repaint();
        }
<<<<<<< Updated upstream

        pickRandomImage();

        try{
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf(' '));
        }
        catch(Exception e){
            this.targetWord = imageFileName.substring(0, imageFileName.indexOf('.'));
        }
        
=======
        // pick a random image based on the difficulty.
        pickRandomImage(difficulty);
        // extracts the target word from the chosen image filename: "imageFileName"
        // uses substring(0, imageFileName.indexOf(".")) to get a substring from the
        // beginning of the filename (index 0) up to, but not including, the first
        // occurrence of the dot (.) which likely separates the filename from the
        // extension.
        this.targetWord = imageFileName.substring(0, imageFileName.indexOf("."));
>>>>>>> Stashed changes

        switch (guessPanelID) {
            // If guessPanelID is 0, a new SimpleGuess object is created, likely providing a
            // basic guess panel functionality.
            case 0:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES);
                break;

            default:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES);
                break;
        }

        switch (revealPanelID) {
            case 0:
<<<<<<< Updated upstream
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS);
=======
                // Creates a SimpleReveal object for basic image revealing.
                this.revealPanel = new SimpleReveal(image, targetWord, 1, false);
>>>>>>> Stashed changes
                break;

            case 1:
<<<<<<< Updated upstream
                this.revealPanel = new ColorReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, true, MAX_REVEALS, 16);
=======
                // Creates a RevealByColor object (possibly revealing based on guessed letters'
                // colors).
                this.revealPanel = new RevealByColor(image, targetWord, 2, true, 20);
>>>>>>> Stashed changes
                break;

            case 2:
                this.revealPanel = new SpotlightReveal(targetWord, REVEAL_SWAP_THRESHOLD, true, image, MAX_REVEALS);
                break;

            default:
<<<<<<< Updated upstream
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS);
=======
                // Falls back to creating a SimpleReveal object.
                this.revealPanel = new SimpleReveal(image, targetWord, 1, false);
>>>>>>> Stashed changes
                break;
        }

        this.guessPanel.setOtherPanel(revealPanel);
        // This sets a reference to the guessPanel within the revealPanel
        this.revealPanel.setOtherPanel(guessPanel);

        // Creates a new GridBagConstraints object to define how the panels will be
        // positioned within the JFrame using a GridBagLayout.
        GridBagConstraints constraints = new GridBagConstraints();
        // Sets the row position of both panels to 0 (the first row).
        constraints.gridy = 0; // Y position in the grid
        // Makes both panels fill their assigned cells horizontally and vertically.
        constraints.fill = GridBagConstraints.BOTH;
        // Sets the vertical weight of the components to 1 (all of the height).
        constraints.weighty = 1; // All of the height

        // Add the revealPanel
        // Sets the column position of the revealPanel to 0 (the first column).
        constraints.gridx = 0; // X position in the grid
        // revealPanel should occupy only one cell horizontally.
        constraints.gridwidth = 1; // Number of cells wide
        // Sets the horizontal weight of the revealPanel to the fraction of the width
        // returning a value between 0 and 1
        constraints.weightx = this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(revealPanel,constraints);

        // Add the guessPanel
        // Sets the column position of the guessPanel to 1 (the second column).
        constraints.gridx = 1; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
<<<<<<< Updated upstream
        constraints.weightx = 1.0-this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(guessPanel,constraints);
=======
        // Sets the horizontal weight of the guessPanel to the remaining space after
        // accounting for the revealPanel width
        constraints.weightx = 1.0 - this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        // Adds the guessPanel to the JFrame with the modified constraints.
        this.add(guessPanel, constraints);
>>>>>>> Stashed changes

        /*
         * revealPanel.setupContentArea(); and guessPanel.setupContentArea();: These
         * lines likely call methods on the respective panels to initialize their
         * content areas. This could include setting up the image and word to be
         * displayed, setting up the input fields for guessing, etc.
         */

        revealPanel.setupContentArea();
        guessPanel.setupContentArea();

<<<<<<< Updated upstream
        // revealPanel.setBorder(new LineBorder(Color.blue, 10));

        guessPanel.setPanelEnabled(false);
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
=======
        // This line disables the guessPanel initially. This could be because the user
        // needs to see the image first before they can start guessing.
        guessPanel.setPanelEnabled(false);
    }
>>>>>>> Stashed changes
}