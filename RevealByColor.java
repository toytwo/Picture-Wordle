import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 * @author Jackson Alexman
 * @version Updated: 4/09/2024
 */
public class RevealByColor extends RevealPanel{

    private int NUMBER_OF_COLORS;
    private JButton[] colorSelectors;

    public RevealByColor(BufferedImage image, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int NUMBER_OF_COLORS){
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, image, new GridLayout());
        this.NUMBER_OF_COLORS = NUMBER_OF_COLORS;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void setupContentArea(){
        this.add(new SimpleReveal(image, targetWord, SWAP_THRESHOLD, doSwapThreshold));

        this.colorSelectors = new JButton[NUMBER_OF_COLORS];
        for(int i=0; i<this.colorSelectors.length; i++){
            this.colorSelectors[i] = new JButton();

            // Distributes the colors across the color spectrum as equally as possible
            double offset = (double) i * 255.0 / NUMBER_OF_COLORS;
            double r = 127.5 * Math.sin(offset + (2 * Math.PI / 3)) + 127.5;
            double g = 127.5 * Math.sin(offset + (2 * 2 * Math.PI / 3)) + 127.5;
            double b = 127.5 * Math.sin(offset + (2 * Math.PI)) + 127.5;

            this.colorSelectors[i].setBackground(new Color((int) r,(int) g,(int) b));
            this.add(this.colorSelectors[i]);
        }
    }

    @Override
    public void deactivatePanel() {
        // TODO Implement Method
    }
}
