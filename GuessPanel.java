import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Jackson Alexman
 * @version Created: 3/27/2024 Updated: 3/30/2024
 */

public abstract class GuessPanel{
    protected JPanel guessPanel;
    /**
     * The number of guesses per reveal.
     */
    protected double ratio;
    /**
     * The number of guesses made.
     */
    protected int guessNumber;
    /** 
     * The max number of guesses that can be made. Also determines the number of guess rows. 
     */
    protected int maxGuesses;
    /**
     * The text field for each guess.
     */
    protected JComboBox<String>[] guessFields;
    /**
     * The word bank of guessable words.
     */
    protected AdaptingLinkedStringList wordBank;
    /**
     * The word to be guessed
     */
    protected String targetWord;
    /**
     * If the word bank is being updated
     */
    protected boolean updatingWordBank;


    /** 
     * @param maxGuesses The max number of guesses that can be made. Also determines the number of guess rows. 
     * @param ratio The number of guesses per reveal.
     */
    public GuessPanel(double ratio, int maxGuesses, String targetWord){
        this.ratio = ratio;
        this.maxGuesses = maxGuesses;
        this.guessPanel = new JPanel(new GridBagLayout());
        this.guessNumber = 0;
        this.targetWord = targetWord;
        this.wordBank = new AdaptingLinkedStringList();
        this.guessFields = new JComboBox[maxGuesses];
        this.updatingWordBank = false;

        // General Constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = -1; // Y position in the grid (-1 because we add 1 every time so first will be -1+1=0)
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

        // Use empty boxes to center the textfield and have it take up 2/3 of the width of the panel
        Component leftRigidArea = Box.createRigidArea(new Dimension(0, 0));
        Component rightRigidArea = Box.createRigidArea(new Dimension(0, 0));

        for(int i = 0; i < maxGuesses; i++){
            guessFields[i] = new JComboBox<String>();
            // Update the wordbank if the user selects one of the popup list options
            guessFields[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Update the word bank if a different item in the popup has been selected and the word bank isn't being updated
                    // because updating the word bank changes the selected item in the popup
                    if (e.getStateChange() == ItemEvent.SELECTED && !updatingWordBank) {
                        // Update the word bank
                        updateWordBank((String) guessFields[guessNumber].getSelectedItem());
                    }
                }
            });
            // Make the dropdown arrow invisible
            guessFields[i].setUI(new MetalComboBoxUI(){
                @Override
                protected JButton createArrowButton() {
                    // Return a hidden arrow button
                    JButton hiddenButton = new JButton();
                    hiddenButton.setPreferredSize(new Dimension(0, 0));
                    hiddenButton.setMinimumSize(new Dimension(0, 0));
                    hiddenButton.setMaximumSize(new Dimension(0, 0));
                    hiddenButton.setVisible(false);
                    return hiddenButton;
                }
            });
            // Allow the user to type in the combobox
            guessFields[i].setEditable(true);
            // Only enable the first guessField
            guessFields[i].setEnabled(i==0);

            // Set a custom renderer for the combo box so that the part that is typed doesn't show in the popup list of elements
            guessFields[i].setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Get the component from the default renderer
                JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Check if the item is the one to be hidden
                if (wordBank.getPart().equals(value)) {
                    // Set an empty string to hide it visually
                    rendererComponent.setText("");
                }

                return rendererComponent;
            }
            });
            // The Key Listener must be added to the editor component of the combobox, not the combobox itself
            JTextField editor = (JTextField) guessFields[i].getEditor().getEditorComponent();
            editor.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {/* Do Nothing */}
    
                @Override
                public void keyPressed(KeyEvent e) {
                    // Cycle to the next guessField
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        Boolean guessOutcome = submitGuess();
                        // Invalid Guess
                        if(guessOutcome == null){
                            return;
                        }
                        // Correct Guess
                        if(guessOutcome){
                            System.out.println("Guessed the word");
                            System.exit(0);
                        }
                        // Incorrect Guess
                        else{
                            // Do nothing
                        }

                        // Disable the previous guessField
                        guessFields[guessNumber].setEnabled(false);
                        guessNumber++;
                        // If the user has used all guesses
                        if(guessNumber>maxGuesses){
                            return;
                        }
                        // Update the wordbank
                        wordBank.updateLetters("");
                        // Add all the word bank words to the guessField popup
                        for(String word : wordBank.getWordList()){
                            guessFields[guessNumber].addItem(word);
                        }
                        // Enable the new guessField
                        guessFields[guessNumber].setEnabled(true);
                        // Set the text cursor to the new guessField
                        guessFields[guessNumber].requestFocus();
                        // Wait 100 seconds before enabling the popup. Without the delay, the popup does not show.
                        Timer timer = new Timer(100, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                guessFields[guessNumber].setPopupVisible(true);
                            };
                        });
                        // Only activate once
                        timer.setRepeats(false);
                        timer.start();
                    }
                    else{
                        // Create a timer to delay retrieving the text. Without the delay, the last letter typed isn't recorded.
                        Timer timer = new Timer(1, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Update the wordbank
                                updateWordBank(editor.getText());

                                // If the popup has been disabled, enable it
                                if(!guessFields[guessNumber].isPopupVisible()){
                                    guessFields[guessNumber].setPopupVisible(true);
                                }
                            };
                        });
                        // Make the timer only activate once
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
    
            @Override
            public void keyReleased(KeyEvent e) {/* Do Nothing */}
                
            });
            constraints.gridy++;

            // Create empty component for left space
            constraints.gridx = 0; // X position in the grid
            constraints.gridwidth = 1; // Number of cells wide
            constraints.weightx = 1.0 / 6.0; // 1/6 of the width
            guessPanel.add(leftRigidArea, constraints);

            // Create the textfield
            constraints.gridx = 1; // X position in the grid
            constraints.gridwidth = 1; // Number of cells wide
            constraints.weightx = 2.0/3.0; // 2/3 of the width
            guessPanel.add(guessFields[i],constraints);
            guessFields[i].setVisible(true);

            // Create empty component for right space
            constraints.gridx = 2; // X position in the grid
            constraints.gridwidth = 1; // Number of cells wide
            constraints.weightx = 1.0 / 6.0; // 1/6 of the width
            guessPanel.add(rightRigidArea, constraints);
        }
        // Add an empty item so the editor starts empty
        guessFields[guessNumber].addItem("");
        for(String word : wordBank.getWordList()){
            guessFields[guessNumber].addItem(word);
        }  
        // Enable the popup after a delay. Without the delay the popup doesn't show.
        Timer timer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guessFields[guessNumber].setPopupVisible(true);
            };
        });
        timer.setRepeats(false);
        timer.start();
        
    }

    /**
     * @return The guessPanel for this GuessPanelType
     */
    public JPanel getPanel(){
        return this.guessPanel;
    }


    public void updateWordBank(String newPart){
        this.updatingWordBank = true;

        // Update the word bank
        this.wordBank.updateLetters(newPart);
        // Update the guessField popup list
        this.guessFields[this.guessNumber].removeAllItems();
        for(String word : this.wordBank.getWordList()){
            this.guessFields[this.guessNumber].addItem(word);
        }

        this.updatingWordBank = false;
    }

    public Boolean submitGuess(){
        // Check if the guess is valid
        if(!this.wordBank.isPartInWordList()){
            return null;
        }
        return this.targetWord.toLowerCase().equals(this.wordBank.getPart());
    }

}