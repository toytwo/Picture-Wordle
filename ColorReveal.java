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
import javax.swing.Timer;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class ColorReveal extends RevealPanel{

    /**
     * The number of reveal buttons. Must be greater than zero.
     */
    private int NUMBER_OF_BUTTONS;
    /**
     * The reveal buttons.
     */
    private JButton[] colorSelectors;
    /**
     * Which hue ranges are being displayed in the image
     */
    private boolean[] activeHueRanges;
    /**
     * Ranges of color. Values from 0.0f to 1.0f.
     */
    private float[] hueRanges;
    /**
     * A resized copy of the image.
     */
    private BufferedImage imageCopy;
    /**
     * A JPanel containing all the reveal buttons.
     */
    private JPanel buttonPanel;
    /**
     * A JPanel containing the image.
     */
    private JPanel imagePanel;

    /**
     * @param image The image to be revealed
     * @param targetWord The word to be guessed
     * @param SWAP_THRESHOLD How many reveals before swapping to guessing
     * @param doSwapThreshold Whether or not the user swaps between guessing and revealing
     * @param NUMBER_OF_BUTTONS The number of reveal buttons
     */
    public ColorReveal(BufferedImage image, String targetWord, int SWAP_THRESHOLD, int MAX_REVEALS, int revealCost, int NUMBER_OF_BUTTONS, boolean pointsEnabled){
        super(targetWord, SWAP_THRESHOLD, image, MAX_REVEALS, revealCost, new GridBagLayout(), pointsEnabled);

        this.NUMBER_OF_BUTTONS = NUMBER_OF_BUTTONS;
        this.activeHueRanges = new boolean[NUMBER_OF_BUTTONS];
        this.hueRanges = new float[NUMBER_OF_BUTTONS + 1];
        this.REVEAL_PANEL_SCREEN_PERCENTAGE = 3.0 / 4.0;
        this.imageCopy = image;

        setupContentArea();
    }

    @Override
    public void resetInstanceVariables() {
        this.imageCopy = copyImage(image);
        this.activeHueRanges = new boolean[NUMBER_OF_BUTTONS];
    }

    @Override
    public void resetContentArea() {
        // Cover the image
        reveal();

        // Reset the colorSelector buttons
        for(int i=0; i<NUMBER_OF_BUTTONS; i++){
            colorSelectors[i].setEnabled(true);

            // Make the first and last button black and white
            if(i == 0){
                colorSelectors[i].setBackground(Color.WHITE);
            }
            else if(i == colorSelectors.length-1){
                colorSelectors[i].setBackground(Color.BLACK);
            }
            // Make all the other buttons colorful
            else{
                colorSelectors[i].setBackground(Color.getHSBColor(hueRanges[i], 1.0f, 1.0f));
            }
        }

        repaint();
    }
    
    @Override
    public void setupContentArea(){
        // Set up both panels

        imagePanel = new JPanel(){
            // Override the paintComponent to draw the image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                    
                // Draw the image
                if (image != null) {
                    g.drawImage(image, 0, 0, this);
                }
            }
        };

        setupButtonPanel(imagePanel);

        // Setup the constraints for the RevealByColor panel
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.gridy = 0;

        // Add an empty panel to the left side for spacing
        constraints.gridx = 0;
        constraints.weightx = 1.0/8.0;
        add(new JPanel(), constraints);

        // Add the image panel
        constraints.gridx = 1;
        constraints.weightx = 7.0/12.0;
        add(imagePanel);

        // Add the button panel
        constraints.gridx = 2;
        constraints.weightx = 1.0/6.0;
        add(buttonPanel);

        // Add an empty panel to the right side for spacing
        constraints.gridx = 3;
        constraints.weightx = 1.0/8.0;
        add(new JPanel(),constraints);

        delayedResize(null); // Parameter not used in the overrided version   
    }

    private void setupButtonPanel(JPanel imagePanel){
        buttonPanel = new JPanel();

        // Place the buttons in a column
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.weightx = 1;
        buttonPanelConstraints.weighty = 1;
        buttonPanelConstraints.anchor = GridBagConstraints.NORTH;

        // Setup the buttons
        colorSelectors = new JButton[NUMBER_OF_BUTTONS];
        for(int i=0; i<NUMBER_OF_BUTTONS; i++){
            // Create the button
            colorSelectors[i] = new JButton();

            // Distributes the colors across the color spectrum as equally as possible
            hueRanges[i] = (float) (i) / (NUMBER_OF_BUTTONS);

            // Make the first and last button black and white
            if(i == 0){
                colorSelectors[i].setBackground(Color.WHITE);
            }
            else if(i == colorSelectors.length-1){
                colorSelectors[i].setBackground(Color.BLACK);
                hueRanges[i+1] = 1.0f; // Set the upper bound of the last range to 1.0
            }
            // Make all the other buttons colorful
            else{
                colorSelectors[i].setBackground(Color.getHSBColor(hueRanges[i], 1.0f, 1.0f));
            }

            // Have to use an effectively final variable inside the override
            int index = i;

            // Add the actionlistener to the button
            colorSelectors[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Activate the corresponding hue range
                    activeHueRanges[index] = true;
                    // Update the image
                    reveal();
                    // An interaction has been performed
                    interactionPerformed(true);
                    // Disable the button
                    colorSelectors[index].setEnabled(false);
                    // Change the color to indicate that it's disabled
                    colorSelectors[index].setBackground(Color.gray);
                }
            });

            // Add the button to the panel
            buttonPanelConstraints.gridy = i;
            buttonPanel.add(colorSelectors[i], buttonPanelConstraints);
        }
    }

    /**
     * Update the image based on the active hue ranges
     */
    private void reveal() {
        // Reset the image
        image = copyImage(imageCopy);

        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate through each pixel and activate it if it is in one of the active hue ranges
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                float hue = hsb[0];
                // Check if the color is in any of the active ranges
                boolean isInActiveRanges = false;
                for(int i = 0; i < activeHueRanges.length; i++){

                    // Skip inactive ranges
                    if(!activeHueRanges[i]){
                        continue;
                    }
                    
                    // If it's in one of the ranges, no need to check the others
                    if (isInActiveRanges = hue >= hueRanges[i] && hue < hueRanges[i + 1]) {
                        break;

                    }
                }

                // If the pixel falls within the any of the active color ranges, uncover it by setting alpha to fully opaque
                if (isInActiveRanges) {
                    image.setRGB(x, y, new Color(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), 255).getRGB());

                } 
                // Otherwise, set the pixel to translucent
                else {
                    image.setRGB(x, y, Color.TRANSLUCENT);
                }
            }
            
            // Update the image
            repaint();
        }
    }

    /**
     * @param imageToCopy
     * @return A copy of originalImage
     */
    private BufferedImage copyImage(BufferedImage imageToCopy){
        // Create a new BufferedImage with the same dimensions and type as the original
        BufferedImage copy = new BufferedImage(imageToCopy.getWidth(), imageToCopy.getHeight(), imageToCopy.getType());
        
        // Create a Graphics2D object to draw on the new image
        Graphics2D g2d = copy.createGraphics();
        
        // Draw the original image onto the new one
        g2d.drawImage(imageToCopy, 0, 0, null);
        
        // Dispose the Graphics2D object
        g2d.dispose();

        return copy;
    }

    @Override
    public void setPanelEnabled(boolean isEnabled) {
        // Enable the panel
        if(isEnabled){
            for (int i = 0; i < colorSelectors.length; i++) {
                // If the hue range is active, the button has been pressed and therefore doesn't need to be reenabled
                colorSelectors[i].setEnabled(!activeHueRanges[i]);
            }
        }
        // Disable the panel
        else{
            for (JButton selector : colorSelectors){
                selector.setEnabled(false);
            }
        }
    }

    @Override
    public void revealEntireImage() {
        this.image = imageCopy;
        repaint();
    }

    @Override
    protected void delayedResize(JPanel panel) {
        // Use a delay to get the actual height instead of 0
        Timer getHeightDelayTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageCopy = resizeImage(image);

                // Cover the image
                reveal();

                // Resize the imagePanel to fit the image
                imagePanel.setPreferredSize(new Dimension(getHeight(),getHeight()));
                imagePanel.setMinimumSize(new Dimension(getHeight(),getHeight()));
                imagePanel.revalidate();
                imagePanel.repaint();

                // Resize the buttonPanel to fit the height of imagePanel
                buttonPanel.setPreferredSize(new Dimension(getHeight()/16,getHeight()));
                buttonPanel.setMaximumSize(new Dimension(getHeight()/16,getHeight()));
                buttonPanel.revalidate();
                buttonPanel.repaint();
            }
        }); 
        getHeightDelayTimer.setRepeats(false);
        getHeightDelayTimer.start();
    }
}
