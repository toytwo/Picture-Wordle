import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

/**
 * @author Jackson Alexman
 * @version Updated: 4/08/2024
 */
public abstract class RevealPanel extends InteractivePanel{
    protected BufferedImage image;

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image, LayoutManager layout){
        super(layout, targetWord, swapThreshold, doSwapThreshold);
        this.image = image;
    }

    public RevealPanel(String targetWord, int swapThreshold, boolean doSwapThreshold, BufferedImage image){
        super(targetWord, swapThreshold, doSwapThreshold);
        this.image = image;
    }
}
