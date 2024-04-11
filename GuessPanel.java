import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
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
 * @version Updated: 4/08/2024
 */
public abstract class GuessPanel extends InteractivePanel{
    /**
     * The number of guesses made.
     */
    protected int guessNumber;
    /** 
     * The max number of guesses that can be made. Also determines the number of guess rows. 
     */
    protected int MAX_GUESSES;
    /**
     * The text field for each guess.
     */
    protected JComboBox<String>[] guessFields;
    /**
     * The word bank of guessable words.
     */
    protected AdaptingStringList wordBank;
    /**
     * If the word bank is being updated
     */
    protected boolean updatingWordBank;


    /** 
     * @param maxGuesses The max number of guesses that can be made. Also determines the number of guess rows. 
     * @param ratio The number of guesses per reveal.
     */
    @SuppressWarnings("unchecked")
    public GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES){
        super(new GridBagLayout(), targetWord, SWAP_THRESHOLD, doSwapThreshold);
        this.MAX_GUESSES = MAX_GUESSES;
        this.guessNumber = 0;
        this.wordBank = new AdaptingStringList();
        this.guessFields = new JComboBox[MAX_GUESSES];
        this.updatingWordBank = false;
    }

    public void setupContentArea(){
        // General Constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = -1; // Y position in the grid (-1 because we add 1 every time so first will be -1+1=0)
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

        // Use empty boxes to left align the textfield and have it take up 5/6 of the width of the panel
        Component rightRigidArea = Box.createRigidArea(new Dimension(0, 0));

        // Create each guessField
        for(int i = 0; i < MAX_GUESSES; i++){
        guessFields[i] = new JComboBox<String>();
        // Customize the gridBagLayout
        
        constraints.gridy++;

        // Create the textfield
        constraints.gridx = 0; // X position in the grid
        constraints.gridwidth = 2; // Number of cells wide
        constraints.weightx = 5.0/6.0; // 5/6 of the width
        this.add(guessFields[i],constraints);
        guessFields[i].setVisible(true);

        // Create empty component for right space
        constraints.gridx = 2; // X position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = 1.0 / 6.0; // 1/6 of the width
        this.add(rightRigidArea, constraints);

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
        // Change the dropdown button to a guess button
        guessFields[i].setUI(new MetalComboBoxUI(){
        @Override
        protected JButton createArrowButton() {
        // Return the guess button
        JButton makeGuessButton = new JButton();
        // Make a guess when clicking the button
        makeGuessButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            makeGuess();
        }
        
        });
        makeGuessButton.setText("Guess");
        // Make the button visible
        makeGuessButton.setVisible(true);
        return makeGuessButton;
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

                // Hide the first item because the part is always contained there
                if (index == 0 && !(wordBank.isPartInWordList() && wordBank.getWordList().length == 1) ) {
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
                        makeGuess();
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
        }
        // Add an empty item so the editor starts empty
        guessFields[guessNumber].addItem("");
        for(String word : wordBank.getWordList()){
            guessFields[guessNumber].addItem(word);
        }  
        // Enable the code after a delay. Without the delay the code doesn't run properly.
        Timer timer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set the text cursor to the guessField
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
            };
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Pares the Word Bank to only display words containing newPart and displays them based on how close the part is to the start of the word alphabetically.
     * @param newPart The String to pare the WordBank list with
     */
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

    public void makeGuess(){
        boolean guessOutcome = this.targetWord.toLowerCase().equals(this.wordBank.getPart());

        // Invalid Guess
        if(!this.wordBank.isPartInWordList()){
            return;
        }
        // Correct Guess
        if(guessOutcome){
            System.out.println("Guessed the word");
            Main.playAgain();
        }
        // Incorrect Guess
        else{
            // Do nothing
        }

        // Disable the previous guessField
        guessFields[guessNumber].setEnabled(false);
        guessNumber++;
        // If the user has used all guesses
        if(guessNumber>MAX_GUESSES){
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
}