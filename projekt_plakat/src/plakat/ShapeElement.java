package plakat;

import java.awt.*;
import java.awt.geom.*;

public class ShapeElement extends PosterElement {

    public enum ShappeKind { SQUARE, CIRCLE }
    private final ShappeKind kind;
    private final double size;
    private final Color fill;

    public ShapeElement(ShappeKind kind, double size, Color fill) {
        this.kind = kind;
        this.size = size;
        this.fill = fill;
        transform.translate(-size / 2.0, -size / 2.0);
    }

    public static ShapeElement fromDragString(String data) {
        String[] toks = data.split(":");
        ShappeKind kind = "circle".equals(toks[1]) ? ShappeKind.CIRCLE : ShappeKind.SQUARE;
        Color col = new Color(Integer.parseInt(toks[2]), true);
        return new ShapeElement(kind, 120, col);
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform AffineTrans = g2d.getTransform();
        g2d.transform(transform);
        g2d.setColor(fill);
        Shape s = getLocalShape();
        g2d.fill(s);
        g2d.setColor(Color.BLACK);
        g2d.draw(s);
        g2d.setTransform(AffineTrans);
    }

    @Override
    protected Shape getLocalShape() {
        return kind == ShappeKind.SQUARE ? new Rectangle2D.Double(0, 0, size, size) : new Ellipse2D.Double(0, 0, size, size);
    }
    public String getKindName() {
        return kind == ShappeKind.CIRCLE ? "circle" : "square";
    }
    public double getBaseSize() {
        return size;
    }
    public Color getFill() {
        return fill;
    }
}
