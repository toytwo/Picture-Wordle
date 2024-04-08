import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 4/07/2024
 */
public class SimpleReveal extends RevealPanel {
    public SimpleReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold){
        super(image, targetWord, SWAP_THRESHOLD, doSwapThreshold);
    }

    @Override
    public void setupContentArea() {/* Do Nothing */}

    @Override
    public void deactivatePanel() {/* Do Nothing */}
}
