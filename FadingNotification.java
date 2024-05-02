import javax.swing.JDialog;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public abstract class FadingNotification extends JDialog {
    /**
     * The current opacity of the JDialogue
     */
    private float opacity = 0.0f;
    /**
     * True if there is a delay between fading in and fading out. False otherwise.
     */
    private boolean doFadeOutDelay;
    /**
     * True if fadeIn doesn't call fade out. False otherwise. 
     */
    private boolean doManualFadeOut;
    protected int FADE_OUT_DELAY_MILLISECONDS = 3000;
    protected int FADE_IN_DURATION_MILLISECONDS = 300;
    protected int FADE_OUT_DURATION_MILLISECONDS = 300;

    public FadingNotification(boolean doFadeOutDelay, boolean doManualFadeOut) {
        super(Game.getCurrentGame(), ModalityType.MODELESS);

        // Default JDialog Customization
        setUndecorated(true); // No bar at top

        // Initialize instance variables
        this.doFadeOutDelay = doFadeOutDelay;
        this.doManualFadeOut = doManualFadeOut;

        if(this.doFadeOutDelay == true && this.doManualFadeOut == true){
            System.err.println("doFadeOutDelay and doManualFadeOut are mutually exclusive and cannot both be true");
            System.exit(0);
        }

        // Begin Fading In
        fadeIn();
    }

    /**
     * Setup the components within the JDialog
     */
    public abstract void setupContentArea();

    protected void fadeOut(){
        Timer fadeOutTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.1f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f; // Cap at 0
                    ((Timer) e.getSource()).stop();
                    dispose(); // Close the popup after fading out
                }
                setOpacity(opacity);
            }
        });
        fadeOutTimer.start();
    }

    protected void fadeIn(){
        Timer fadeInTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.02f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f; // Cap at 1
                    ((Timer) e.getSource()).stop();

                    // Start fade out after a delay
                    if(doFadeOutDelay){
                        fadeOutDelay();
                    }
                    // Let fade out be called manually
                    else if(doManualFadeOut){
                        /* Do Nothing */
                    }
                    // Start fade out immediately
                    else{
                        fadeOut();
                    }
                }

                setOpacity(opacity);
            }
        });
        fadeInTimer.start();
    }

    private void fadeOutDelay(){
        Timer delayTimer = new Timer(FADE_OUT_DELAY_MILLISECONDS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fadeOut();
            }
        });
        delayTimer.setRepeats(false); // Only activates once
        delayTimer.start();
    }
}