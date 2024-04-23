import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
public class Game extends JFrame {
    private GuessPanel guessPanel;
    private RevealPanel revealPanel;
<<<<<<< Updated upstream
    private int difficulty;
    private String targetWord;
    private BufferedImage image;
    private String imageFileName;
    private File[] images;
    private int guessPanelID;
    private int revealPanelID;
    public static Game game;
    private Random random;
=======
    private int difficulty; // 0 = easy, 1 = medium, 2 = hard
    private String targetWord; // the word to be guessed
    private BufferedImage image; // the image to be revealed
    private File imageFile; // the image file to be revealed
    private String imageFileName; // the name of the image file to be revealed
    private File[] images; // the images to be revealed
    private File[] unsortedImages; // the unsorted images to be revealed
    private int guessPanelID; // only simple is used at the moment
    private int revealPanelID; // only simple is used at the moment
    public static Game game;
    private Random random;
    private final boolean doModularDifficulty = false; //
    int score = 0;
>>>>>>> Stashed changes

    public Game(int guessPanelID, int revealPanelID, int difficulty) {
        this.difficulty = difficulty;
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.images = new File("Images").listFiles();
        this.random = new Random();

<<<<<<< Updated upstream
=======
        String filePath = "Images" + File.separator;
        if (this.difficulty == 0) {
            filePath += "Easy";
            score += 500;
        } else if (this.difficulty == 1) {
            filePath += "Medium";
            score += 500;
        } else {
            filePath += "Hard";
            score += 1000;
        }
        this.images = new File(filePath + File.separator).listFiles();
        this.unsortedImages = new File("Images/Unsorted").listFiles();

>>>>>>> Stashed changes
        this.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game = this;

        setupGame();
    }

<<<<<<< Updated upstream
    private void pickRandomImage(int difficulty) {
        try {
            File file = images[random.nextInt(images.length)];
            image = ImageIO.read(file);
            imageFileName = file.getName(); 

        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
=======
    private void pickRandomImage() {
        if (unsortedImages.length > 0) {
            try {
                imageFile = unsortedImages[random.nextInt(unsortedImages.length)];
                image = ImageIO.read(imageFile);
                String oldFileName = imageFile.getName();
                String newFileName = oldFileName.substring(0, oldFileName.indexOf('.')) + " 0.0.jpg";
                String newFilePath = "Images" + File.separator + "Easy" + File.separator;

                // Rename the file
                File newImageFile = new File(newFilePath + newFileName);
                if (imageFile.renameTo(newImageFile)) {
                    // Do Nothing
                } else {
                    System.err.println("Failed to rename the file.");
                    System.exit(0);
                }

                imageFileName = newFileName;

            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        } else {
            try {
                imageFile = images[random.nextInt(images.length)];
                image = ImageIO.read(imageFile);
                imageFileName = imageFile.getName();

            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
>>>>>>> Stashed changes
        }
    }

    public void setupGame() {
        if (this.revealPanel != null || this.guessPanel != null) {
            this.guessPanel.setEnabled(false);
            this.revealPanel.setEnabled(false);
            this.remove(this.revealPanel);
            this.remove(this.guessPanel);
            this.guessPanel = null;
            this.revealPanel = null;
            this.revalidate();
            this.repaint();
        }

        pickRandomImage(difficulty);

        this.targetWord = imageFileName.substring(0, imageFileName.indexOf("."));

        switch (guessPanelID) {
            case 0:
                this.guessPanel = new SimpleGuess(this.targetWord, 1, true, 5);
                break;
            default:
                this.guessPanel = new SimpleGuess(this.targetWord, 1, true, 5);
                break;
        }

        switch (revealPanelID) {
            case 0:
                this.revealPanel = new SimpleReveal(image, targetWord, 1, false);
                break;
            case 1:
                this.revealPanel = new RevealByColor(image, targetWord, 2, true, 20);
                break;
            default:
                this.revealPanel = new SimpleReveal(image, targetWord, 1, false);
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
        this.add(revealPanel, constraints);

        // Add the guessPanel
        constraints.gridx = 1; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = 1.0 - this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(guessPanel, constraints);

        revealPanel.setupContentArea();
        guessPanel.setupContentArea();

        guessPanel.setPanelEnabled(false);
    }
<<<<<<< Updated upstream
=======

    /**
     * Updates the difficulty of the image based on the percentage of guesses used
     * 
     * @param guessesMade How many guesses it took to guess the image
     * @param MAX_GUESSES How many guesses were allowed in total
     */
    public void updateImageDifficulty(int guessesMade, int MAX_GUESSES) {
        if (!this.doModularDifficulty) {
            return;
        }

        /**
         * The percent of MAX_GUESSES used
         */
        double guessPercentage = (double) guessesMade / MAX_GUESSES;
        double newAverageGuessPercentage = (Double.parseDouble(
                imageFileName.substring(imageFileName.indexOf('.'), imageFileName.lastIndexOf('.'))) + guessPercentage)
                / 2.0;
        String newFileName = imageFileName.substring(0, imageFileName.indexOf(' ')) + " " + newAverageGuessPercentage
                + ".jpg";
        String difficulty;
        if (newAverageGuessPercentage > 0.8) {
            difficulty = "Hard";
        } else if (newAverageGuessPercentage > 0.4) {
            difficulty = "Medium";
        } else {
            difficulty = "Easy";
        }
        String newFilePath = "Images" + File.separator + difficulty + File.separator;

        System.out.println(newAverageGuessPercentage + " " + difficulty);
        System.exit(0);

        // Rename the file
        File newImageFile = new File(newFilePath + newFileName);
        if (imageFile.renameTo(newImageFile)) {
            // Do Nothing
        } else {
            System.err.println("Failed to rename the file.");
            System.exit(0);
        }
    }
>>>>>>> Stashed changes
}