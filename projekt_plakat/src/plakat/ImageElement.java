package plakat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageElement extends PosterElement {

    private final BufferedImage image;
    private final String filePath;
    public ImageElement(BufferedImage img, String path) {
        image = img;
        filePath = path;
    }

    public static ImageElement fromFile(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img != null) {
                return new ImageElement(img, path);
            }
        } catch (Exception ignored) {}
        return new ImageElement(PosterIO.placeholderImage(), null);
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform prevAffineTrans = g2d.getTransform();
        g2d.transform(transform);
        g2d.drawImage(image, 0, 0, null);
        g2d.setTransform(prevAffineTrans);
    }

    @Override
    protected Rectangle2D getLocalShape() {
        return new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
    }

    public String getFilePath() {
        return filePath;
    }
}
