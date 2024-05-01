/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
public class SimpleGuess extends GuessPanel {
    /**
     * @param SWAP_THRESHOLD The number of guesses to be made before the user can reveal. Must be greater than 0.
     * @param MAX_GUESSES The maximum number of guesses allowed for each image. Must be greater than 0..
     * @param targetWord The word the user is trying to guess. Must be in WordList.txt.
     */
    public SimpleGuess(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES, int GUESS_COST){
        super(targetWord,SWAP_THRESHOLD,doSwapThreshold,MAX_GUESSES, GUESS_COST);
    }
}
