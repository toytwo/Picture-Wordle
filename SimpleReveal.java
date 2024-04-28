import java.awt.Graphics;
import java.awt.image.BufferedImage;
/**
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
 */
/**
 * The SimpleReveal class represents a panel where an image is revealed without
 * any interactive elements.
 * It extends the RevealPanel class.
 */
public class SimpleReveal extends RevealPanel {
<<<<<<< Updated upstream
    public SimpleReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_REVEALS){
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, image, MAX_REVEALS);
=======
    /**
     * Constructor to initialize a SimpleReveal object with the specified image,
     * target word, swap threshold,
     * and swap threshold functionality flag.
     * 
     * @param image           The image to be revealed.
     * @param targetWord      The word associated with the image.
     * @param SWAP_THRESHOLD  The number of reveals before swapping to guessing.
     * @param doSwapThreshold Whether or not the user swaps between guessing and
     *                        revealing.
     */
    public SimpleReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold) {
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, image);
>>>>>>> Stashed changes
    }

    /**
     * Override method to paint the image component onto the panel.
     * 
     * @param g The Graphics object used for painting.
     */
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

    /**
     * Override method to set up the content area of the panel.
     * This method does nothing in SimpleReveal as there are no interactive elements
     * to set up.
     */
    @Override
<<<<<<< Updated upstream
    public void setupContentArea() {/* Do Nothing */}
=======
    public void setupContentArea() {
        /* Do Nothing */
    }

    /**
     * Override method to set the panel enabled or disabled.
     * This method does nothing in SimpleReveal as there are no interactive elements
     * to enable or disable.
     * 
     * @param isEnabled True to enable the panel, false to disable.
     */
    @Override
    public void setPanelEnabled(boolean isEnabled) {
        /* Do Nothing */
    }
>>>>>>> Stashed changes

    /**
     * Override method to subtract points.
     * This method does nothing in SimpleReveal as there are no points to subtract.
     */
    @Override
<<<<<<< Updated upstream
    public void setPanelEnabled(boolean isEnabled) {/* Do Nothing */}
=======
    public void subtractPoint() {
        // Do nothing
        // Prevent negative interactionCount
    }
>>>>>>> Stashed changes
}
// Path: RevealPanel.java