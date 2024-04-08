import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author Jackson Alexman
 * @version Updated: 4/07/2024
 */
public abstract class RevealPanel extends InteractivePanel{
    protected BufferedImage image;
    protected int width;
    protected int height;

    public RevealPanel(BufferedImage image, String targetWord, int swapThreshold, boolean doSwapThreshold){
        super(new GridBagLayout(), targetWord, swapThreshold, doSwapThreshold);
        this.image = image;
    }

    @Override
    public void setupContentArea() {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        this.add(label);
        
    }
}
