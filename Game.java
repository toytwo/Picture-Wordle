import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class Game{
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

        targetWord = fileName.substring(0,fileName.indexOf("."));
        this.guessPanel = new SimpleGuess(1,5,targetWord);
        this.revealPanel = new RevealByColor(image);
        this.difficulty = difficulty;

        this.startGame();
    }

    private void startGame(){
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(revealPanel);
        splitPane.setRightComponent(guessPanel.getPanel());
        splitPane.setDividerLocation(1200);
        // splitPane.setDividerSize(0);

        guessPanel.setupContentArea();
        revealPanel.setupContentArea();

        JFrame f = new JFrame();
        f.add(splitPane);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        f.setUndecorated(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}