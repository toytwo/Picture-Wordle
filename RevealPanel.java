import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

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
    protected double REVEAL_PANEL_SCREEN_PERCENTAGE = 2.0/3.0;

    public RevealPanel(String targetWord, int swapThreshold, BufferedImage image, int MAX_REVEALS, int revealCost, LayoutManager layout, boolean pointsEnabled){
        super(layout, targetWord, swapThreshold, MAX_REVEALS, revealCost, pointsEnabled);
        this.image = image;
        this.setPanelDescriptors("Reveal", "Reveals");
    }

    public RevealPanel(String targetWord, int swapThreshold, BufferedImage image, int MAX_REVEALS, int revealCost, boolean pointsEnabled){
        super(targetWord, swapThreshold, MAX_REVEALS, revealCost, pointsEnabled);
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
        this.image = resizeImage(newImage);
        
        super.resetPanel(newTargetWord);
    }

    /**
     * Resize the image to fit the panel
     * @param imageToResize The image to be resized
     * @return The resized image
     */
    protected BufferedImage resizeImage(BufferedImage imageToResize){
        BufferedImage resizedImage = new BufferedImage(getHeight(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(imageToResize, 0, 0, getHeight(), getHeight(), null);
        graphics2D.dispose();
        return resizedImage;
    }

    protected void delayedResize(JPanel panel){
        // Use a delay to get the actual height instead of 0
        Timer getHeightDelayTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Resize the image to fit the panel
                image = resizeImage(image);

                // Resize the imagePanel to fit the image
                panel.setPreferredSize(new Dimension(getHeight(),getHeight()));
                panel.setMaximumSize(new Dimension(getHeight(),getHeight()));
                panel.revalidate();
                panel.repaint();
            }
        }); 
        getHeightDelayTimer.setRepeats(false);
        getHeightDelayTimer.start();
    }
}
