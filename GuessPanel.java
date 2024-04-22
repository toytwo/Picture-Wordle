import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Jackson Alexman
 * @version Updated: 4/17/2024
 */
public abstract class GuessPanel extends InteractivePanel{
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
     * A display of hints based targetWord which changes everytime a guess is made
     */
    protected HintPanel hintPanel;


    /** 
     * @param maxGuesses The max number of guesses that can be made. Also determines the number of guess rows. 
     * @param ratio The number of guesses per reveal.
     */
    @SuppressWarnings("unchecked")
    public GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_ACTIONS){
        super(new BorderLayout(), targetWord, SWAP_THRESHOLD, doSwapThreshold);
        // Initialize wordBank using an anonymous SwingWorker
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                wordBank = new AdaptingStringList();
                return null;
            }

            @Override
            protected void done() {/* Do Nothing */}
        };
        worker.execute();

        // Initialize instance variables
        this.MAX_ACTIONS = MAX_ACTIONS;
        this.guessFields = new JComboBox[MAX_ACTIONS];
        this.updatingWordBank = false;
        this.hintPanel = new HintPanel(this.targetWord, this.MAX_ACTIONS);
    }

    public void setupContentArea(){
        // Add the hintPanel
        this.add(this.hintPanel, BorderLayout.NORTH);

        // Add the guessField Panel
        JPanel guessFieldPanel = new JPanel(new GridBagLayout());
        this.add(guessFieldPanel, BorderLayout.CENTER);

        // Setup the guessFieldPanel
        // General Constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

        // Use an empty panel to left align the textfield and have it take up 5/6 of the width of the panel
        constraints.gridx = 2; // X position in the grid
        constraints.gridy = 1; // Y position in the grid
        constraints.gridwidth = 1; // Number of cells wide
        constraints.weightx = 1.0 / 6.0; // 1/6 of the width
        constraints.gridheight = MAX_ACTIONS; // Number of cells tall
        guessFieldPanel.add(new JPanel(), constraints);

        constraints.gridy = 0; // Y position in the grid (0 because we add 1 every time so first will be 0+1=1)
        constraints.gridheight = 1; // Number of cells tall

        // Create each guessField
        for(int i = 0; i < MAX_ACTIONS; i++){
            // Create the textfield
            guessFields[i] = new JComboBox<String>();
            guessFields[i].setVisible(false);

            // Customize the gridBagLayout
            constraints.gridy++;
            constraints.gridx = 0; // X position in the grid
            constraints.gridwidth = 2; // Number of cells wide
            constraints.weightx = 5.0/6.0; // 5/6 of the width
            guessFieldPanel.add(guessFields[i],constraints);

            // Update the wordbank if the user selects one of the popup list options
            guessFields[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Update the word bank if a different item in the popup has been selected and the word bank isn't being updated
                    // because updating the word bank changes the selected item in the popup
                    if (e.getStateChange() == ItemEvent.SELECTED && !updatingWordBank) {
                        // Update the word bank
                        updateWordBank((String) guessFields[interactionCount].getSelectedItem());
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
                        interactionPerformed();
                    }});

                    // Set the label of the button
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
                        interactionPerformed();
                    }
                    else{
                        // Create a timer to delay retrieving the text. Without the delay, the last letter typed isn't recorded.
                        Timer timer = new Timer(1, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Update the wordbank
                                updateWordBank(editor.getText());

                                // Close and reopen the popup to update its dimensions
                                guessFields[interactionCount].setPopupVisible(false);
                                guessFields[interactionCount].setPopupVisible(true);
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

            guessFields[i].setVisible(true);
        }

        // Fix the size of every guessField
        for(JComboBox<String> guessField : guessFields){
            guessField.setPreferredSize(new Dimension(250,30));
        }
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
        this.guessFields[this.interactionCount].removeAllItems();
        int wordCount = 0;
        for(String word : this.wordBank.getWordList()){
            wordCount++;
            this.guessFields[this.interactionCount].addItem(word);
            // Only show the first 100 words
            if(wordCount >= 100){
                break;
            }
        }

        this.updatingWordBank = false;
    }

    @Override
    public boolean interactionPerformed(){
        // Invalid Guess
        if(!this.wordBank.isPartInWordList()){
            return false;
        }
        
        boolean guessOutcome = this.targetWord.toLowerCase().equals(this.wordBank.getPart());

        // Correct Guess
        if(guessOutcome){
            // Reset game
            Game.game.updateImageDifficulty(interactionCount, MAX_ACTIONS);
            Game.game.setupGame();
            return false;
        }

        // Incorrect Guess
        // Check if the user has run out of guesses
        if(interactionCount+1 >= MAX_ACTIONS){ // +1 because we increment this later
            // Reset game
            Game.game.setupGame();
            return false;
        }

        // Potentially reveal a hint
        this.hintPanel.checkReveal(interactionCount);

        // Check if it's time to swap
        if(super.interactionPerformed()){
            return true;
        }

        // Disable the previous guessField
        guessFields[interactionCount].setEnabled(false);
    
        // Update the wordbank
        wordBank.updateLetters("");
        // Add all the word bank words to the guessField popup
        for(String word : wordBank.getWordList()){
            guessFields[interactionCount].addItem(word);
        }
        // Enable the new guessField
        guessFields[interactionCount].setEnabled(true);
        // Set the text cursor to the new guessField
        guessFields[interactionCount].requestFocus();
        // Wait 100 seconds before enabling the popup. Without the delay, the popup does not show.
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guessFields[interactionCount].setPopupVisible(true);
            };
        });
        // Only activate once
        timer.setRepeats(false);
        timer.start();

        // Not time to swap
        return false;
    }

    @Override
    public void setPanelEnabled(boolean isEnabled) {
        // Enable panel
        if(isEnabled){
            for(int i = interactionCount; i < MAX_ACTIONS; i++){
                // Enable the new guessField
                guessFields[i].setEnabled(true);
            }

            // Add an empty item so the editor starts empty
            updateWordBank("");
        }
        // Disable panel
        else{
            for(int i = Math.max(interactionCount-1, 0); i < MAX_ACTIONS; i++){
                guessFields[i].setEnabled(false);
            }
        }
    }
}