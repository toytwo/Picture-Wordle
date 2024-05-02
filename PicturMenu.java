import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;

/**
 * @author Babucarr
 * @version ( 04/13/2024)
 */
public class PicturMenu extends JFrame implements ActionListener{
      private JFrame frame;
      private JButton startButton;
      private JButton backButton; private JButton exitButton; private JButton settingsButton;
      private CardLayout cardLayout;
      private JPanel menuArea;
      private JPanel settingsPanel;
    private int defaultGuessPanel;
    private int defaultRevealPanel;
    private int defaultDifficulty;
    private boolean defaultDoModularDifficulty;
    private boolean defaultDoPoints;
       
      /**
       * @ this controls all events of the game menu
       * all buttons clicked responds to something
       */     
        @Override
        public void actionPerformed(ActionEvent e){
         String comm = e.getActionCommand();
        if(comm.equals("EXIT")){
            System.exit(0); 
        }
        
        if(comm.equals("START")){
            new Game(guesscomboBox.getSelectedIndex(), revealcomboBox.getSelectedIndex(),comboBox.getSelectedIndex() , checkbox.isSelected(), 10, defaultDoPoints);
            this.dispose();
        }
        
        
            if(comm.equals("EASY")){
                
            }
            else if(comm.equals("MEDIUM")){
                
            }
            else{
                if(comm.equals("HARD")){
                    
                }
            
             
        }
        
        if(comm.equals("Modular Difficulty")){
            
        }
        
        if(comm.equals("SETTINGS")){
            cardLayout.show(getContentPane(), "settingsPanel");  
        }
        
        if(comm.equals("BACK")){
             cardLayout.show(getContentPane(), "menuArea");
        }
        
        if(comm.equals("SIMPLE REVEAL")){
            
        }
        if(comm.equals("BY ICON")){
            
        }
        if(comm.equals("BY COLOR")){
            
        }
        
        if(comm.equals("SIMPLE GUESS")){
            
        }
        if(comm.equals("WORD TYPE")){
            
        }
    }
    
    /*
     * This method sets up the content area of the menu. 
     * a settings button is used to enter settings panel
     * sports block text is used in drawingthe title and the credits label and a cursor is used and points to buttons
     * whenever the mouse hovers over the location of the buttons.
     */
    private void setupMenu(){
        
         menuArea = new JPanel(){
             //Draws the title of the game and it's credits
            @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            ImageIcon image = new ImageIcon("background.png");
            g.drawImage( image.getImage(),0,0, getWidth(), getHeight(), this);
            g.drawImage( image.getImage(),0,0, getWidth(), getHeight(), this);
            
            g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
                    // Create a translucent color
                    Color overlayColor = new Color(0, 0, 0, 100); // Black with 100/255 opacity
                    // Fill the panel with the translucent color
                    g.setColor(overlayColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
            Graphics2D g2d = (Graphics2D) g;
                String text = "PICTUR";
                Font font = new Font("sanSerif", Font.BOLD, 100);
                g2d.setFont(font);
                Stroke stroke = new BasicStroke(3.0f);
                g2d.setStroke(stroke);
                g2d.setColor(new Color(165,42,42));
                g2d.drawString(text, 335, 150);
                g2d.setStroke(new BasicStroke());
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, 320, 150);
                
                Graphics2D g2 = (Graphics2D) g;
                String credits = "Credits: Jackson,Dammon, Babucarr, Gishe";
                Font cred = new Font("sanSerif", Font.BOLD, 20);
                g2.setFont(cred);
                Stroke stro = new BasicStroke(3.0f);
                g2.setStroke(stro);
                g2.setColor(Color.RED);
                g2.drawString(credits, 05, 550);
                g2.setStroke(new BasicStroke());
                g2.setColor(Color.WHITE);
                g2.drawString(credits, 05, 550 + 2);
                
          }};
           menuArea.setPreferredSize(frame.getSize()); 
           menuArea.setLayout(null);
           
        
        settingsPanel = new JPanel(){
            //This draws the backgroun image for the settings panel
            @Override
         protected void paintComponent(Graphics g){
            super.paintComponent(g);
            ImageIcon imageIcon = new ImageIcon("bed.jpeg");
          g.drawImage( imageIcon.getImage(),0,0, getWidth(), getHeight(), this);

          g.setColor(Color.RED);
          g.setFont(new Font("sanSerif", Font.BOLD, 18));
          g.drawString("DIFFICULTY",431,197);
          g.drawString("GUESS TYPES",423,310);
          g.drawString("REVEAL TYPES",418,425);
          g.drawString("IMAGE MODE",657,314);
       
          }};
        settingsPanel.setPreferredSize(frame.getSize()); 
        settingsPanel.setLayout(null);
          
