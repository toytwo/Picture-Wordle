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
import java.awt.Font;

/**
<<<<<<< Updated upstream
 * @author Jackson Alexman
 * @version Updated: 4/24/2024
=======
 * The abstract GuessPanel class serves as a base class for panels used for user
 * interaction in guessing games.
 * It provides methods and instance variables common to guess panels.
 * 
 * Instance Variables:
 * - guessFields: The text field for each guess, represented as an array of
 * JComboBoxes.
 * - wordBank: The word bank of guessable words, managed by an
 * AdaptingStringList.
 * - updatingWordBank: A boolean flag indicating whether the word bank is
 * currently being updated.
 * - hintPanel: A display of hints based on the target word, which changes every
 * time a guess is made.
 * - MAX_ACTIONS: The maximum number of actions allowed.
 * 
 * Methods:
 * - GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold,
 * int MAX_ACTIONS): Constructor to initialize a new GuessPanel instance.
 * - setupContentArea(): Method to define the layout and behavior of the guess
 * panel's content area.
 * - updateWordBank(String newPart): Parses the Word Bank to only display words
 * containing newPart and displays them based on how close the part is to the
 * start of the word alphabetically.
 * - interactionPerformed(): Method invoked when an interaction (e.g., guess) is
 * performed by the user.
 * - setPanelEnabled(boolean isEnabled): Method to enable or disable the guess
 * panel based on the specified boolean flag.
 * 
 * Author: Jackson Alexman
 * Version: Updated: 4/17/2024
>>>>>>> Stashed changes
 */
public abstract class GuessPanel extends InteractivePanel {
    /** The text field for each guess. */
    protected JComboBox<String>[] guessFields;
    /** The word bank of guessable words. */
    protected AdaptingStringList wordBank;
    /** If the word bank is being updated. */
    protected boolean updatingWordBank;
    /**
     * A display of hints based targetWord which changes everytime a guess is made.
     */
    protected HintPanel hintPanel;
    /** The maximum number of actions allowed. */
    protected final int MAX_ACTIONS;

    /**
     * Constructor to initialize a new GuessPanel instance.
     * 
     * @param targetWord      The target word to be guessed.
     * @param SWAP_THRESHOLD  The threshold for swapping.
     * @param doSwapThreshold A boolean indicating whether to swap.
     * @param MAX_ACTIONS     The maximum number of actions.
     */
    @SuppressWarnings("unchecked")
<<<<<<< Updated upstream
    public GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_GUESSES){
        super(new BorderLayout(), targetWord, SWAP_THRESHOLD, doSwapThreshold, MAX_GUESSES);
=======
    public GuessPanel(String targetWord, int SWAP_THRESHOLD, boolean doSwapThreshold, int MAX_ACTIONS) {
        super(new BorderLayout(), targetWord, SWAP_THRESHOLD, doSwapThreshold);
>>>>>>> Stashed changes
        // Initialize wordBank using an anonymous SwingWorker
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                wordBank = new AdaptingStringList(); // manages list of possible words based on user guesses.
                return null;
            }

            @Override
            protected void done() {
                /* Do Nothing */
            }
        };
        worker.execute();

        // Initialize instance variables
        this.guessFields = new JComboBox[MAX_ACTIONS];
        this.updatingWordBank = false;
        this.hintPanel = new HintPanel(this.targetWord, this.MAX_ACTIONS);
        this.setPanelDescriptors("Guess", "Guesses");
    }

