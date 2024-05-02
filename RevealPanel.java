import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
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

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, int MAX_REVEALS, int revealCost, LayoutManager layout, boolean pointsEnabled){
        super(layout, targetWord, swapThreshold, doSwapThreshold, MAX_REVEALS, revealCost, pointsEnabled);
        this.image = image;
        this.setPanelDescriptors("Reveal", "Reveals");
    }

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, int MAX_REVEALS, int revealCost, boolean pointsEnabled){
        super(targetWord, swapThreshold, doSwapThreshold, MAX_REVEALS, revealCost, pointsEnabled);
        this.image = image;
        this.setPanelDescriptors("Reveal", "Reveals");
    }

    public double getREVEAL_PANEL_SCREEN_PERCENTAGE(){
        return REVEAL_PANEL_SCREEN_PERCENTAGE;
    }

    public abstract void revealEntireImage();

    /**
     * Resets the components in and instance variables in the panel.
     * @param newImage
     * @param newTargetWord
     */
    public void resetPanel(String newTargetWord, BufferedImage newImage){
        this.image = newImage;
        
        super.resetPanel(newTargetWord);
    }
}
