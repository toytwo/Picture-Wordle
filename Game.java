import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private int difficulty;
    private String targetWord;
    private BufferedImage image;
    private File imageFile;
    private String imageFileName;
    private File[] images;
    private File[] unsortedImages;
    private int guessPanelID;
    private int revealPanelID;
    public static Game game;
    private Random random;
    private boolean doModularDifficulty;
    private static final int MAX_GUESSES = 5;
    private static final int MAX_REVEALS = 10;
    private static final int REVEAL_SWAP_THRESHOLD = 2;
    private static final int GUESS_SWAP_THRESHOLD = 1;

    public Game(int guessPanelID, int revealPanelID, int difficulty, boolean doModularDifficulty) {
        this.difficulty = difficulty;
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.doModularDifficulty = doModularDifficulty;
        this.random = new Random();

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
        this.unsortedImages = new File("Images"+File.separator+"Unsorted").listFiles();

        this.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game = this;

        setupGame();
    }

    private void pickRandomImage() {
        if(unsortedImages.length > 0){
            try {
                imageFile = unsortedImages[random.nextInt(unsortedImages.length)];
                image = ImageIO.read(imageFile);
                imageFileName = imageFile.getName(); 
    
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
        else{
            try {
                imageFile = images[random.nextInt(images.length)];
                image = ImageIO.read(imageFile);
                imageFileName = imageFile.getName(); 
    
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
    }

    public void setupGame() {
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