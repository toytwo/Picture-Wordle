import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 * @author Jackson Alexman
 * @version Updated: 4/08/2024
 */
public class RevealByColor extends RevealPanel{

    private int NUMBER_OF_COLORS;

    private JButton[] colorSelectors;

    public RevealByColor(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int NUMBER_OF_COLORS){
        super(image, targetWord, SWAP_THRESHOLD, doSwapThreshold);
        this.NUMBER_OF_COLORS = NUMBER_OF_COLORS;
    }

    @Override
    public void setupContentArea(){
        // Draw the image
        super.setupContentArea();

        this.colorSelectors = new JButton[NUMBER_OF_COLORS];
        for(int i=0; i<this.colorSelectors.length; i++){
            this.colorSelectors[i] = new JButton();

            // Distributes the colors across the color spectrum as equally as possible
            double offset = i*255/NUMBER_OF_COLORS;
            double r = 127.5*Math.sin(offset+(2*Math.PI/3))+127.5;
            double g = 127.5*Math.sin(offset+(2*2*Math.PI/3))+127.5;
            double b = 127.5*Math.sin(offset+(2*Math.PI))+127.5;

            this.colorSelectors[i].setBackground(new Color((int)r,(int)g,(int)b));
            this.add(this.colorSelectors[i]);
        }
    }

    @Override
    public void deactivatePanel() {
        // TODO Implement Method
    }
}
