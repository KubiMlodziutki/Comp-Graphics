package plakat.canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public final class CanvasUtil {

    private CanvasUtil() {}

    public static void export(Component comp, File file, int wid, int hei) {
        BufferedImage img = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.scale(wid / (double) comp.getWidth(),hei / (double) comp.getHeight());
        comp.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(img, "PNG", file);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex, "Błąd zapisu PNG", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
