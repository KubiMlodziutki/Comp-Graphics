package my_vector_editor;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VectorEditorPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final List<AbstractShape> shapesList;
    private ShapeType currentShapeType;
    private Color currentColor;

    private AbstractShape shapeBeingCreated, shapeBeingMoved;

    private boolean isCreatingNewShape = false;

    private int previousMouseX, previousMouseY, shapeStartX, shapeStartY;

    public VectorEditorPanel() {
        shapesList = new ArrayList<>();
        currentShapeType = ShapeType.LINE;
        currentColor = Color.BLACK;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (AbstractShape shape : shapesList) {
            shape.drawShape(g);
        }

        if (isCreatingNewShape && shapeBeingCreated != null) {
            shapeBeingCreated.drawShape(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            for (int i = shapesList.size() - 1; i >= 0; i--) {
                AbstractShape shape = shapesList.get(i);
                if (shape.isMousePointerNear(e.getX(), e.getY())) {
                    shapesList.remove(i);
                    repaint();
                    return;
                }
            }
            return;
        }

        shapeBeingMoved = null;
        for (int i = shapesList.size() - 1; i >= 0; i--) {
            AbstractShape shape = shapesList.get(i);
            if (shape.isMousePointerNear(e.getX(), e.getY())) {
                shapeBeingMoved = shape;
                if (shape instanceof LineShape line) {
                    if (line.isNearStartPoint(e.getX(), e.getY())) {
                        line.setMovingStartPoint(true);
                    } else if (line.isNearEndPoint(e.getX(), e.getY())) {
                        line.setMovingEndPoint(true);
                    } else if (line.isNearCenterRegion(e.getX(), e.getY())) {
                        line.setMovingWholeLine(true);
                    }
                }
                break;
            }
        }
        if (shapeBeingMoved != null) {
            previousMouseX = e.getX();
            previousMouseY = e.getY();
            isCreatingNewShape = false;
        }
        else {
            isCreatingNewShape = true;
            shapeStartX = e.getX();
            shapeStartY = e.getY();
            shapeBeingCreated = createNewShape(shapeStartX, shapeStartY, shapeStartX, shapeStartY, currentColor);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (shapeBeingMoved != null) {
            if (shapeBeingMoved instanceof LineShape) {
                ((LineShape) shapeBeingMoved).resetMovingFlags();
            }
            shapeBeingMoved = null;
        }
        else if (isCreatingNewShape && shapeBeingCreated != null) {
            updateShapeSize(shapeBeingCreated, shapeStartX, shapeStartY, e.getX(), e.getY());
            shapesList.add(shapeBeingCreated);
            shapeBeingCreated = null;
            isCreatingNewShape = false;
        }
        repaint();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - previousMouseX;
        int deltaY = e.getY() - previousMouseY;
        if (shapeBeingMoved != null) {
            if (shapeBeingMoved instanceof LineShape line) {
                if (line.isMovingWholeLine()) {
                    line.moveShape(deltaX, deltaY);
                }
                else if (line.isMovingStartPoint()) {
                    line.moveStartPoint(deltaX, deltaY);
                }
                else if (line.isMovingEndPoint()) {
                    line.moveEndPoint(deltaX, deltaY);
                }
            }
            else {
                shapeBeingMoved.moveShape(deltaX, deltaY);
            }
            previousMouseX = e.getX();
            previousMouseY = e.getY();
            repaint();
        }
        else if (isCreatingNewShape && shapeBeingCreated != null) {
            updateShapeSize(shapeBeingCreated, shapeStartX, shapeStartY, e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    private AbstractShape createNewShape(int startX, int startY, int endX, int endY, Color color) {
        switch (currentShapeType) {
            case LINE:
                return new LineShape(startX, startY, endX, endY, color);
            case RECTANGLE:
                int initWidth = Math.abs(endX - startX);
                int initHeight = Math.abs(endY - startY);
                int rectX = Math.min(startX, endX);
                int rectY = Math.min(startY, endY);
                return new RectangleShape(rectX, rectY, initWidth, initHeight, color);
            case CIRCLE:
                int initialRadius = (int) AbstractShape.calculateDistance(startX, startY, endX, endY);
                return new CircleShape(startX, startY, initialRadius, color);
            default:
                return null;
        }
    }

    private void updateShapeSize(AbstractShape shape, int startX, int startY, int currentX, int currentY) {
        if (shape instanceof LineShape line) {
            line.setStartPoint(startX, startY);
            line.setEndPoint(currentX, currentY);
        }
        else if (shape instanceof RectangleShape rect) {
            int newX = Math.min(startX, currentX);
            int newY = Math.min(startY, currentY);
            int newW = Math.abs(currentX - startX);
            int newH = Math.abs(currentY - startY);
            rect.setTopLeftPoint(newX, newY);
            rect.setWidthHeight(newW, newH);
        }
        else if (shape instanceof CircleShape circle) {
            circle.setCenterPoint(startX, startY);
            int newRadius = (int) AbstractShape.calculateDistance(startX, startY, currentX, currentY);
            circle.setRadius(newRadius);
        }
    }

    public void setCurrentShapeType(ShapeType newType) {
        this.currentShapeType = newType;
    }

    public void setCurrentColor(Color newColor) {
        this.currentColor = newColor;
    }

    public List<AbstractShape> getShapesList() {
        return shapesList;
    }
}
