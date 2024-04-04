/**
 * @author Jackson Alexman
 * @version Updated: 4/01/2024
 */
public class SimpleGuess extends GuessPanel {
    /**
     * @param GUESS_REVEAL_RATIO The number of guesses divided by the number of reveals for every guess/reveal cycle. Must be greater than 0 and less than MAX_GUESSES.
     * @param MAX_GUESSES The maximum number of guesses allowed for each image. Must be greater than 0 and greater than GUESS_REVEAL_RATIO.
     * @param targetWord The word the user is trying to guess. Must be in WordList.txt.
     */
    public SimpleGuess(int GUESS_REVEAL_RATIO, int MAX_GUESSES, String targetWord){
        super(GUESS_REVEAL_RATIO,MAX_GUESSES, targetWord);
        if(GUESS_REVEAL_RATIO >= MAX_GUESSES){
            throw new Error("GUESS_REVEAL_RATIO must be less than MAX_GUESSES. Otherwise, the user would be unable to reveal anything.");
        }
    }
}
