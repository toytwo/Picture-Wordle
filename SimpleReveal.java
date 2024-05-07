import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

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
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Resize the image to fit the panel
                image = resizeImage(image);

                // Resize the imagePanel to fit the image
                setPreferredSize(new Dimension(getHeight(),getHeight()));
                setMaximumSize(new Dimension(getHeight(),getHeight()));
                revalidate();
                repaint();

                // Remove the listener so it is only called once
                removeComponentListener(getComponentListeners()[0]);
            }
        });
        
    }

    @Override
    public void revealEntireImage() {/* Do Nothing */}

    @Override
    public void resetInstanceVariables() {/* Do Nothing */}

    @Override
    public void resetContentArea() {/* Do Nothing */}
}
