<<<<<<< Updated upstream
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
=======

/**
 * Author: Jackson Alexman
 * Version: Updated: 4/17/2024
>>>>>>> Stashed changes
 */
import javax.swing.JFrame;

public class Main {
    private final static boolean skipMenu = true; // Developer tool for skipping the main menu
    public final static int defaultGuessPanel = 0; // 0 - SimpleGuess
    public final static int defaultRevealPanel = 2; // 0 - SimpleReveal | 1 - ColorReveal | 2 - SpotlightReveal
    public final static int defaultDifficulty = 1; // 0 - EASY | 1 - MEDIUM | 2 - HARD
    public final static boolean defaultDoModularDifficulty = false; // True - Images change their difficulty based on number of guesses used | False - Otherwise

<<<<<<< Updated upstream
    public static void main(String[] args) {
        if(skipMenu){
            new Game(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
        else{
            new PicturMenu(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
=======
    /** The main JFrame for the application, representing the game window. */
    public static JFrame f;

    /**
     * The main method, which is the entry point of the application.
     * It creates an instance of the Game class and initializes the main JFrame.
     * 
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        f = new Game(0, 1, 0); // Create an instance of the Game class
>>>>>>> Stashed changes
    }
}
    