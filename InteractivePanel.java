/**
 * The InteractivePanel class represents a panel that provides interactive functionalities in a game or application.
 * It includes functionality for tracking user interactions, swapping panels based on a threshold, and enabling/disabling interactive elements.
 * 
 * Instance Variables:
 * - SWAP_THRESHOLD: The threshold value for triggering a panel swap based on user interactions.
 * - interactionCount: The number of user interactions performed for this panel.
 * - MAX_ACTIONS: The maximum number of user actions that can be performed for this panel.
 * - targetWord: The word to be guessed or the target of the interactive actions.
 * - doSwapThreshold: A flag indicating whether to use the swap threshold functionality.
 * - otherPanel: A reference to another InteractivePanel that can be swapped with.
 * 
 * Methods:
 * - InteractivePanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold): Constructor to initialize a new InteractivePanel instance.
 * - setupContentArea(): Abstract method to set up the components in the panel's content area.
 * - interactionPerformed(): Method called when an action is performed in this panel, likely tracking user interactions.
 * - subtractPoint(): Abstract method to be overridden by subclasses for implementing logic related to deducting points.
 * - swap(): Method to check if the actionCount meets the swapThreshold and swap panels if necessary.
 * - setOtherPanel(InteractivePanel otherPanel): Method to set the reference to the other interactive panel in the game.
 * - setPanelEnabled(boolean isEnabled): Abstract method to enable or disable certain interactive components in this panel.
 * 
 * Usage:
 * - Create a subclass of InteractivePanel to implement specific interactive functionalities.
 * - Implement the abstract methods setupContentArea() and setPanelEnabled(boolean isEnabled) based on the panel's requirements.
 * - Use interactionPerformed() to track user interactions and update the panel accordingly.
 * - Use swap() to handle panel swapping based on user interactions.
 * 
 * Author: Jackson Alexman
 * Version: Updated: 4/17/2024
 */
import java.awt.LayoutManager;
import javax.swing.JPanel;

<<<<<<< Updated upstream
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
=======
>>>>>>> Stashed changes
public abstract class InteractivePanel extends JPanel {
    /** The threshold value for triggering a panel swap. */
    protected int SWAP_THRESHOLD;
    /** The number of user interactions performed for this panel. */
    protected int interactionCount;
    /** The word to be guessed or the target of the interactive actions. */
    protected String targetWord;
    /** A flag indicating whether to use the swap threshold functionality. */
    protected boolean doSwapThreshold;
    /** A reference to another InteractivePanel that can be swapped with. */
    protected InteractivePanel otherPanel;
    /**
     * The name of the panel simplified to a string. Used for displaying to the player.
     */
    protected String panelName;
    /**
     * The plural form of the panel's action. Used for displaying to the player.
     */
    protected String panelPluralAction;

<<<<<<< Updated upstream
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
=======
    /**
     * Constructor to initialize a new InteractivePanel instance.
     * 
     * @param targetWord       The word to be guessed or the target of the interactive actions.
     * @param SWAP_THRESHOLD   The threshold value for triggering a panel swap based on user interactions.
     * @param doSwapThreshold  A flag indicating whether to use the swap threshold functionality.
     */
    public InteractivePanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold) {
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;
>>>>>>> Stashed changes
        this.interactionCount = 0;
    }

    /**
     * Constructor to initialize a new InteractivePanel instance with a specified layout manager.
     * 
     * @param layout           The layout manager for the panel.
     * @param targetWord       The word to be guessed or the target of the interactive actions.
     * @param SWAP_THRESHOLD   The threshold value for triggering a panel swap based on user interactions.
     * @param doSwapThreshold  A flag indicating whether to use the swap threshold functionality.
     */
    public InteractivePanel(LayoutManager layout, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold) {
        super(layout);
        this.SWAP_THRESHOLD = SWAP_THRESHOLD;
        this.targetWord = targetWord;
        this.doSwapThreshold = doSwapThreshold;
        this.interactionCount = 0;
    }

    /**
     * Sets up the components in the panel's content area.
     */
    public abstract void setupContentArea();

    /**
<<<<<<< Updated upstream
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
        Boolean doSwap = this.swap(); // Check if it's time to swap
        this.sendNotification(doSwap); // Send a notif
        return doSwap;
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
=======
     * Method called when an action is performed in this panel, likely tracking user interactions.
     */
    public void interactionPerformed() {
        this.interactionCount++;
        swap();
    }

    /**
     * Abstract method to be overridden by subclasses for implementing logic related to deducting points.
     */
    public abstract void subtractPoint();

    /**
     * Method to check if the actionCount meets the swapThreshold and swap panels if necessary.
     * 
     * @return If enough actions have been performed and it is time to swap to the other panel.
     */
    final public boolean swap() {
        if (!doSwapThreshold) {
            return false;
        }
        if (interactionCount % SWAP_THRESHOLD == 0) {
>>>>>>> Stashed changes
            this.setPanelEnabled(false);
            otherPanel.setPanelEnabled(true);
            return true;
        }
        else{
            return false;
        }
    }

    /**
<<<<<<< Updated upstream
     * Sets the instance variable otherPanel
     * @param otherPanel the other interactive panel in Game
=======
     * Sets the reference to the other interactive panel in the game.
     * 
     * @param otherPanel The other interactive panel in Game.
>>>>>>> Stashed changes
     */
    final public void setOtherPanel(InteractivePanel otherPanel){
        this.otherPanel = otherPanel;
    }

    /**
     * Abstract method to enable or disable certain interactive components in this panel.
     * 
     * @param isEnabled True if the panel should be enabled; false otherwise.
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
