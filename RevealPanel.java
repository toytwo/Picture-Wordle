import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

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
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            // Calculate the desired width to cover 2/3 of the available width
            int desiredWidth = (int) (panelWidth * 2.0 / 3.0);
            
            // Calculate the position to center the image horizontally
            int x = (panelWidth - desiredWidth) / 2;
            
            // Draw the scaled image with the height matching the panel height
            g.drawImage(image, x, 0, desiredWidth, panelHeight, this);
        }
    }

    public void setupContentArea(){

    }
}
