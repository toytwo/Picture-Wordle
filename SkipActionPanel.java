import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Jackson Alexman
 * @version Updated: 5/2/2024
 */
public class SkipActionPanel extends JPanel {
    private JButton skipNextActionButton;
    private JButton skipActionsTillSwapButton;
    private InteractivePanel enabledPanel;
    /**
     * False if the skip cooldown is active. True otherwise.
     */
    private boolean canSkip;

    public SkipActionPanel(){
        canSkip = true;

        setupContentArea();
    }

    public void setupContentArea(){
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridy = 0;

        this.skipNextActionButton = new JButton();
        this.skipNextActionButton.setFont(new Font("Lexend", Font.BOLD, 20));
        this.skipNextActionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canSkip){
                    (new ActionNotification("NO SKIP", "You must wait before skipping again.")).setVisible(true);
                    return;
                }

                enabledPanel.interactionPerformed(false);
                startSkipCooldown();
            }
        });
        constraints.gridx = 0;
        this.add(skipNextActionButton, constraints);

        this.skipActionsTillSwapButton = new JButton();
        this.skipActionsTillSwapButton.setFont(new Font("Lexend", Font.BOLD, 20));
        this.skipActionsTillSwapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canSkip){
                    (new ActionNotification("NO SKIP", "You must wait before skipping again.")).setVisible(true);
                    return;
                }

                // Skip actions until swap
                while(!enabledPanel.interactionPerformed(false)){
                    // Do nothing
                }
                startSkipCooldown();
            }
        });
        constraints.gridx = 1;
        this.add(skipActionsTillSwapButton, constraints);
    }

    public void setEnabledPanel(InteractivePanel enabledPanel){
        this.enabledPanel = enabledPanel;
        this.skipNextActionButton.setText("Skip "+this.enabledPanel.panelName);
        this.skipActionsTillSwapButton.setText("Skip To Next "+this.enabledPanel.otherPanel.panelName);
    }

    private void startSkipCooldown(){
        this.canSkip = false;

        Timer skipCooldown = new Timer(1200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canSkip = true;
            }
        });

        skipCooldown.setRepeats(false);
        skipCooldown.start();
    }
}
