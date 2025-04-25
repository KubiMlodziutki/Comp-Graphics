package plakat;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class TextElement extends PosterElement {

    private final String text;
    private final Font font = new Font("SansSerif", Font.BOLD, 28);

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform prevAffineTrans = g2d.getTransform();
        g2d.transform(transform);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, 0);
        g2d.setTransform(prevAffineTrans);
    }

    @Override
    protected Shape getLocalShape() {
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tmp.createGraphics();
        g2d.setFont(font);
        Rectangle2D rect = g2d.getFontMetrics().getStringBounds(text, g2d);
        g2d.dispose();
        return new Rectangle2D.Double(0, -rect.getHeight(), rect.getWidth(), rect.getHeight());
    }

    public String getText() {
        return text;
    }
}
