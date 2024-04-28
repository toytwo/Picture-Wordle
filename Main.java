import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
public class Main {

    public static JFrame f;
    private final static boolean skipMenu = false; // Developer tool for skipping the main menu
    private final static int defaultGuessPanel = 0; // 0 - SimpleGuess
    private final static int defaultRevealPanel = 2; // 0 - SimpleReveal | 1 - ColorReveal | 2 - SpotlightReveal
    private final static int defaultDifficulty = 1; // 0 - EASY | 1 - MEDIUM | 2 - HARD
    private final static boolean defaultDoModularDifficulty = false; // True - Images change their difficulty based on number of guesses used | False - Otherwise

    public static void main(String[] args) {
        if(skipMenu){
            f = new Game(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
        else{
            f = new PicturMenu(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
    }
}
