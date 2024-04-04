import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated 4/4/2024
 */

public class RevealPanel extends JPanel{
    protected BufferedImage image;
    protected int width;
    protected int height;

    public RevealPanel(BufferedImage image){
        this.image = image;
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

    public void setupContentArea(){

    }
}
