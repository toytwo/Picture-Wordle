import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Jackson Alexman
 * @version Created: 3/27/2024 Updated: 3/28/2024
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
    protected int guesses;
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
     * @param maxGuesses The max number of guesses that can be made. Also determines the number of guess rows. 
     * @param ratio The number of guesses per reveal.
     */
    public GuessPanel(double ratio, int maxGuesses){
        this.ratio = ratio;
        this.maxGuesses = maxGuesses;
        this.guessPanel = new JPanel(new GridBagLayout());
        this.guesses = 0;
        this.wordBank = new AdaptingLinkedStringList();

        this.guessFields = new JComboBox[maxGuesses];

        // General Constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = -1; // Y position in the grid (-1 because we add 1 every time so first will be -1+1=0)
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

        // Use empty boxes to center the textfield and have it take up 2/3 of the width of the panel
        Component leftRigidArea = Box.createRigidArea(new Dimension(0, 0));
        Component rightRigidArea = Box.createRigidArea(new Dimension(0, 0));

        for(int i = 0; i < maxGuesses; i++){
            guessFields[i] = new JComboBox<String>();
            guessFields[i].setEditable(true);
            guessFields[i].setFocusable(i==0);
              // Set a custom renderer for the combo box
              guessFields[i].setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    // Get the component from the default renderer
                    JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    // Check if the item is the one to be hidden
                    String letters = wordBank.getLetters();
                    if (letters.length() > 0 && letters.substring(0,letters.length()-1).equals(value)) {
                        // Set an empty string to hide it visually
                        rendererComponent.setText("");
                    }

                    return rendererComponent;
                }
            });
            // The Key Listener must be added to the editor component of the combobox, not the combobox itself
            Component editorComponent = guessFields[i].getEditor().getEditorComponent();
            editorComponent.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {/* Do Nothing */}
    
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        guessFields[guesses].setEnabled(false);
                        guessFields[guesses].setFocusable(false);
                        guesses = (guesses+1)%maxGuesses;
                        guessFields[guesses].setFocusable(true);
                        wordBank.reset();
                        for(String word : wordBank.getWordList()){
                            guessFields[guesses].addItem(word);
                        }
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                        wordBank.removeLastLetter();
                        guessFields[guesses].removeAllItems();
                        for(String word : wordBank.getWordList()){
                            guessFields[guesses].addItem(word);
                        }
                    }
                    else{
                        String typed = KeyEvent.getKeyText(e.getKeyCode());
                        // If the length is longer than 1, they didn't type a character
                        if(typed.length() == 1){
                            wordBank.addLetter(typed);
                            guessFields[guesses].removeAllItems();
                            for(String word : wordBank.getWordList()){
                                guessFields[guesses].addItem(word);
                            }
                        }
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

            // Create empty component for right space
            constraints.gridx = 2; // X position in the grid
            constraints.gridwidth = 1; // Number of cells wide
            constraints.weightx = 1.0 / 6.0; // 1/6 of the width
            guessPanel.add(rightRigidArea, constraints);
        }
        for(String word : wordBank.getWordList()){
            guessFields[guesses].addItem(word);
        }   
    }

    /**
     * @return The guessPanel for this GuessPanelType
     */
    public JPanel getPanel(){
        return this.guessPanel;
    }

}