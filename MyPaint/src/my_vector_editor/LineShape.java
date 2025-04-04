package my_vector_editor;

import java.awt.Color;
import java.awt.Graphics;

public class LineShape extends AbstractShape {

    private int startX, startY, endX, endY;

    private boolean movingStartPoint = false;
    private boolean movingEndPoint = false;
    private boolean movingWholeLine = false;

    public LineShape(int startX, int startY, int endX, int endY, Color color) {
        super(color);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public boolean isNearStartPoint(int mouseX, int mouseY) {
        int endpointThreshold = 8;
        return AbstractShape.calculateDistance(mouseX, mouseY, startX, startY) <= endpointThreshold;
    }

    public void setStartPoint(int newStartX, int newStartY) {
        this.startX = newStartX;
        this.startY = newStartY;
    }

    public void setEndPoint(int newEndX, int newEndY) {
        this.endX = newEndX;
        this.endY = newEndY;
    }

    public boolean isNearEndPoint(int mouseX, int mouseY) {
        int endpointThreshold = 8;
        return AbstractShape.calculateDistance(mouseX, mouseY, endX, endY) <= endpointThreshold;
    }

    public boolean isNearCenterRegion(int mouseX, int mouseY) {
        int centerX = (startX + endX) / 2;
        int centerY = (startY + endY) / 2;
        double lineLength = AbstractShape.calculateDistance(startX, startY, endX, endY);
        double halfSquareSide = (lineLength * 0.05) / 2.0;
        return (mouseX >= centerX - halfSquareSide && mouseX <= centerX + halfSquareSide &&
                mouseY >= centerY - halfSquareSide && mouseY <= centerY + halfSquareSide);
    }

    @Override
    public boolean isMousePointerNear(int mouseX, int mouseY) {
        return isNearStartPoint(mouseX, mouseY) ||
                isNearEndPoint(mouseX, mouseY) ||
                isNearCenterRegion(mouseX, mouseY);
    }

    @Override
    public void moveShape(int deltaX, int deltaY) {
        this.startX += deltaX;
        this.startY += deltaY;
        this.endX   += deltaX;
        this.endY   += deltaY;
    }

    public void moveStartPoint(int deltaX, int deltaY) {
        this.startX += deltaX;
        this.startY += deltaY;
    }

    public void moveEndPoint(int deltaX, int deltaY) {
        this.endX += deltaX;
        this.endY += deltaY;
    }

    public void resetMovingFlags() {
        movingStartPoint = false;
        movingEndPoint = false;
        movingWholeLine = false;
    }

    public void setMovingStartPoint(boolean flag) {
        movingStartPoint = flag;
    }

    public void setMovingEndPoint(boolean flag) {
        movingEndPoint = flag;
    }

    public void setMovingWholeLine(boolean flag) {
        movingWholeLine = flag;
    }

    public boolean isMovingStartPoint() { return movingStartPoint; }
    public boolean isMovingEndPoint() { return movingEndPoint; }
    public boolean isMovingWholeLine() { return movingWholeLine; }

    @Override
    public void drawShape(Graphics g) {
        g.setColor(shapeColor);
        g.drawLine(startX, startY, endX, endY);
    }

    @Override
    public String toFileString() {
        return String.format("L %d %d %d %d %d %d %d",
                startX,
                startY,
                endX,
                endY,
                shapeColor.getRed(),
                shapeColor.getGreen(),
                shapeColor.getBlue());
    }
}