        JCheckBox checkbox = new JCheckBox ("Modular Difficulty");
        checkbox.setFont(new Font("Arial", Font.BOLD, 15));
        checkbox.setBounds(620,110,150,15);
        checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR)); //This puts the pointing finger cursor when we hover over 
                                                            //the checkbox coordinates
        checkbox.setForeground(Color.BLACK);
        checkbox.addActionListener(this);
        checkbox.setOpaque(false);
        checkbox.setContentAreaFilled(false);
        checkbox.setBorderPainted(false);
        checkbox.setSelected(defaultDoModularDifficulty);
        settingsPanel.add( checkbox);
          
        backButton = new JButton("BACK");
        backButton.addActionListener(this);
        backButton.setBounds(20,50,140,16);
        backButton.setForeground(Color.RED);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        settingsPanel.add(backButton);
        
        settingsButton = new JButton("SETTINGS");
        settingsButton.addActionListener(this);
        settingsButton.setBounds(450,300,157,25);
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsButton.setFont(new Font("Arial", Font.BOLD, 30));
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        menuArea.add(settingsButton);
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
       
        startButton = new JButton("START");
        startButton.addActionListener(this);
        startButton.setBounds(445,240,150,26);
        startButton.setForeground(Color.WHITE);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setFont(new Font("Arial", Font.BOLD, 30));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        menuArea.add(startButton);

        exitButton = new JButton("EXIT");
        exitButton.addActionListener(this);
        exitButton.setBounds(443,350,157,25);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setForeground(Color.WHITE);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        menuArea.add(exitButton);
        
        String[] difficulty = {"EASY", "MEDIUM", "HARD"};
        JComboBox<String> comboBox = new JComboBox<>(difficulty);//This implements a dropdown box for the various game levels
        String selectedOption = (String) comboBox.getSelectedItem();
        comboBox.setBounds(450,200,70,26);
        comboBox.setFont(new Font("Arial", Font.BOLD, 15));
        comboBox.setForeground(Color.BLACK);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comboBox.setOpaque(false);
        comboBox.setSelectedIndex(defaultDifficulty);
        settingsPanel.add(comboBox);
        
        //This creates the guess types the user can choose.
        String[] guess = {"SIMPLE GUESS","WORD TYPE","LOGO"};
        JComboBox<String> guesscomboBox = new JComboBox<>(guess);
        guesscomboBox.setForeground(Color.BLACK);
        guesscomboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String selectedguess = (String) comboBox.getSelectedItem();
        guesscomboBox.setBounds(417,315,145,26);
        guesscomboBox.setFont(new Font("Arial", Font.BOLD, 15));
        guesscomboBox.setSelectedIndex(defaultGuessPanel);
        settingsPanel.add(guesscomboBox);

        String[] imageMode = { "NORMAL", "ICON"};
        JComboBox<String> imageModecomboBox = new JComboBox<> (imageMode);
        imageModecomboBox.setForeground(Color.RED);
        imageModecomboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String selectedImageMode = (String) comboBox.getSelectedItem();
        imageModecomboBox.setBounds(673,320,90,26);
        imageModecomboBox.setFont(new Font("Arial", Font.BOLD, 15));
        imageModecomboBox.setSelectedItem("NORMAL");
        settingsPanel.add(imageModecomboBox);
        
        //This creates the options for reveal types in a dropdown box style.
        String[] reveal = {"SIMPLE REVEAL", "BY COLOR", "BY ICON"};
        JComboBox<String> revealcomboBox = new JComboBox<>(reveal);
        revealcomboBox.setForeground(Color.BLACK);
        revealcomboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String selectedreveal = (String) comboBox.getSelectedItem();
        revealcomboBox.setBounds(415,430,145,26);
        revealcomboBox.setFont(new Font("Arial", Font.BOLD, 15));
        revealcomboBox.setSelectedIndex(defaultRevealPanel);
        settingsPanel.add(revealcomboBox);
        
        add(menuArea, "menuArea");
        add(settingsPanel, "settingsPanel");
         
        }
    
        /*
         * This is the PicturMenu default constructor.
         * It calls setupMenu so we can achieve all the funtionalities we want the game menu to do.
         */
    public PicturMenu(int defaultGuessPanel, int defaultRevealPanel, int defaultDifficulty, boolean defaultDoModularDifficulty, boolean defaultDoPoints){
        // Default settings
        this.defaultGuessPanel = defaultGuessPanel;
        this.defaultRevealPanel = defaultRevealPanel;
        this.defaultDifficulty = defaultDifficulty;
        this.defaultDoModularDifficulty = defaultDoModularDifficulty;
        this.defaultDoPoints = defaultDoPoints;

        // Setup the frame
        frame = this;
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Picture-Wordle");
        setupMenu();
        setLocationRelativeTo(null);
        setVisible(true);
        repaint();
    }
    
    public static void main(String[] args){
        PicturMenu menuPanel = new PicturMenu();
       menuPanel.setVisible(true);  
    }
}
