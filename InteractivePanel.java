import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
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
    /**
     * How much score is lost for an interaction
     */
    protected int ACTION_COST;
    /**
     * True if the panel is enabled. False otherwise.
     */
    protected boolean isEnabled;
    /**
     * True if points are being tracked. False otherwise.
     */
    protected boolean pointsEnabled;

    public InteractivePanel(String targetWord, int SWAP_THRESHOLD, int MAX_ACTIONS, int ACTION_COST, boolean pointsEnabled){
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = true;
        this.MAX_ACTIONS = MAX_ACTIONS;
        this.interactionCount = 0;
        this.ACTION_COST = ACTION_COST;
        this.pointsEnabled = pointsEnabled;
    }

    public InteractivePanel(LayoutManager layout, String targetWord, int SWAP_THRESHOLD, int MAX_ACTIONS, int ACTION_COST, boolean pointsEnabled){
        super(layout);
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = true;
        this.MAX_ACTIONS = MAX_ACTIONS;
        this.interactionCount = 0;
        this.ACTION_COST = ACTION_COST;
        this.pointsEnabled = pointsEnabled;
    }

    /**
     * Resets the instance variables of the interactive panel. DO NOT CALL THIS METHOD FROM GAME.
     * @param newTargetWord
     */
    protected void resetPanel(String newTargetWord){
        this.targetWord = newTargetWord;
        this.interactionCount = 0;
        this.isEnabled = true;

        resetInstanceVariables();
        resetContentArea();
    }

    /**
     * Resets instance variables specific to the panel.
     */
    public abstract void resetInstanceVariables();
    /**
     * Resets any components in the panel.
     */
    public abstract void resetContentArea();

    /**
     * Sets up the components in the panel.
     */
    protected abstract void setupContentArea();

    /**
     * Sends a notification to the player based on the number of guesses or reveals remaining
     * @param isSwapping True if it is time to swap to the other panel. False otherwise.
     */
    final public void sendNotification(Boolean isSwapping) {
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
     * @param doSubtractPoints If points should be subtracted for this action
     * @return If it's time to swap
     */
    protected boolean interactionPerformed(boolean doSubtractPoints){
        boolean doSwap = false;

        this.interactionCount++; // Increment the number of actions performed

        if(doSwapThreshold){
            doSwap = this.swap(); // Check if it's time to swap
            this.sendNotification(doSwap); // Show the player the next action they should perform
        }

        if(this.pointsEnabled && doSubtractPoints){
            this.subtractPointCost();
        }

        return doSwap;
    }

    /**
     * Subtract the ACTION_COST from the image score
     */
    final protected void subtractPointCost(){
        Game.getCurrentGame().getScorePanel().updateImageScore(-ACTION_COST);
    }

    /**
     * Check if the actionCount meets the swapThreshold. If so, swap panels.
     * @return If enough actions have been performed and it is time to swap to the other panel
     */
    final private boolean swap(){
        // If the swap threshold has been met, deactivate this panel and activate otherPanel
        if(interactionCount%SWAP_THRESHOLD == 0){
            this.setPanelEnabled(false);
            otherPanel.setPanelEnabled(true);
            Game.getCurrentGame().getSkipActionPanel().setEnabledPanel(otherPanel);
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
    public void setOtherPanel(InteractivePanel otherPanel){
        this.otherPanel = otherPanel;
    }

    /**
     * Enable or disable certain interactable components in this panel
     */
    public void setPanelEnabled(boolean isEnabled){
        this.isEnabled = isEnabled;
    }

    /**
     * Sets descriptors of the panel
     * @param panelName The name of the panel
     * @param panelPluralAction The plural form of the action of the panel
     */
    final public void setPanelDescriptors(String panelName, String panelPluralAction){
        this.panelName = panelName;
        this.panelPluralAction = panelPluralAction;
    }
}
