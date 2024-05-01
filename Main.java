/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
public class Main {
    private final static boolean skipMenu = true; // Developer tool for skipping the main menu
    public final static int defaultGuessPanel = 0; // 0 - SimpleGuess
    public final static int defaultRevealPanel = 2; // 0 - SimpleReveal | 1 - ColorReveal | 2 - SpotlightReveal
    public final static int defaultDifficulty = 1; // 0 - EASY | 1 - MEDIUM | 2 - HARD
    public final static boolean defaultDoModularDifficulty = false; // True - Images change their difficulty based on number of guesses used | False - Otherwise

    public static void main(String[] args) {
        if(skipMenu){
            new Game(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
        else{
            new PicturMenu(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty);
        }
    }
}
