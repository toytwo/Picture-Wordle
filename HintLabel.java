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
 * @version Updated: 4/24/2024
 */
public class HintLabel extends JLabel {
    private String hint;
    private int backgroundOpacity = 255;
    private int textOpacity = 255;
    private Color textColor = Color.WHITE;
    private static final int OPACITY_DECREMENT = 2;
    public static final int TEXT_SIZE = 20;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    

    public HintLabel(int hintNumber, String hint) {
        super("Hint "+hintNumber, SwingConstants.CENTER);
        this.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        this.setForeground(textColor);
        this.hint = hint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(this.backgroundOpacity > 0){
            // Draw the background rounded rectangle
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(), BACKGROUND_COLOR.getBlue(), Math.max(0,this.backgroundOpacity)));
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
        Timer timer = new Timer(10, new ActionListener() {
            int textOpacityModifier = 2;
            @Override
            public void actionPerformed(ActionEvent e) {
                // Decrease the opacity
                backgroundOpacity -= OPACITY_DECREMENT;
                textOpacity -= OPACITY_DECREMENT*textOpacityModifier;

                if(textOpacity < 0){
                    textOpacityModifier *= -1;
                    setHorizontalAlignment(SwingConstants.LEFT); // Left align
                    textColor = Color.BLACK; // Text Color
                    setText(" "+hint); // Blank space for formatting
                }
                
                setForeground(new Color(textColor.getRed(),textColor.getGreen(),textColor.getBlue(),Math.max(0,textOpacity)));

                // Stop the timer when the opacity is less than 0
                if(backgroundOpacity < 0){
                    ((Timer)e.getSource()).stop();
                }

                repaint();
            };
        });

        timer.start();
    }
}