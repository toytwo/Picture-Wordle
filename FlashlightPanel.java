import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class FlashlightPanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage image;
    private int radius;
    private int x, y;
    private Ellipse2D oval;
    private List<Area> revealedAreas;
    private int ovalCount;

    public FlashlightPanel() {
        try {
            image = ImageIO.read(new File("wolf.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        radius = 70;
        x = -1;
        y = -1;
        revealedAreas = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(750, 750); // Set the preferred size to 1000x1000
    }

    public void mouseClicked(MouseEvent e) {
        if (oval != null) {
            revealedAreas.add(new Area(oval)); // Add the clicked oval to the list of revealed areas

            ovalCount++;
            System.out.println(ovalCount);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        oval = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        repaint();
    }

    public void mouseDragged(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        if (image != null) {
            BufferedImage scaledImage = scaleImage(image, getWidth(), getHeight());

            g.drawImage(scaledImage, 0, 0, this);
        }

        // Subtract revealed areas from the black screen
        Area mask = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
        for (Area area : revealedAreas) {
            mask.subtract(area);
        }

        // Create a buffered image for the mask
        BufferedImage maskImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D maskGraphics = maskImage.createGraphics();
        maskGraphics.setColor(Color.BLACK);
        maskGraphics.fill(mask);
        maskGraphics.dispose();

        // Draw the mask onto the panel
        g.drawImage(maskImage, 0, 0, this);

        // Draw the oval representing the flashlight
        if (oval != null) {
            Graphics2D g2d2 = (Graphics2D) g.create();
            g2d2.setColor(Color.WHITE);//Color of Floating Oval
            g2d2.draw(oval);
            g2d2.fill(oval);
            g2d2.setColor(Color.BLACK);//Color of Reveal text
            g2d2.draw(oval);//Draws string onto oval
            g2d2.setFont(new Font("SansSerif", Font.BOLD, 19));
            g2d2.drawString("Reveal", x - 29, y);
            g2d2.dispose();
        }

    }

    private BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashlight Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlashlightPanel panel = new FlashlightPanel();
        frame.add(panel);

        frame.setSize(750, 750);
        frame.setVisible(true);
    }
}
