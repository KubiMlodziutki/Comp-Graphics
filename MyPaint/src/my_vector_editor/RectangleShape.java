package my_vector_editor;

import java.awt.Color;
import java.awt.Graphics;

public class RectangleShape extends AbstractShape {

    private int topLeftX, topLeftY, width, height;

    public RectangleShape(int topLeftX, int topLeftY, int width, int height, Color color) {
        super(color);
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawShape(Graphics g) {
        g.setColor(shapeColor);
        g.drawRect(topLeftX, topLeftY, width, height);
    }

    public boolean isMousePointerNear(int mouseX, int mouseY) {
        int centerX = topLeftX + width / 2;
        int centerY = topLeftY + height / 2;
        int clickableWidth = (int) (width * 0.1);
        int clickableHeight = (int) (height * 0.1);
        int halfClickableWidth = clickableWidth / 2;
        int halfClickableHeight = clickableHeight / 2;

        return (mouseX >= centerX - halfClickableWidth && mouseX <= centerX + halfClickableWidth &&
                mouseY >= centerY - halfClickableHeight && mouseY <= centerY + halfClickableHeight);
    }

    @Override
    public void moveShape(int deltaX, int deltaY) {
        topLeftX += deltaX;
        topLeftY += deltaY;
    }

    @Override
    public String toFileString() {
        return String.format(
                "R %d %d %d %d %d %d %d",
                topLeftX,
                topLeftY,
                width,
                height,
                shapeColor.getRed(),
                shapeColor.getGreen(),
                shapeColor.getBlue()
        );
    }

    public void setTopLeftPoint(int x, int y) {
        this.topLeftX = x;
        this.topLeftY = y;
    }

    public void setWidthHeight(int newWidth, int newHeight) {
        this.width  = newWidth;
        this.height = newHeight;
    }
}

