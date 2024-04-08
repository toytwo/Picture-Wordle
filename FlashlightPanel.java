import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

public class FlashlightPanel extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage image;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int radius;
    public JLabel reveal;
    private Ellipse2D oval;
    private Ellipse2D [] ovals;
    private int numOfOvals = 0;
    private boolean pressed = false;
    
    
    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
        repaint();
    }
    
    public void mouseDragged(MouseEvent e){
        
    }
    
    public void mouseClicked(MouseEvent e){
        // g.drawImage(image,0,0,this);
        // x = e.getX();
        // y = e.getY();
        // ovals[numOfOvals] = oval;
        // numOfOvals++;
        // System.out.println(oval == null);
        // repaint();
    }
    
    public void mousePressed(MouseEvent e){
        if(pressed){
            pressed = false;
            return;
        }
        x2 = e.getX();
        y2 = e.getY();
        ovals[numOfOvals] = oval;
        numOfOvals++;
        System.out.println(oval == null);
        repaint();
        pressed = true;
    }
    
    public void mouseReleased(MouseEvent e){
    }
    
    public void mouseEntered(MouseEvent e){
        
    }
    
    public void mouseExited(MouseEvent e){
        
    }
    
    public FlashlightPanel(){
        try{
            image = ImageIO.read(new File("wolf.jpg"));
        }
        catch(Exception e){
            System.exit(1);
        }
        radius = 50;
        x = -1;
        y = -1;
        reveal = new JLabel("Reveal");
        reveal.setForeground(Color.BLACK);
        addMouseListener(this);
        ovals = new Ellipse2D[20];


        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public int getWidth(){
        if(image == null) return 0;
        return image.getWidth();
    }
    
    public int getHeight(){
        if(image == null) return 0;
        return image.getHeight();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            
        if(image != null){
                g.setColor(Color.WHITE);
                //g.drawImage(image,0,0,this);
        }
        if(x != -1){
                // Creates the oval, but don't draw it
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                Ellipse2D oval = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
                //g.drawString("Reveal", x-20, y);
                // Draw oval
                g2d.fill(oval);
                g2d.setColor(new Color(0,0,0));

                g2d.setFont((new Font("SansSerif", Font.BOLD, 19)));
                g2d.drawString("Reveal", x-29, y);


                // Create a region representing everything outside of the oval
                Area outsideRegion = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
                outsideRegion.subtract(new Area(oval));
                //Area insideRegion = new Area(oval);
                
                //insideRegion.add(oval);

                // Fill the outside region with a color
                g2d.setColor(Color.BLACK);
                g2d.fill(outsideRegion);
                


                //Ellipse2D oval2 = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);

        }
        else{
                Graphics2D g2d = (Graphics2D) g.create();
                Area fullWindow = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
                // Fill the outside region with a color
                g2d.setColor(Color.BLACK);
                g2d.fill(fullWindow);
        }

        Graphics2D g3d = (Graphics2D) g.create();
        for (int i = 0; i < numOfOvals; i++) {
            Ellipse2D currentOval = ovals[i];
            ovals[i] = oval;
            Area insideRegion = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
            insideRegion.subtract(new Area(currentOval));
            g.drawImage(image,0,0,this);
            g3d.drawImage(image,x2,y2,this); // Draw the image first
            g3d.setColor(Color.RED); // Set the color for drawing the ellipse outline and filling the region
            g3d.draw(ovals[i]); // Draw the ellipse outline
            g3d.fill(insideRegion);
            
        }
    }

    public static void main(String[] args) {
    JFrame frame = new JFrame("Outside Oval Fill Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    FlashlightPanel panel = new FlashlightPanel();
    frame.add(panel);
    
    frame.revalidate();
    frame.setSize(panel.getWidth(), panel.getHeight());
    frame.setVisible(true);
    }
}
