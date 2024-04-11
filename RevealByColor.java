import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Jackson Alexman
 * @version Updated: 4/09/2024
 */
public class RevealByColor extends RevealPanel{

    private int NUMBER_OF_BUTTONS;
    private JButton[] colorSelectors;
    private boolean[] activeColorRanges;
    private float[] hueRanges;
    private BufferedImage imageCopy;

    public RevealByColor(BufferedImage rawImage, String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int NUMBER_OF_BUTTONS){
        super(targetWord, SWAP_THRESHOLD, doSwapThreshold, rawImage, new GridBagLayout());
        this.NUMBER_OF_BUTTONS = NUMBER_OF_BUTTONS;
        this.activeColorRanges = new boolean[NUMBER_OF_BUTTONS];
        this.hueRanges = new float[NUMBER_OF_BUTTONS + 1];
        this.REVEAL_PANEL_SCREEN_PERCENTAGE = 3.0 / 4.0;
        this.imageCopy = rawImage;
    }

    @Override
    public void setupContentArea(){
        JPanel imagePanel = new JPanel(){
            // Override the paintComponent to draw the image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (image != null) {
                    // Draw the scaled image with the height matching the panel height
                    g.drawImage(image, 0, 0, this);
                }
            }
        };

        JPanel buttonPanel = setupButtonPanel(imagePanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.gridy = 0;

        constraints.gridx = 0;
        constraints.weightx = 1.0/6.0;
        add(new JPanel());

        constraints.gridx = 1;
        constraints.weightx = 2.0/3.0;
        add(imagePanel);

        constraints.gridx = 2;
        constraints.weightx = 2.0/3.0;
        add(buttonPanel);

        constraints.gridx = 3;
        constraints.weightx = 1.0/6.0;
        add(new JPanel());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Resize the image to fit the panel
                BufferedImage resizedImage = new BufferedImage(getHeight(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(image, 0, 0, getHeight(), getHeight(), null);
                graphics2D.dispose();
                imageCopy = resizedImage;

                // Resize the imagePanel to fit the image
                imagePanel.setPreferredSize(new Dimension(getHeight(),getHeight()));
                imagePanel.repaint();

                // Resize the buttonPanel to fit the height of imagePanel
                buttonPanel.setPreferredSize(new Dimension(buttonPanel.getWidth(),getHeight()));
                buttonPanel.revalidate();
                buttonPanel.repaint();

                // Cover the image
                reveal();

                // Only activate once
                removeComponentListener(getComponentListeners()[0]);
            }
        });
        
        
    }

    private JPanel setupButtonPanel(JPanel imagePanel){
        JPanel buttonPanel = new JPanel();

        // Place the buttons in a column
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.weightx = 1;
        buttonPanelConstraints.weighty = 1;

        // Setup the buttons
        colorSelectors = new JButton[NUMBER_OF_BUTTONS];
        for(int i=0; i<NUMBER_OF_BUTTONS; i++){
            colorSelectors[i] = new JButton();

            // Make the first and last button black and white
            if(i == 0){
                colorSelectors[i].setBackground(Color.WHITE);
                hueRanges[i] = (float) (i) / (NUMBER_OF_BUTTONS);
            }
            else if(i == colorSelectors.length-1){
                colorSelectors[i].setBackground(Color.BLACK);
                hueRanges[i] = 1.0f; // Set the upper bound of the last range to 1.0
            }
            // Make all the other buttons colorful
            else{
                // Distributes the colors across the color spectrum as equally as possible
                hueRanges[i] = (float) (i) / (NUMBER_OF_BUTTONS);

                colorSelectors[i].setBackground(Color.getHSBColor(hueRanges[i], 1.0f, 1.0f));
            }

            // Have to use an effectively final variable inside the override
            int index = i;

            // Add the actionlistener
            colorSelectors[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    activeColorRanges[index] = true;
                    reveal();
                }
            });

            buttonPanelConstraints.gridy = i;
            buttonPanel.add(colorSelectors[i], buttonPanelConstraints);
        }

        return buttonPanel;
    }

    private void reveal() {
        // Reset the image
        image = copyImage(imageCopy);

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                float hue = hsb[0];
                // Check if the color is in any of the active ranges
                boolean isInActiveRanges = false;
                int index = -1; // We increment at the start of the for loop so the first index is actually 0
                for(boolean activeRange : activeColorRanges){
                    index++;

                    // Skip inactive ranges
                    if(!activeRange){
                        continue;
                    }

                    isInActiveRanges = hue >= hueRanges[index] && hue < hueRanges[index + 1];
                
                    if (isInActiveRanges) {
                        break;

                    } else {
                        continue;
                    }
                }

                if (isInActiveRanges) {
                    // If the pixel falls within the any of the active color ranges, uncover it by setting alpha to fully opaque
                    image.setRGB(x, y, new Color(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), 255).getRGB());

                } else {
                    // Otherwise, set the pixel to black
                    image.setRGB(x, y, Color.TRANSLUCENT);
                }
            }
            
            repaint();
        }
    }

    private BufferedImage copyImage(BufferedImage originalImage){
        // Create a new BufferedImage with the same dimensions and type as the original
        BufferedImage copy = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        
        // Create a Graphics2D object to draw on the new image
        Graphics2D g2d = copy.createGraphics();
        
        // Draw the original image onto the new one
        g2d.drawImage(originalImage, 0, 0, null);
        
        // Dispose the Graphics2D object
        g2d.dispose();

        return copy;
    }
    @Override
    public void deactivatePanel() {
        // TODO Implement Method
    }
}
