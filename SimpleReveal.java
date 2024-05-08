import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

/**
 * @author Jackson Alexman
 * @version Updated: 5/6/2024
 */
public class SimpleReveal extends RevealPanel {
    public SimpleReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, int MAX_REVEALS, int REVEAL_COST, boolean pointsEnabled){
        super(targetWord, SWAP_THRESHOLD, image, MAX_REVEALS, REVEAL_COST, pointsEnabled);

        this.doSwapThreshold = false;

        setupContentArea();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Draw the scaled image with the height matching the panel height
            g.drawImage(image,0,0, this);
        }
    }

    @Override
    public void setupContentArea() {
        delayedResize(this);
    }

    @Override
    public void revealEntireImage() {/* Do Nothing */}

    @Override
    public void resetInstanceVariables() {/* Do Nothing */}

    @Override
    public void resetContentArea() {/* Do Nothing */}
}
