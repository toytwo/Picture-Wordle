import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
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
    /**
     * The name of the panel simplified to a string. Used for displaying to the player.
     */
    protected String panelName;
    /**
     * The plural form of the panel's action. Used for displaying to the player.
     */
    protected String panelPluralAction;

    public InteractivePanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_ACTIONS){
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;
        this.MAX_ACTIONS = MAX_ACTIONS;
        this.interactionCount = 0;
    }

    public InteractivePanel(LayoutManager layout, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_ACTIONS){
        super(layout);
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;
        this.MAX_ACTIONS = MAX_ACTIONS;
        this.interactionCount = 0;
    }

    /**
     * Sets up the components in the panel.
     */
    public abstract void setupContentArea();

    /**
     * Sends a notification to the player based on the number of guesses or reveals remaining
     * @param isSwapping True if it is time to swap to the other panel. False otherwise.
     */
    public void sendNotification(Boolean isSwapping) {
        int ACTIONS_REMAINING;
        String ACTION_NAME;
        InteractivePanel PANEL;

        // Time to swap
        if(isSwapping){
            PANEL = this.otherPanel;
            ACTIONS_REMAINING = this.otherPanel.SWAP_THRESHOLD;
        }
        // Keep Revealing
        else{
            PANEL = this;
            ACTIONS_REMAINING = this.SWAP_THRESHOLD-(interactionCount%this.SWAP_THRESHOLD);
        }

        // Singular
        if(ACTIONS_REMAINING == 1){
            ACTION_NAME = PANEL.panelName;
        }
        // Plural
        else{
            ACTION_NAME = PANEL.panelPluralAction;
        }

        // Notify the player of their next action
        (new ActionNotification(PANEL.panelName, ACTIONS_REMAINING+" "+ACTION_NAME+" Remaining")).setVisible(true); // Set visible here because it freezes when doing it in FadingNotification
    }

    /**
     * Called when an action is performed in this panel.
     * @return If it's time to swap
     */
    public boolean interactionPerformed(){
        this.interactionCount++; // Increment the number of actions performed
        Boolean isSwapping = this.swap(); // Check if it's time to swap
        this.sendNotification(isSwapping); // Send a notif
        return isSwapping;
    }

    /**
     * Check if the actionCount meets the swapThreshold. If so, swap panels.
     * @return If enough actions have been performed and it is time to swap to the other panel
     */
    final public boolean swap(){
        if(!doSwapThreshold){
            return false;
        }

        // If the swap threshold has been met, deactivate this panel and activate otherPanel
        if(interactionCount%SWAP_THRESHOLD == 0){
            this.setPanelEnabled(false);
            otherPanel.setPanelEnabled(true);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets the instance variable otherPanel
     * @param otherPanel the other interactive panel in Game
     */
    final public void setOtherPanel(InteractivePanel otherPanel){
        this.otherPanel = otherPanel;
    }

    /**
     * Enable or disable certain interactable components in this panel
     */
    public abstract void setPanelEnabled(boolean isEnabled);

    /**
     * Sets descriptors of the panel
     * @param panelName The name of the panel
     * @param panelPluralAction The plural form of the action of the panel
     */
    public void setPanelDescriptors(String panelName, String panelPluralAction){
        this.panelName = panelName;
        this.panelPluralAction = panelPluralAction;
    }
}