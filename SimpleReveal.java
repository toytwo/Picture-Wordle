import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class SimpleReveal extends RevealPanel {
    public SimpleReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_REVEALS, int REVEAL_COST, boolean pointsEnabled){
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, image, MAX_REVEALS, REVEAL_COST, pointsEnabled);

        setupContentArea();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Calculate the position to center the image horizontally
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int x = (panelWidth-panelHeight) / 2;
            
            // Draw the scaled image with the height matching the panel height
            g.drawImage(image, x, 0, panelHeight, panelHeight, this);
        }
    }

    @Override
    public void setupContentArea() {/* Do Nothing */}

    @Override
    public void revealEntireImage() {/* Do Nothing */}

    @Override
    public void resetInstanceVariables() {/* Do Nothing */}

    @Override
    public void resetContentArea() {/* Do Nothing */}
}
