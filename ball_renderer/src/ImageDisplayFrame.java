import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageDisplayFrame extends JFrame {

    public ImageDisplayFrame(BufferedImage imageToDisplay) {
        setTitle("Rendered Sphere");
        setSize(imageToDisplay.getWidth(), imageToDisplay.getHeight());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JPanel() {
            protected void paintComponent(Graphics graphicsContext) {
                graphicsContext.drawImage(imageToDisplay, 0, 0, null);
            }
        });
    }
}
