
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.DefaultListCellRenderer;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class MenuNotification extends FadingNotification {
    /**
     * The word the user was trying to guess
     */
    private String targetWord;
    /**
     * The number of guesses the user made.
     */
    private int GUESSES_USED;
    /**
     * True if the user correctly guessed the word. False otherwise.
     */
    private boolean wasCorrectGuess;
    private String messageTitle;
    private String messageSubtitle;

    /**
     * @param title The main text
     * @param subtitle The text under the main text
     */
    public MenuNotification(String targetWord, boolean wasCorrectGuess, int GUESSES_USED) {
        super(false,true);
        
        this.targetWord = targetWord;
        this.GUESSES_USED = GUESSES_USED;
        this.wasCorrectGuess = wasCorrectGuess;

        // Must be called here and not in superclass in order to properly add elements (I don't know why)
        setupContentArea();
    }

    @Override
    public void setupContentArea() {
        // Customize the JDialog
        setLayout(new BorderLayout()); // Layout
        setBackground(Color.BLACK); // Background color
        setSize(500, 250); // Size

        // Calculate the desired position
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int dialogWidth = getWidth();
        int dialogHeight = getHeight();
        int x = (screenWidth - dialogWidth) / 2; // Center horizontally
        int y = screenHeight / 3 - dialogHeight / 2; // 2/3 of height, centered vertically

        // Set the dialog's location
        setLocation(x, y);

        JPanel headerPanel = setupHeader();
        JPanel bodyPanel = setupBody();

        this.add(headerPanel,BorderLayout.NORTH);
        this.add(bodyPanel,BorderLayout.CENTER);
        this.add(new JPanel(), BorderLayout.EAST);
        this.add(new JPanel(), BorderLayout.WEST);
    }

    /**
     * Adds Title and Subtitle to the header.
     * @return The JPanel containing the header
     */
    private JPanel setupHeader(){
        JPanel headerPanel = new JPanel(new BorderLayout());

        determineMessageText();

        JLabel titleLabel = new JLabel(messageTitle);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lexend", Font.BOLD, 40));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel(messageSubtitle);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Lexend", Font.PLAIN, 20));
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel setupBody(){
        JPanel bodyPanel = new JPanel(new BorderLayout());

        // West
        JPanel westPanel = new JPanel(new BorderLayout());

        JButton mainMenuButton = new JButton("MAIN MENU");
        mainMenuButton.setFont(new Font("Lexend", Font.BOLD, 20));
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PicturMenu(Main.defaultGuessPanel,Main.defaultRevealPanel,Main.defaultDifficulty,Main.defaultDoModularDifficulty, Main.defaultPointsEnabled);
                Game.getCurrentGame().dispose();
            }
        });
        westPanel.add(mainMenuButton,BorderLayout.CENTER);

        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setFont(new Font("Lexend", Font.BOLD, 15));
        westPanel.add(emptyLabel, BorderLayout.NORTH);

        bodyPanel.add(westPanel, BorderLayout.WEST);

        // Center
        JPanel centerPanel = new JPanel(new BorderLayout());

        JButton nextImageButton = new JButton("NEXT IMAGE");
        nextImageButton.setFont(new Font("Lexend", Font.BOLD, 20));
        nextImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fadeOut();
                Game.getCurrentGame().resetGame();
            }
            
        });
        centerPanel.add(nextImageButton,BorderLayout.CENTER);

        emptyLabel = new JLabel(" ");
        emptyLabel.setFont(new Font("Lexend", Font.BOLD, 15));
        centerPanel.add(emptyLabel, BorderLayout.NORTH);

        bodyPanel.add(centerPanel,BorderLayout.CENTER);

        // East
        JPanel eastPanel = new JPanel(new BorderLayout());

        JLabel difficultyLabel = new JLabel("DIFFICULTY");
        difficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Lexend", Font.BOLD, 15));
        eastPanel.add(difficultyLabel,BorderLayout.NORTH);

        JComboBox<String> difficultySelector = new JComboBox<String>(new String[]{"EASY","MEDIUM","HARD"});
        difficultySelector.setFont(new Font("Lexend", Font.BOLD, 20));
        // Set the alignment
        difficultySelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });
        difficultySelector.setSelectedIndex(Game.getCurrentGame().getDifficulty());
        difficultySelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.getCurrentGame().setDifficulty(difficultySelector.getSelectedIndex());
            }
        });
        eastPanel.add(difficultySelector,BorderLayout.CENTER);

        bodyPanel.add(eastPanel,BorderLayout.EAST);

        // North
        JPanel northPanel = new JPanel();
        bodyPanel.add(northPanel, BorderLayout.NORTH);

        // South
        JPanel southPanel = new JPanel();
        Game.getCurrentGame().updateTotalScore();
        JLabel scoreLabel = new JLabel(String.valueOf("Score: "+Game.getCurrentGame().getTotalScore()));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Lexend", Font.BOLD, 20));
        southPanel.add(scoreLabel);
        bodyPanel.add(southPanel, BorderLayout.SOUTH);

        return bodyPanel;
    }

    /**
     * Determines the content of the message based on whether not the user was successful, 
     * their points, and how many guesses it took them.
     */
    private void determineMessageText(){
        // Title
        this.messageTitle = pickPhrase(this.wasCorrectGuess);

        // Subtitle
        this.messageSubtitle = "The word was '"+this.targetWord+"'.";
        if(this.wasCorrectGuess){
            this.messageSubtitle += " You guessed it in "+this.GUESSES_USED+".";
        }
        else{
            this.messageSubtitle += " You didn't guess the word.";
        }
    }

    private String pickPhrase(boolean isHappyPhrase){
        Random random = new Random();
        String[] sadPhrases = {
            "GOOD TRY!",
            "OH NO!",
            "NEXT TIME!",
            "KEEP TRYING!",
            "DON'T GIVE UP!",
            "IT'S OKAY!",
            "ALMOST THERE!",
            "HANG IN THERE!",
            "BETTER LUCK!",
            "YOU'LL PREVAIL!",
            "CHIN UP!",
            "UNLUCKY!",
            "DON'T GIVE IN!",
            "KEEP PUSHING!",
            "YOU'RE PROGRESSING!",
            "TRY AGAIN!",
            "STAY POSITIVE!",
            "STAY STRONG!",
            "PERSIST ON!",
            "YOU'RE NEARING!",
            "KEEP GOING!",
            "YOU'RE ON TRACK!",
            "PERSEVERE!",
            "NOT THE END!",
            "UNFORTUNATE!",
            "YOU'RE ADVANCING!",
            "LEARN & GROW!",
            "STAY DETERMINED!"
        };        
    
        String[] happyPhrases = {
            "WELL DONE!",
            "BRAVO!",
            "EXCELLENT WORK!",
            "FANTASTIC!",
            "WAY TO GO!",
            "OUTSTANDING!",
            "KUDOS TO YOU!",
            "TERRIFIC JOB!",
            "GREAT JOB!",
            "AMAZING WORK!",
            "SUPERB!",
            "IMPRESSIVE!",
            "YOU NAILED IT!",
            "PHENOMENAL!",
            "KEEP IT UP!",
            "YOU'RE A STAR!",
            "HATS OFF TO YOU!",
            "THUMBS UP!",
            "INCREDIBLE!",
            "TOP-NOTCH!",
            "BRAVO, CHAMP!",
            "FABULOUS!",
            "YOU'RE KILLING IT!",
            "AWESOME!",
            "REMARKABLE!",
            "YOU'RE A ROCKSTAR!",
            "SPLENDID!",
            "A JOB WELL DONE!",
            "YOU'RE UNSTOPPABLE!"
        };
    
        String[] chosenPhrases;
    
        if(isHappyPhrase){
            chosenPhrases = happyPhrases;
        }
        else{
            chosenPhrases = sadPhrases;
        }
    
        return chosenPhrases[random.nextInt(chosenPhrases.length)];
    }
    
}