<<<<<<< Updated upstream
    public void setupContentArea(){
        // Use a centerPanel so hintPanel doesn't draw above the empty Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
=======
    /**
     * Method to define the layout and behavior of the guess panel's content area.
     */
    public void setupContentArea() {
        // Add the hintPanel
        this.add(this.hintPanel, BorderLayout.NORTH);
>>>>>>> Stashed changes

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

        // Add the centerPanel
        this.add(centerPanel, BorderLayout.CENTER);

        // Setup the guessFieldPanel
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

<<<<<<< Updated upstream
        // Customize the gridBagLayout
        constraints.gridx = 0; // X position in the grid
        constraints.gridwidth = 2; // Number of cells wide
        constraints.weightx = 5.0/6.0; // 5/6 of the width
        constraints.gridy = 0; // Y position in the grid (0 because we add 1 every time so first will be 0+1=1)]
=======
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1.0 / 6.0;
        constraints.gridheight = MAX_ACTIONS;
        guessFieldPanel.add(new JPanel(), constraints);

        constraints.gridy = 0;
        constraints.gridheight = 1;
>>>>>>> Stashed changes

        for (int i = 0; i < MAX_ACTIONS; i++) {
            guessFields[i] = new JComboBox<String>();
            guessFields[i].setFont(new Font("Arial", Font.PLAIN, 20));
            guessFields[i].setVisible(false);
            guessFields[i].setEnabled(false);

            constraints.gridy++;
<<<<<<< Updated upstream
            guessFieldPanel.add(guessFields[i],constraints);
=======
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            constraints.weightx = 5.0 / 6.0;
            guessFieldPanel.add(guessFields[i], constraints);
>>>>>>> Stashed changes

            guessFields[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !updatingWordBank) {
                        updateWordBank((String) guessFields[interactionCount].getSelectedItem());
                    }
                }
            });

            guessFields[i].setUI(new MetalComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton makeGuessButton = new JButton();

                    makeGuessButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            interactionPerformed();
                        }
                    });

                    makeGuessButton.setText("Guess");
                    makeGuessButton.setVisible(true);

                    return makeGuessButton;
                }
            });

            guessFields[i].setEditable(true);
            guessFields[i].setEnabled(i == 0);

            guessFields[i].setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index,
                            isSelected, cellHasFocus);

                    if (index == 0 && !(wordBank.isPartInWordList() && wordBank.getWordList().length == 1)) {
                        rendererComponent.setText("");
                    }

                    return rendererComponent;
                }
            });

            JTextField editor = (JTextField) guessFields[i].getEditor().getEditorComponent();
            editor.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        interactionPerformed();
                    } else {
                        Timer timer = new Timer(1, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                updateWordBank(editor.getText());
                                guessFields[interactionCount].setPopupVisible(false);
                                guessFields[interactionCount].setPopupVisible(true);
                            };
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            guessFields[i].setVisible(true);
        }

        for (JComboBox<String> guessField : guessFields) {
            guessField.setPreferredSize(new Dimension(250, 30));
        }
    }

    /**
     * Parses the Word Bank to only display words containing newPart and displays
     * them based on how close the part is to the start of the word alphabetically.
     * 
     * @param newPart The String to pare the WordBank list with.
     */
    public void updateWordBank(String newPart) {
        this.updatingWordBank = true;

        this.wordBank.updateLetters(newPart);
        this.guessFields[this.interactionCount].removeAllItems();
        int wordCount = 0;
        for (String word : this.wordBank.getWordList()) {
            wordCount++;
            this.guessFields[this.interactionCount].addItem(word);
            if (wordCount >= 100) {
                break;
            }
        }

        this.updatingWordBank = false;
    }

    /**
     * Method invoked when an interaction (e.g., guess) is performed by the user.
     */
    @Override
<<<<<<< Updated upstream
    public boolean interactionPerformed(){
        // Invalid Guess
        if(!this.wordBank.isPartInWordList()){
            return false;
        }
        
        boolean wasCorrectGuess = this.targetWord.toLowerCase().equals(this.wordBank.getPart());

        // Correct Guess or The user has run out of guesses
        if(wasCorrectGuess || interactionCount+1 >= MAX_ACTIONS){ // Intentionally don't increment interactionCount to differentiate between winning on the last guess and losing on the last guess
            // Reset game
            Game.game.updateImageDifficulty(interactionCount, MAX_ACTIONS, wasCorrectGuess);
            this.setPanelEnabled(false);
            (new MenuNotification(targetWord, wasCorrectGuess, interactionCount+1)).setVisible(true); //Increment it here to display the correct number
            return false;
        }

        boolean doSwap = super.interactionPerformed();

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
        // Enable panel
        if(isEnabled){
            nextGuess();
        }
        // Disable panel
        else{
            for(int i = Math.max(interactionCount-1, 0); i < MAX_ACTIONS; i++){
                guessFields[i].setEnabled(false);
            }
        }
    }


    private void nextGuess(){
        // Update the wordbank
        updateWordBank("");
        // Enable the new guessField
        guessFields[interactionCount].setEnabled(true);
        if(interactionCount > 0){
            // Disable the previous guessField
            guessFields[interactionCount-1].setEnabled(false);
        }
        // Set the text cursor to the new guessField
=======
    public void interactionPerformed() {
        if (!this.wordBank.isPartInWordList()) {
            return;
        }

        boolean guessOutcome = this.targetWord.toLowerCase().equals(this.wordBank.getPart());

        if (guessOutcome) {
            Game.game.setupGame();
            return;
        } else {
            if (++interactionCount == MAX_ACTIONS) {
                Game.game.setupGame();
                return;
            }
        }

        this.hintPanel.checkReveal(interactionCount);

        if (swap()) {
            return;
        }

        guessFields[interactionCount].setEnabled(false);
        if (interactionCount > MAX_ACTIONS) {
            return;
        }
        wordBank.updateLetters("");
        for (String word : wordBank.getWordList()) {
            guessFields[interactionCount].addItem(word);
        }
        guessFields[interactionCount].setEnabled(true);
>>>>>>> Stashed changes
        guessFields[interactionCount].requestFocus();
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guessFields[interactionCount].setPopupVisible(true);
            };
        });
        timer.setRepeats(false);
        timer.start();
    }
<<<<<<< Updated upstream
}
=======

    /**
     * Method to enable or disable the guess panel based on the specified boolean
     * flag.
     * 
     * @param isEnabled A boolean flag indicating whether to enable or disable the
     *                  panel.
     */
    @Override
    public void setPanelEnabled(boolean isEnabled) {
        if (isEnabled) {
            for (int i = interactionCount; i < MAX_ACTIONS; i++) {
                guessFields[i].setEnabled(true);
            }

            updateWordBank("");
        } else {
            for (int i = Math.max(interactionCount - 1, 0); i < MAX_ACTIONS; i++) {
                guessFields[i].setEnabled(false);
            }
        }
    }
}
>>>>>>> Stashed changes
