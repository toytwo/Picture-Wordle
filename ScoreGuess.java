/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class ScoreGuess extends GuessPanel {

    /**
     * @param SWAP_THRESHOLD The number of guesses to be made before the user can reveal. Must be greater than 0.
     * @param doSwapThreshold Whether or not to swap between panels using the SWAP_THRESHOLD. Must be true for this panel.
     * @param MAX_GUESSES The maximum number of guesses allowed for each image. Must be greater than 0..
     * @param targetWord The word the user is trying to guess. Must be in WordList.txt.
     * @param GUESS_COST The amount of points lost for a guess
     */
    public ScoreGuess(String targetWord, int SWAP_THRESHOLD, int MAX_GUESSES, int GUESS_COST, boolean pointsEnabled){
        super(targetWord,SWAP_THRESHOLD,MAX_GUESSES, GUESS_COST, pointsEnabled);

        if(!doSwapThreshold){
            System.err.println("doSwapThreshold must be True for this panel.");
            System.exit(0);
        }
        if(!pointsEnabled){
            System.err.println("doSubtractPoints must be True for this panel.");
            System.exit(0);
        }

        this.pointsEnabled = true;

        setupContentArea();
    }
}
