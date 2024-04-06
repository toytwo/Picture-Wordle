<<<<<<< Updated upstream
=======
import javax.swing.JFrame;

>>>>>>> Stashed changes
/**
 * @author Jackson Alexman
 * @version Updated 4/3/2024
 */

public class Main {
<<<<<<< Updated upstream
    public static void main(String[] args) {
        new Game(0, 0, 0);
=======

    public static JFrame f;
    public static void main(String[] args) {
        f = new Game(0, 0, 0);
    }

    public static void playAgain(){
        f.dispose();
        f = new Game(0, 0, 0);
>>>>>>> Stashed changes
    }
}
