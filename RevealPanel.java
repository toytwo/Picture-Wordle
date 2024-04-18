import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
public abstract class RevealPanel extends InteractivePanel{
    /**
     * The image to be guessed.
     */
    protected BufferedImage image;
    /**
     * How much of the screen width the revealPanel occupies. guessPanel will occupy the other part (1-this).
     */
    protected double REVEAL_PANEL_SCREEN_PERCENTAGE = 5.0/6.0;

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, LayoutManager layout){
        super(layout, targetWord, swapThreshold, doSwapThreshold);
        this.image = image;
    }

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image){
        super(targetWord, swapThreshold, doSwapThreshold);
        this.image = image;
    }

    public double getREVEAL_PANEL_SCREEN_PERCENTAGE(){
        return REVEAL_PANEL_SCREEN_PERCENTAGE;
    }
}
