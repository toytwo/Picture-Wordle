import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        SingleGuessPair s = new SingleGuessPair();
        JFrame f = new JFrame();
        f.add(s.getPanel());
        // f.pack();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        f.setUndecorated(true);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
