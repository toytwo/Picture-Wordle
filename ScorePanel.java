import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class ScorePanel extends InteractivePanel{
    /**
     * The score the player currently has for this image.
     */
    private int imageScore;
    /**
     * The current cumulative score for the player.
     */
    private int totalScore;
    private JLabel totalScoreLabel;
    private JLabel imageScoreLabel;
    private int difficulty;

    public ScorePanel(int difficulty){
        super(null,0,false,0,0,true); // Uses Interactive Panels methods, not it's instance variables

        this.imageScore = 0;
        this.totalScore = 0;
        this.difficulty = difficulty;
        newImage(this.difficulty);

        setupContentArea();
    }

    protected void resetPanel(String newTargetWord, int difficulty) {
        super.resetPanel(newTargetWord);

        this.difficulty = difficulty;
    }

    @Override
    public void resetInstanceVariables() {
        /* Handled in resetPanel and resetContentArea */
    }

    @Override
    public void resetContentArea() {
        newImage(difficulty);
        updateImageScore(-this.imageScore); // Reset to 0
        updateImageScore(0);
    }

    /**
     * Initializes the components in the panel
     */
    @Override
    protected void setupContentArea(){
        this.setLayout(new BorderLayout());

        this.totalScoreLabel = new JLabel("Total Score: "+totalScore);
        totalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalScoreLabel.setFont(new Font("Lexend", Font.BOLD, 15));
        this.add(this.totalScoreLabel,BorderLayout.WEST);

        this.imageScoreLabel = new JLabel("Image Score: "+imageScore);
        imageScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageScoreLabel.setFont(new Font("Lexend", Font.BOLD, 15));
        this.add(this.imageScoreLabel,BorderLayout.CENTER);

        this.add(new JPanel(), BorderLayout.SOUTH);
    }

    /**
     * Sets the starting value for imageScore. Called once per image.
     * @param difficulty The difficulty of the image.
     */
    public void newImage(int difficulty){
        switch(difficulty){
            // Easy
            case 0: this.imageScore = 500; break;
            // Medium
            case 1: this.imageScore = 750; break;
            // Hard
            case 2: this.imageScore = 1000; break;
            // Unsorted Image
            case 3: this.imageScore = 500; break;
            default: System.err.println("Invalid Difficulty"); System.exit(0); break;
        }
    }

    /**
     * @param score The amount to add to imageScore 
     */
    public void updateImageScore(int score){
        this.imageScore += score;
        this.imageScoreLabel.setText("Image Score: "+this.imageScore);
    }

    /**
     * Updates the total score display
     * @param newTotalScore
     */
    public void updateTotalScore(int newTotalScore){
        this.totalScore = newTotalScore;
        this.totalScoreLabel.setText("Total ScoreL "+this.totalScore);
    }

    /**
     * @return The current score for the image
     */
    public int getImageScore(){
        return this.imageScore;
    }
}