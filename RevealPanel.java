import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
<<<<<<< Updated upstream
public abstract class RevealPanel extends InteractivePanel{
    /**
     * The image to be guessed.
     */
    protected BufferedImage image;
=======
/**
 * The RevealPanel class is an abstract class representing a panel where users can reveal parts of an image.
 */
public abstract class RevealPanel extends InteractivePanel {
    /**
     * The image to be guessed.
     */
    protected BufferedImage image; 
>>>>>>> Stashed changes
    /**
     * How much of the screen width the revealPanel occupies. guessPanel will occupy the other part (1-this).
     */
<<<<<<< Updated upstream
    protected double REVEAL_PANEL_SCREEN_PERCENTAGE = 5.0/6.0;

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, int MAX_REVEALS, LayoutManager layout){
        super(layout, targetWord, swapThreshold, doSwapThreshold, MAX_REVEALS);
=======
    protected double REVEAL_PANEL_SCREEN_PERCENTAGE = 5.0 / 6.0;

    /**
     * Constructor to initialize a RevealPanel object with a specified target word,
     * swap threshold, swap threshold functionality flag, image, and layout manager.
     * 
     * @param targetWord      The word to be guessed.
     * @param swapThreshold   The number of actions per swap.
     * @param doSwapThreshold Whether or not the user swaps between guessing and
     *                        revealing.
     * @param image           The image to be revealed.
     * @param layout          The layout manager for the panel.
     */
    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image,
            LayoutManager layout) {
        super(layout, targetWord, swapThreshold, doSwapThreshold);
>>>>>>> Stashed changes
        this.image = image;
        this.setPanelDescriptors("Reveal", "Reveals");
    }
<<<<<<< Updated upstream

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, int MAX_REVEALS){
        super(targetWord, swapThreshold, doSwapThreshold, MAX_REVEALS);
=======
    /**
     * Constructor to initialize a RevealPanel object with a specified target word,
     * swap threshold, swap threshold functionality flag, and image.
     * 
     * @param targetWord      The word to be guessed.
     * @param swapThreshold   The number of actions per swap.
     * @param doSwapThreshold Whether or not the user swaps between guessing and
     *                        revealing.
     * @param image           The image to be revealed.
     */
    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image) {
        super(targetWord, swapThreshold, doSwapThreshold);
>>>>>>> Stashed changes
        this.image = image;
        this.setPanelDescriptors("Reveal", "Reveals");
    }

<<<<<<< Updated upstream
    public double getREVEAL_PANEL_SCREEN_PERCENTAGE(){
=======
    /**
     * Getter method to retrieve the percentage of screen width occupied by the revealPanel.
     * 
     * @return The percentage of screen width occupied by the revealPanel.
     */
    public double getREVEAL_PANEL_SCREEN_PERCENTAGE() {
>>>>>>> Stashed changes
        return REVEAL_PANEL_SCREEN_PERCENTAGE;
    }
}
