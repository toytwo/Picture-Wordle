import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
public class Game extends JFrame {
    private GuessPanel guessPanel;
    private RevealPanel revealPanel;
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
     * Images that haven't been selected for revealing during this session. Resets when all potential images have been selected.
     */
    private Map<String, File> unselectedImages;

    public Game(int guessPanelID, int revealPanelID, int difficulty, boolean doModularDifficulty) {
        this.difficulty = difficulty;
        this.previousDifficulty = -1; // Ensure it's different from difficulty
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.doModularDifficulty = doModularDifficulty;
        this.unselectedImages = new HashMap<String,File>();
        this.random = new Random();

        this.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
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
            this.guessPanel.setEnabled(false);
            this.revealPanel.setEnabled(false);
            this.remove(this.revealPanel);
            this.remove(this.guessPanel);
            this.guessPanel = null;
            this.revealPanel = null;
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
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES);
                break;

            default:
                this.guessPanel = new SimpleGuess(this.targetWord, GUESS_SWAP_THRESHOLD, true, MAX_GUESSES);
                break;
        }

        switch (revealPanelID) {
            case 0:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS);
                break;

            case 1:
                this.revealPanel = new RevealByColor(image, targetWord, REVEAL_SWAP_THRESHOLD, true, MAX_REVEALS, 20);
                break;

            case 2:
                this.revealPanel = new SpotlightReveal(targetWord, REVEAL_SWAP_THRESHOLD, true, image, MAX_REVEALS);
                break;

            default:
                this.revealPanel = new SimpleReveal(image, targetWord, REVEAL_SWAP_THRESHOLD, false, MAX_REVEALS);
                break;
        }

        this.guessPanel.setOtherPanel(revealPanel);
        this.revealPanel.setOtherPanel(guessPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0; // Y position in the grid
        constraints.fill = GridBagConstraints.BOTH; // Fill horizontally and vertically
        constraints.weighty = 1; // All of the height

        // Add the revealPanel
        constraints.gridx = 0; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(revealPanel,constraints);

        // Add the guessPanel
        constraints.gridx = 1; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = 1.0-this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(guessPanel,constraints);

        revealPanel.setupContentArea();
        guessPanel.setupContentArea();

        // revealPanel.setBorder(new LineBorder(Color.blue, 10));

        guessPanel.setPanelEnabled(false);
    }

    /**
     * Updates the difficulty of the image based on the percentage of guesses used
     * @param guessesMade How many guesses it took to guess the image
     * @param MAX_GUESSES How many guesses were allowed in total
     */
    public void updateImageDifficulty(int guessesMade, int MAX_GUESSES){
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
}