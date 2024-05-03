import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.BorderLayout;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class ActionNotification extends FadingNotification {
    private String title;
    private String subtitle;

    /**
     * @param title The main text
     * @param subtitle The text under the main text
     */
    public ActionNotification(String title, String subtitle) {
        super(true,false);
        
        this.title = title;
        this.subtitle = subtitle;

        // Must be called here and not in superclass in order to properly add elements (I don't know why)
        setupContentArea();
    }

    @Override
    public void setupContentArea() {
        // Customize the JDialog
        this.setLayout(new BorderLayout()); // Layout
        this.setBackground(Color.BLACK); // Background color
        this.setSize(300, 150); // Size

        // Calculate the desired position
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int dialogWidth = getWidth();
        int dialogHeight = getHeight();
        int x = (screenWidth - dialogWidth / 2) * 55/70;
        int y = (screenHeight / 3 - dialogHeight / 2) * 59/70;

        // Set the dialog's location
        this.setLocation(x, y);

        // Add the title
        JLabel titleLabel = new JLabel(this.title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(titleLabel, BorderLayout.CENTER);

        // Add the subtitle
        JLabel subtitleLabel = new JLabel(this.subtitle);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        this.add(subtitleLabel, BorderLayout.SOUTH);
    }
}