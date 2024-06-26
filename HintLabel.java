import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class HintLabel extends JLabel {
    private String hint;
    private int backgroundOpacity = 255;
    private int textOpacity = 255;
    private Color textColor = Color.WHITE;
    private static final int OPACITY_DECREMENT = 2;
    public static final int TEXT_SIZE = 20;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    /**
     * True if the label is being reset. False otherwise. Used to cancel any animations occuring when the reset is intitiated.
     */
    private Timer revealTimer;

    public HintLabel(String hint, int revealAt) {
        super("Reveals Automatically After Guess "+revealAt, SwingConstants.CENTER);
        this.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        this.setForeground(textColor);
        this.hint = hint;
    }

    public void resetLabel(String hint, int revealAt){
        if(this.revealTimer != null){
            this.revealTimer.stop();
        }

        this.setText("Reveals Automatically After Guess "+revealAt);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.textColor = Color.WHITE;
        this.setForeground(textColor);
        this.hint = hint;
        this.backgroundOpacity = 255;
        this.textOpacity = 255;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(this.backgroundOpacity > 0){
            // Draw the background rounded rectangle
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), this.backgroundOpacity));
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            g2d.dispose();
        }
        
        // Call the superclass method to draw the text
        super.paintComponent(g);
    }



    /**
     * Slowly reveals the label by decreasing the opacity of the background
     */
    public void reveal(){
        revealTimer = new Timer(10, new ActionListener() {
            int textOpacityModifier = 2;
            @Override
            public void actionPerformed(ActionEvent e) {
                // Decrease the opacity
                backgroundOpacity -= OPACITY_DECREMENT;
                textOpacity -= OPACITY_DECREMENT*textOpacityModifier;

                // Start fading in the new text when the old text is faded out
                if(textOpacity <= 0){
                    textOpacityModifier *= -1;
                    textColor = Color.BLACK; // Text Color
                    setHorizontalAlignment(SwingConstants.LEFT); // Left align
                    setText(" "+hint); // Blank space for formatting
                    
                }
                
                setForeground(new Color(textColor.getRed(),textColor.getGreen(),textColor.getBlue(),Math.max(0,textOpacity)));

                // Stop the timer when the background opacity is less than 0
                if(backgroundOpacity <= 0){
                    backgroundOpacity = 0; // Set to 0 in case it's negative
                    ((Timer)e.getSource()).stop();
                }

                repaint();
            };
        });

        revealTimer.start();
    }
}