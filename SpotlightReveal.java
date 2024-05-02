import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @version Integrated into program by Jackson on 4/24/2024. Updated: 4/30/2024 (Jackson)
 */
public class SpotlightReveal extends RevealPanel {
    private int radius;
    private int x, y;
    private Ellipse2D oval;
    private Area[] revealedAreas;
    /**
     * Whether or not the cursor is on the panel
     */
    private boolean focused;

    /**
     * @param n
     * @param i
     * @param b
     * @param g
     * @param MAX_REVEALS The maximum number of times that the player can reveal part of the image
     */
    public SpotlightReveal(String n, int i, boolean b, BufferedImage g, int MAX_REVEALS, int REVEAL_COST) {
        super(n,i,b,g, MAX_REVEALS, REVEAL_COST, new GridBagLayout());
        radius = 70;
        x = -1;
        y = -1;
        revealedAreas = new Area[MAX_ACTIONS];
        this.isEnabled = true;
        this.focused = false;

        setupContentArea();
    }   

    @Override
    public void resetInstanceVariables() {
        revealedAreas = new Area[MAX_ACTIONS];
    }

    @Override
    public void resetContentArea() {
        repaint();
    }

    @Override
    public void setupContentArea() {
        // Create a panel to hold the image
        JPanel imagePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                g.drawImage(image, 0, 0, this);

                if(revealedAreas == null){
                    // Don't hide the image
                    return;
                }

                // Subtract revealed areas from the black screen
                Area mask = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
                for (int i = 0; i < interactionCount; i++) {
                    if(revealedAreas[i] == null){
                        continue;
                    }

                    mask.subtract(revealedAreas[i]);
                }

                // Create a buffered image for the mask
                BufferedImage maskImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D maskGraphics = maskImage.createGraphics();
                maskGraphics.setColor(Color.BLACK);
                maskGraphics.fill(mask);
                maskGraphics.dispose();

                // Draw the mask onto the panel
                g.drawImage(maskImage, 0, 0, this);

                // Draw the oval representing the spotlight
                if (oval != null && isEnabled && focused) {
                    Graphics2D g2d2 = (Graphics2D) g.create();
                    g2d2.setColor(Color.WHITE);// Color of Oval
                    g2d2.draw(oval);
                    g2d2.fill(oval);
                    g2d2.setColor(Color.BLACK);//Color of Reveal text
                    g2d2.draw(oval);//Draws string onto oval
                    g2d2.setFont(new Font("SansSerif", Font.BOLD, 19));
                    g2d2.drawString("Reveal", x - 29, y);
                    g2d2.dispose();
                }
            }
            
            {
            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (oval != null && isEnabled && focused) {
                        revealedAreas[interactionCount] = new Area(oval); // Add the clicked oval to the list of revealed areas
                        interactionPerformed(true);
                        repaint();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {/* Do Nothing */}

                @Override
                public void mouseReleased(MouseEvent e) {/* Do Nothing */}

                @Override
                public void mouseEntered(MouseEvent e) {
                    if(isEnabled){
                        // Draw the reveal oval if the mouse is in the panel
                        focused = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if(isEnabled){
                        // Dont draw the reveal oval if the mouse isn't in the panel
                        focused = false;
                        repaint();
                    }
                }
               
            });

            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {/* Do Nothing */}

                public void mouseMoved(MouseEvent e) {
                    if(isEnabled && focused){
                        x = e.getX();
                        y = e.getY();
                        oval = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
                    }
                    // Repaint regardless to properly update visuals
                    repaint();
                }
            });
            }
        };

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Resize the image to fit the panel
                BufferedImage resizedImage = new BufferedImage(getHeight(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(image, 0, 0, getHeight(), getHeight(), null);
                graphics2D.dispose();
                image = resizedImage;

                // Resize the imagePanel to fit the image
                imagePanel.setPreferredSize(new Dimension(getHeight(),getHeight()));
                imagePanel.setMaximumSize(new Dimension(getHeight(),getHeight()));
                imagePanel.revalidate();
                imagePanel.repaint();

                // Remove the listener so it is only called once
                removeComponentListener(getComponentListeners()[0]);
            }
        });

        // Add an empty panel to the right for spacing
        JPanel emptyPanel = new JPanel();
        this.add(emptyPanel);

        // Add the image panel
        this.add(imagePanel);

        // Add an empty panel to the right for spacing
        emptyPanel = new JPanel();
        this.add(emptyPanel);
    }

    @Override
    public void revealEntireImage() {
        this.revealedAreas = null;
        repaint();
    }
}
