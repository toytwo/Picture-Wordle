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
    private int difficulty;
    private String targetWord;
    private BufferedImage image;
    private String imageFileName;
    private File[] images;
    private int guessPanelID;
    private int revealPanelID;
    public static Game game;
    private Random random;

    public Game(int guessPanelID, int revealPanelID, int difficulty) {
        this.difficulty = difficulty;
        this.guessPanelID = guessPanelID;
        this.revealPanelID = revealPanelID;
        this.images = new File("Images").listFiles();
        this.random = new Random();

        this.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game = this;

        setupGame();
    }

    private void pickRandomImage(int difficulty) {
        try {
            File file = images[random.nextInt(images.length)];
            image = ImageIO.read(file);
            imageFileName = file.getName(); 

        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
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
        this.add(revealPanel,constraints);

        // Add the guessPanel
        constraints.gridx = 1; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = 1.0-this.revealPanel.getREVEAL_PANEL_SCREEN_PERCENTAGE(); // Fraction of the width
        this.add(guessPanel,constraints);

        revealPanel.setupContentArea();
        guessPanel.setupContentArea();

        guessPanel.setPanelEnabled(false);
    }
}