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

    public ScorePanel(){
        super(null,0,0,0,true); // Uses Interactive Panels methods, not it's instance variables

        this.imageScore = 0;
        this.totalScore = 0;

        setupContentArea();

        newImage();
    }

    protected void resetPanel(String newTargetWord) {
        super.resetPanel(newTargetWord);
    }

    @Override
    public void resetInstanceVariables() {
        /* Handled in resetPanel and resetContentArea */
    }

    @Override
    public void resetContentArea() {
        newImage();
    }

    /**
     * Initializes the components in the panel
     */
    @Override
    protected void setupContentArea(){
        this.setLayout(new BorderLayout());

        this.totalScoreLabel = new JLabel("Total Score: "+totalScore);
        totalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalScoreLabel.setFont(new Font("Lexend", Font.BOLD, 25));
        this.add(this.totalScoreLabel,BorderLayout.WEST);

        this.imageScoreLabel = new JLabel("Image Score: "+imageScore);
        imageScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageScoreLabel.setFont(new Font("Lexend", Font.BOLD, 25));
        this.add(this.imageScoreLabel,BorderLayout.CENTER);

        this.add(new JPanel(), BorderLayout.SOUTH);
    }

    /**
     * Sets the starting value for imageScore. Called once per image.
     * @param difficulty The difficulty of the image.
     */
    public void newImage(){
        switch(Game.getCurrentGame().getDifficulty()){
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
        updateImageScore(0);
    }

    /**
     * @param score The amount to add to imageScore 
     */
    public void updateImageScore(int score){
        this.imageScore += score;
        this.imageScoreLabel.setText("Image Score: "+this.imageScore);
    }

    /**
     * Updates the total score display.
     * @param newTotalScore Sets the display to this number.
     */
    public void setTotalScore(int newTotalScore){
        this.totalScore = newTotalScore;
        this.totalScoreLabel.setText("Total Score: "+this.totalScore);
    }

    /**
     * @return The current score for the image
     */
    public int getImageScore(){
        return this.imageScore;
    }
}