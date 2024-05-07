/**
 * @author Jackson Alexman
 * @version Updated: 5/6/2024
 */
public class Main {
    private final static boolean skipMenu = false; // Developer tool for skipping the main menu
    public final static int defaultGuessPanel = 1; // 0 - SimpleGuess | 1 - ScoreGuess
    public final static int defaultRevealPanel = 2; // 0 - SimpleReveal | 1 - ColorReveal | 2 - SpotlightReveal
    public final static int defaultDifficulty = 1; // 0 - EASY | 1 - MEDIUM | 2 - HARD
    public final static int defaultImageMode = 0; // 0 - Images | 1 - Icons
    public final static boolean defaultPointsEnabled = true; // Track points for image guess percentage
    public final static boolean defaultDoModularDifficulty = true; // True - Images change their difficulty based on number of guesses used | False - Otherwise

    public static void main(String[] args) {
        if(skipMenu){
            new Game(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty, 10, defaultPointsEnabled, defaultImageMode);
        }
        else{
            new PicturMenu(defaultGuessPanel, defaultRevealPanel, defaultDifficulty, defaultDoModularDifficulty, defaultPointsEnabled, defaultImageMode);
        }
    }
}
