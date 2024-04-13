import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated: 4/12/2024
 */
public class Game extends JFrame{
    /**
     * The chosen GuessPanel for this Game
     */
    private GuessPanel guessPanel;
    /**
     * The chosen RevealPanel for this Game
     */
    private RevealPanel revealPanel;
    /**
     * A non-negative integer less than or equal to 5 representing the difficulty for this game. Higher numbers are more difficult.
     */
    private int difficulty;
    /**
     * The word to guess
     */
    private String targetWord;

    public Game(int guessPanel,int revealPanel, int difficulty){
        BufferedImage image = null;
        String fileName = null;
        // Specify the directory path
        String directoryPath = "Images";

        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Get all files in the directory
        File[] files = directory.listFiles();

        // Pick a random image and load it
        Random r = new Random();
        try{
            File file = files[r.nextInt(files.length)];
            image = ImageIO.read(file);
            fileName = file.getName(); // Store the file name

        }
        catch(Exception e){
            System.out.println(e);
            System.exit(1);
        }

        this.targetWord = fileName.substring(0,fileName.indexOf("."));
        this.difficulty = difficulty;

        switch(guessPanel){
            case 0: this.guessPanel = new SimpleGuess(this.targetWord,1,false,5); break;
            default: this.guessPanel = new SimpleGuess(this.targetWord,1,false,5); break;
        }

        switch(revealPanel){
            case 0: this.revealPanel = new SimpleReveal(image,targetWord,1,false); break;
            case 1: this.revealPanel = new RevealByColor(image,targetWord,1,false, 20); break;
            default: this.revealPanel = new SimpleReveal(image,targetWord,1,false); break;
        }

        this.startGame();
    }


    /**
     * Sets up the guessPanel and revealPanel then adds them to this (frame).
     */
    private void startGame(){
        // General Constraints
        this.setLayout(new GridBagLayout());
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

        guessPanel.setupContentArea();
        revealPanel.setupContentArea();

        // Configure the frame
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}