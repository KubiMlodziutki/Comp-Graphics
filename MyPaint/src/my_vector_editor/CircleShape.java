package my_vector_editor;

import java.awt.Color;
import java.awt.Graphics;

public class CircleShape extends AbstractShape {

    private int centerX, centerY;
    private int radius;

    public CircleShape(int centerX, int centerY, int radius, Color color) {
        super(color);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius  = radius;
    }

    @Override
    public void drawShape(Graphics g) {
        g.setColor(shapeColor);
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    @Override
    public boolean isMousePointerNear(int mouseX, int mouseY) {
        double distanceToCenter = calculateDistance(mouseX, mouseY, centerX, centerY);
        double centerMoveRadius = radius * 0.1;
        return (distanceToCenter <= centerMoveRadius);
    }

    @Override
    public void moveShape(int deltaX, int deltaY) {
        centerX += deltaX;
        centerY += deltaY;
    }

    @Override
    public String toFileString() {
        return String.format(
                "C %d %d %d %d %d %d",
                centerX,
                centerY,
                radius,
                shapeColor.getRed(),
                shapeColor.getGreen(),
                shapeColor.getBlue()
        );
    }

    public void setCenterPoint(int x, int y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void setRadius(int newRadius) {
        this.radius = newRadius;
    }
}

