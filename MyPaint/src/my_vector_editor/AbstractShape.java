package my_vector_editor;

import java.awt.Color;
import java.awt.Graphics;

public abstract class AbstractShape {

    protected Color shapeColor;

    public AbstractShape(Color color) {
        this.shapeColor = color;
    }

    public abstract void drawShape(Graphics g);

    public abstract boolean isMousePointerNear(int mouseX, int mouseY);

    public abstract void moveShape(int deltaX, int deltaY);

    public abstract String toFileString();

    protected static double calculateDistance(int x1, int y1, int x2, int y2) {
        double dx = (double) x1 - x2;
        double dy = (double) y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
