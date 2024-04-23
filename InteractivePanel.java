import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
public abstract class InteractivePanel extends JPanel {
    /**
     * The number of actions per swap.
     */
    protected int SWAP_THRESHOLD;
    /**
     * The number of user interactions performed for this panel.
     */
    protected int interactionCount;
    /**
     * The max number of user actions that can be performed for this panel.
     */
    protected int MAX_ACTIONS;
    /**
     * The word to be guessed
     */
    protected String targetWord;
    /**
     * If only one panel can be accessed at a time i.e. use the swap threshold
     */
    protected boolean doSwapThreshold;
    /**
     * The other interactive panel in Game.
     */
    protected InteractivePanel otherPanel;

    public InteractivePanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold) {
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;

        this.interactionCount = 0;
    }

    public InteractivePanel(LayoutManager layout, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold) {
        super(layout); // call jpanel constructor
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;

        this.interactionCount = 0;
    }

    /**
     * Sets up the components in the panel.
     */
    public abstract void setupContentArea();

    /**
     * Called when an action is performed in this panel.
<<<<<<< Updated upstream
     */
    public void interactionPerformed(){
        this.interactionCount++;
        swap();
=======
     * 
     * @return If it's time to swap
     */
    public boolean interactionPerformed() {
        this.interactionCount++;
        subtractPoint();
        return swap();
>>>>>>> Stashed changes
    }

    // public boolean subtractPoint() {
    // this.interactionCount++;
    // return swap();
    // }

    // public abstract mehtod to be overriden by subclasses
    public abstract void subtractPoint();

    // public abstract subtractPoint(int interactionType) {
    // int pointsToSubtract = 0;
    // switch (interactionType) {
    // case 0: // guessubtractPoint
    // pointsToSubtract = 20;
    // break;
    // case 1: // reveal
    // pointsToSubtract = 50;
    // break;
    // default:
    // // Handle unexpected difficulty level (optional)
    // pointsToSubtract = 5; // Default to EASY behavior
    // System.out.println("Ignoring unknown difficulty level: " + interactionType);
    // // Use warn instead of err
    // // for
    // // potential issues
    // }
    // Game.game.score -= pointsToSubtract;

    // // Prevent negative interactionCount
    // }

    /**
     * Check if the actionCount meets the swapThreshold. If so, swap panels.
     * 
     * @return If enough actions have been performed and it is time to swap to the
     *         other panel
     */
    final public boolean swap() {
        if (!doSwapThreshold) {
            return false; // no swapThreshold reached
        }

        // If the swap threshold has been met, deactivate this panel and activate
        // otherPanel
        if (interactionCount % SWAP_THRESHOLD == 0) {
            this.setPanelEnabled(false);
            otherPanel.setPanelEnabled(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the instance variable otherPanel
     * 
     * @param otherPanel the other interactive panel in Game
     */
    final public void setOtherPanel(InteractivePanel otherPanel) {
        this.otherPanel = otherPanel;
    }

    /**
     * Enable or disable certain interactable components in this panel
     */
    public abstract void setPanelEnabled(boolean isEnabled);
}

// InteractivePanel panel = new InteractivePanel("targetWord", 5, true) {
// @Override
// public void setupContentArea() {
// // Set up components
// }

// @Override
// public void setPanelEnabled(boolean isEnabled) {
// // Enable or disable components
// }
// };

// // ... user interaction happens ...

// panel.subtractPoint(Difficulty.EASY); // Subtract 5 points
// panel.subtractPoint(Difficulty.MEDIUM); // Subtract 8 point
// panel.subtractPoint(Difficulty.HARD); // Subtract 10 point
