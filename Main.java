import javax.swing.JFrame;

/**
 * @author Jackson Alexman
 * @version Updated 4/3/2024
 */

public class Main {

    public static JFrame f;
    public static void main(String[] args) {
        f = new Game(0, 0, 0);
    }

    public static void playAgain(){
        f.dispose();
        f = new Game(0, 0, 0);
    }
}
