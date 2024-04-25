import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;

/**
 * @author Babucarr
 * @version ( 04/13/2024)
 */
public class MenuPanel extends JFrame implements ActionListener{
      private JFrame frame;
      private JLabel words;
      private JButton startButton;
      private JButton backButton;
      private CardLayout cardLayout;
      private JPanel menuArea;
   
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
                //THIS WILL CALL START FROM GAME.
        }
        
        
            if(comm.equals("EASY")){
                //LOGIC FOR EASY
            }
            else if(comm.equals("MODERATE")){
                //LOGIC HERE
            }
            else{
                if(comm.equals("HARD")){
                    //LOGIC HERE
                }
            
             
        }
        
        if(comm.equals("Modular Difficulty")){
            //logic
        }
        
        if(comm.equals("SETTINGS")){
            cardLayout.show(getContentPane(), "settingsPanel");  
        }
        
        if(comm.equals("BACK")){
             cardLayout.show(getContentPane(), "menuArea");
        }
        
        if(comm.equals("SIMPLE REVEAL")){
            //LOGIC
        }
        if(comm.equals("BY ICON")){
            //LOGIC
        }
        if(comm.equals("BY COLOR")){
            //LOGIC
        }
        
        if(comm.equals("SIMPLE GUESS")){
            //LOGIC
        }
        if(comm.equals("WORD TYPE")){
            //LOGIC
        }
        if(comm.equals("EX")){
            //LOGIC
        }
    }
    
    private void setupMenu(){
        
         JPanel menuArea = new JPanel(){
            @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            ImageIcon image = new ImageIcon("umbrella.jpeg");
            g.drawImage( image.getImage(),0,0, getWidth(), getHeight(), this);
       
          }};
        
        menuArea.setPreferredSize(frame.getSize()); 
        menuArea.setLayout(null);
         JPanel settingsPanel = new JPanel(){
            @Override
         protected void paintComponent(Graphics g){
            super.paintComponent(g);
            ImageIcon imageIcon = new ImageIcon("Apple.jpg");
          g.drawImage( imageIcon.getImage(),0,0, getWidth(), getHeight(), this);
       
          }};
           settingsPanel.setPreferredSize(frame.getSize()); 
          settingsPanel.setLayout(null);
          
          JCheckBox checkbox1 = new JCheckBox ("Modular Difficulty");
           checkbox1.setFont(new Font("Arial", Font.BOLD, 15));
          checkbox1.setBounds(800,400,150,75);
          settingsPanel.add( checkbox1);
          checkbox1.addActionListener(this);
          checkbox1.setOpaque(false);
        checkbox1.setContentAreaFilled(false);
        checkbox1.setBorderPainted(false);
        checkbox1.setSelected(true);

          
        backButton = new JButton("BACK");
        backButton.addActionListener(this);
        backButton.setBounds(200,50,100,30);
         backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
         settingsPanel.add(backButton);
        
        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.addActionListener(this);
        settingsButton.setBounds(442,350,140,30);
        settingsButton.setFont(new Font("Arial", Font.BOLD, 20));
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        menuArea.add(settingsButton);
        
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        JLabel titleLabel = new JLabel("PICTUR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 100));
        titleLabel.setBounds(300,50,500,250);
        titleLabel.setForeground(Color.MAGENTA);
        menuArea.add(titleLabel);
        
        JLabel credits = new JLabel("Credits: Jackson,Damon, Babucarr, Gishe ");
        credits.setFont(new Font("SansSerif", Font.BOLD, 13));
        credits.setBounds(5,450,350,200);
        menuArea.add(credits);
                
        JButton startButton = new JButton("START");
        startButton.addActionListener(this);
        startButton.setBounds(450,240,120,30);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        menuArea.add(startButton);
        
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(this);
        exitButton.setBounds(450,300,120,30);
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        menuArea.add(exitButton);
        
        String[] difficulty = {"EASY", "MODERATE", "HARD"};
        JComboBox<String> comboBox = new JComboBox<>(difficulty);
        settingsPanel.add(comboBox);
        String selectedOption = (String) comboBox.getSelectedItem();
        comboBox.setBounds(450,240,100,30);
        comboBox.setOpaque(false);
        comboBox.setSelectedItem("EASY");
        
        String[] guess = {"SIMPLE GUESS","WORD TYPE","EX"};
        JComboBox<String> guesscomboBox = new JComboBox<>(guess);
        settingsPanel.add(guesscomboBox);
        String selectedguess = (String) comboBox.getSelectedItem();
        guesscomboBox.setBounds(444,350,110,30);
         guesscomboBox.setSelectedItem("SIMPLE GUESS");
        
        String[] reveal = {"SIMPLE REVEAL", "BY COLOR", "BY ICON"};
        JComboBox<String> revealcomboBox = new JComboBox<>(reveal);
        settingsPanel.add(revealcomboBox);
        String selectedreveal = (String) comboBox.getSelectedItem();
        revealcomboBox.setBounds(445,460,110,30);
        revealcomboBox.setSelectedItem("SIMPLE REVEAL");
        
       
         add(menuArea, "menuArea");
         add(settingsPanel, "settingsPanel");
         
        }
    
    public MenuPanel(){
        frame = new JFrame();
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Picture-Wordle");
        setupMenu();
        setLocationRelativeTo(null);
        setVisible(true);
        repaint();
    }
    
    public static void main(String[] args){
        MenuPanel  menuPanel = new MenuPanel();
       menuPanel.setVisible(true);  
    }
}
