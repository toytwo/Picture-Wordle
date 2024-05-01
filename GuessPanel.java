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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * @author Jackson Alexman
 * @version Updated: 4/30/2024
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
     * Allows the user to skip the next action(s) for the enabled panel
     */
    protected SkipActionPanel skipActionPanel;


    /** 
     * @param targetWord The word to guess.
     * @param SWAP_THRESHOLD The number of guesses before swapping to revealing.
     * @param doSwapThreshold Whether or not to limit the number of guesses before swapping to revealing.
     * @param MAX_GUESSES The maximum number of guesses for each image.
     * @param guessCost The amount of points to subtract for each guess.
     */
    @SuppressWarnings("unchecked")
    public GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES, int guessCost){
        super(new BorderLayout(), targetWord, SWAP_THRESHOLD, doSwapThreshold, MAX_GUESSES, guessCost);
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
        this.guessFields = new JComboBox[MAX_ACTIONS];
        this.updatingWordBank = false;
        this.hintPanel = new HintPanel(this.targetWord, this.MAX_ACTIONS, Game.REVEAL_HINT_COST);
        this.setPanelDescriptors("Guess", "Guesses");
    }

    @Override
    public void setupContentArea(){
        // Use a centerPanel so hintPanel doesn't draw above the empty Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
    
        // Add the guessField Panel
        JPanel guessFieldPanel = new JPanel(new GridBagLayout());
        centerPanel.add(guessFieldPanel, BorderLayout.CENTER);
    
        // Add an empty panel to the right side for spacing
        JPanel emptyPanel = new JPanel();
        emptyPanel.setMinimumSize(new Dimension(20, 100));
        emptyPanel.setPreferredSize(new Dimension(20, 100));
        this.add(emptyPanel, BorderLayout.EAST);
    
        // Add the hintPanel
        centerPanel.add(this.hintPanel, BorderLayout.NORTH);
    
        // Add the scorePanel
        centerPanel.add(scorePanel, BorderLayout.SOUTH);
    
        // Add the centerPanel
        this.add(centerPanel, BorderLayout.CENTER);
    
        // Setup the guessFieldPanel
        // General Constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
    
        // Customize the gridBagLayout
        constraints.gridx = 0; // X position in the grid
        constraints.gridwidth = 2; // Number of cells wide
        constraints.weightx = 5.0/6.0; // 5/6 of the width
        constraints.gridy = 0; // Y position in the grid (0 because we add 1 every time so first will be 0+1=1)]
    
        // Create each guessField
        for(int i = 0; i < MAX_ACTIONS; i++){
            // Create the textfield
            guessFields[i] = new JComboBox<String>();
            guessFields[i].setFont(new Font("Arial", Font.PLAIN, 20));
            guessFields[i].setEnabled(false);
            guessFields[i].setEditable(true);
            guessFields[i].setVisible(true);
    
            constraints.gridy++;
            guessFieldPanel.add(guessFields[i],constraints);

            // Create a delay between adding the combobox and setting up the combobox to avoid errors
            int index = i;
            Timer delay = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setupGuessField(guessFields[index]);
                }
                
            });
            delay.setRepeats(false);
            delay.start();
        }
    
        if(this.doSwapThreshold){
            // Add the SkipActionPanel
            constraints.gridy++;
            guessFieldPanel.add(this.skipActionPanel, constraints);
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

    private void setupGuessField(JComboBox<String> guessField){
        // Update the wordbank if the user selects one of the popup list options
        guessField.addItemListener(new ItemListener() {
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
        guessField.setUI(new MetalComboBoxUI(){
            @Override
            protected JButton createArrowButton() {
                // Return the guess button
                JButton makeGuessButton = new JButton();

                // Make a guess when clicking the button
                makeGuessButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    interactionPerformed(true);
                }});

                // Set the label of the button
                makeGuessButton.setText("Guess");

                makeGuessButton.setFont(new Font("Arial", Font.BOLD, 20));

                // Make the button visible
                makeGuessButton.setVisible(true);
                
                return makeGuessButton;
                }
        });
        
        // Set a custom renderer for the combo box so that the part that is typed doesn't show in the popup list of elements
        guessField.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Get the component from the default renderer
                JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
                // Hide the first item because the part is always contained there
                if (index == 0 && !(wordBank.isPartInWordList() && wordBank.getWordList().length == 1)) {
                    // Set an empty string to hide it visually
                    rendererComponent.setText("");
                }
                return rendererComponent;
            }
            
        });
        // The Key Listener must be added to the editor component of the combobox, not the combobox itself
        JTextField editor = (JTextField) guessField.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {/* Do Nothing */}

            @Override
            public void keyPressed(KeyEvent e) {
                // Cycle to the next guessField
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    interactionPerformed(true);
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
    }

    @Override
    public boolean interactionPerformed(boolean doSubtractPoints){
        // Action Skipped
        if(!doSubtractPoints){
            // Can't skip last guess
            if(interactionCount+1 >= MAX_ACTIONS){
                (new ActionNotification("NO SKIP", "You cannot skip the last guess.")).setVisible(true);
                return false;
            }

            boolean doSwap = super.interactionPerformed(false);
            this.hintPanel.checkReveal(interactionCount);
            return doSwap;
        }
        // Invalid Guess
        if(!this.wordBank.isPartInWordList()){
            return false;
        }
        
        boolean wasCorrectGuess = this.targetWord.toLowerCase().equals(this.wordBank.getPart());

        // Correct Guess or The user has run out of guesses
        if(wasCorrectGuess || interactionCount+1 >= MAX_ACTIONS){ // Intentionally don't increment interactionCount to differentiate between winning on the last guess and losing on the last guess
            // Reset game
            Game.game.updateImageDifficulty(interactionCount, MAX_ACTIONS, wasCorrectGuess);
            interactionCount++; //Increment after difficulty set
            this.setPanelEnabled(false);
            this.hintPanel.checkReveal(MAX_ACTIONS);
            ((RevealPanel) otherPanel).revealEntireImage();
            (new MenuNotification(targetWord, wasCorrectGuess, interactionCount)).setVisible(true);            
            return false;
        }

        boolean doSwap = super.interactionPerformed(true);

        // Potentially reveal a hint
        this.hintPanel.checkReveal(interactionCount);
        
        if(doSwap){
            return true;
        }

        nextGuess();

        // Not time to swap
        return false;
    }

    @Override
    public void setPanelEnabled(boolean isEnabled) {
        super.setPanelEnabled(isEnabled);

        // Enable panel
        if(isEnabled){
            nextGuess();
        }
        // Disable panel
        else{
            if(interactionCount <= 0){
                return;
            }

            int PREVIOUS_GUESSFIELD = interactionCount-1; 
            guessFields[PREVIOUS_GUESSFIELD].getEditor().getEditorComponent().setBackground(Color.GRAY);
            guessFields[PREVIOUS_GUESSFIELD].setEnabled(false);
        }
    }

    private void nextGuess(){
        // Update the wordbank
        updateWordBank("");
        // Enable the new guessField
        guessFields[interactionCount].setEnabled(true);
        // Set the background color
        guessFields[interactionCount].getEditor().getEditorComponent().setBackground(Color.LIGHT_GRAY);
        // Set the text cursor to the new guessField
        guessFields[interactionCount].requestFocus();
    }

    public void setSkipActionPanel(SkipActionPanel skipActionPanel){
        this.skipActionPanel = skipActionPanel;
    }
}