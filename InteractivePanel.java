import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated: 4/07/2024
 */

public abstract class InteractivePanel extends JPanel {
    /**
     * The number of actions per swap.
     */
    protected int SWAP_THRESHOLD;
    /**
     * The number of user actions performed for this panel. Equal to the number of guesses in GuessPanel or the number of reveals in RevealPanel.
     */
    protected int actionCount;
    /**
     * The word to be guessed
     */
    protected String targetWord;
    /**
     * If only one panel can be accessed at a time i.e. use the swap threshold
     */
    protected boolean doSwapThreshold;

    public InteractivePanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold){
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;

        this.actionCount = 0;
    }

    public InteractivePanel(LayoutManager layout, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold){
        super(layout);
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;

        this.actionCount = 0;
    }

    /**
     * Sets up the components in the panel
     */
    public abstract void setupContentArea();

    /**
     * Check if the actionCount meets the ratio. If so call doSwap.
     * @return If enough actions have been performed and it is time to swap to the other panel
     */
    public boolean swap(){
        if(!doSwapThreshold){
            return false;
        }

        // If the swap threshold has been met, deactive this panel
        if(actionCount%SWAP_THRESHOLD == 0){
            deactivatePanel();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Disable every interactable component in this panel
     */
    public abstract void deactivatePanel();
}
