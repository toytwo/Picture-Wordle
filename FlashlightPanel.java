import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FlashlightPanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage image;
    private int radius;
    private int x, y;
    private Ellipse2D oval;
    private java.util.List<Area> revealedAreas;

    public FlashlightPanel() {
        try {
            image = ImageIO.read(new File("wolf.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        radius = 50;
        x = -1;
        y = -1;
        revealedAreas = new java.util.ArrayList<>();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (oval != null) {
            revealedAreas.add(new Area(oval));

            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        oval = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        BufferedImage background= new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D backgroundGraphics = background.createGraphics();


        if (image != null) {
            // int imageX = (getWidth() - image.getWidth()) / 2;
            // int imageY = (getHeight() - image.getHeight()) / 2;

            // g.drawImage(image, imageX, imageY, this);
            backgroundGraphics.drawImage(image, 0, 0, this);
    
            //g2d.dispose();
            //g.drawImage(image, 0, 0, this);
        }
        
        backgroundGraphics.setColor(new Color(0, 0, 0, 100));
        backgroundGraphics.setComposite(AlphaComposite.Clear);

        for (Area area : revealedAreas) {
            backgroundGraphics.fill(area);

        }

        backgroundGraphics.dispose();

        g.drawImage(background, 0, 0, this);

        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, getWidth(), getHeight());

        if (oval != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.draw(oval);
            g2d.fill(oval);
            g2d.setColor(Color.BLACK);
            g2d.draw(oval);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 19));
            g2d.drawString("Reveal", x - 29, y);
        }
        setBackground(Color.GREEN);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashlight Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlashlightPanel panel = new FlashlightPanel();
        frame.add(panel);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
