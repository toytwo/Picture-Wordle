/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
/**
 * The SimpleGuess class represents a panel where users can make guesses to
 * reveal an image.
 * It extends the GuessPanel class.
 */
public class SimpleGuess extends GuessPanel {
    /**
<<<<<<< Updated upstream
     * @param SWAP_THRESHOLD The number of guesses to be made before the user can reveal. Must be greater than 0.
     * @param MAX_GUESSES The maximum number of guesses allowed for each image. Must be greater than 0..
     * @param targetWord The word the user is trying to guess. Must be in WordList.txt.
     */
    public SimpleGuess(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES){
        super(targetWord,SWAP_THRESHOLD,doSwapThreshold,MAX_GUESSES);
=======
     * Constructor to initialize a SimpleGuess object with the specified target
     * word, swap threshold,
     * swap threshold functionality flag, and maximum number of guesses.
     * 
     * @param targetWord      The word the user is trying to guess.
     * @param SWAP_THRESHOLD  The number of guesses to be made before the user can
     *                        reveal.
     * @param doSwapThreshold Whether or not the user swaps between guessing and
     *                        revealing.
     * @param MAX_GUESSES     The maximum number of guesses allowed for each image.
     */
    public SimpleGuess(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES) {
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, MAX_GUESSES);
    }

    /**
     * Override method to subtract points from the user's score when an action is
     * performed.
     */
    @Override
    public void subtractPoint() {
        Game.game.score -= 50;
>>>>>>> Stashed changes
    }
}
// Path: GuessPanel.java