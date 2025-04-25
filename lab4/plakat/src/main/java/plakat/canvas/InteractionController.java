package plakat.canvas;

import plakat.PosterElement;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;

public class InteractionController implements MouseListener, MouseMotionListener {

    private static final double HANDLE_RADIUS = 8;
    private static final double ROTATION_HANDLE_OFFSET = 30;
    private enum DragMode { NONE, MOVE, SCALE, ROTATE }
    private final ElementStore store;
    private final PosterCanvas canvas;
    private DragMode currentMode = DragMode.NONE;
    private Point2D initialMousePoint;
    private AffineTransform initialTransform;
    private Point2D pivotPoint;
    private double initialDistance;
    private double initialAngle;

    public InteractionController(ElementStore store, PosterCanvas canvas) {
        this.store = store;
        this.canvas = canvas;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D mousePt = new Point2D.Double(e.getX(), e.getY());
        PosterElement posElemSelected = store.getSelected();

        if (posElemSelected != null) {
            Rectangle2D bounds = posElemSelected.getTransformedBounds();
            if (isOnMoveHandle(mousePt, bounds)) {
                startMove(posElemSelected, mousePt);
                return;
            }
            if (isOnScaleHandle(mousePt, bounds)) {
                startScale(posElemSelected, mousePt);
                return;
            }
            if (isOnRotateHandle(mousePt, bounds)) {
                startRotate(posElemSelected, mousePt);
                return;
            }
            if (posElemSelected.hit(mousePt)) {
                store.clearSelection();
                canvas.repaintCanvas();
                return;
            }
        }

        if (store.pick(mousePt)) {
            canvas.repaintCanvas();
        }
        else {
            store.clearSelection();
            canvas.repaintCanvas();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentMode = DragMode.NONE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            store.delete();
            canvas.repaintCanvas();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        PosterElement posElemSelected = store.getSelected();
        if (posElemSelected == null || currentMode == DragMode.NONE){
            return;
        }

        Point2D mousePt = new Point2D.Double(e.getX(), e.getY());
        AffineTransform newTransform;

        switch (currentMode) {
            case MOVE -> newTransform = computeMove(mousePt);
            case SCALE -> newTransform = computeScale(mousePt);
            case ROTATE -> newTransform = computeRotate(mousePt);
            default -> newTransform = initialTransform;
        }

        posElemSelected.setTransform(newTransform);
        canvas.repaintCanvas();
    }

    @Override
    public void mouseMoved (MouseEvent e) {}
    @Override
    public void mouseEntered (MouseEvent e) {}
    @Override
    public void mouseExited (MouseEvent e) {}

    private void startMove(PosterElement posElem, Point2D mousePoint) {
        currentMode = DragMode.MOVE;
        initialMousePoint = mousePoint;
        initialTransform = posElem.getTransform();
    }

    private void startScale(PosterElement posElem, Point2D mousePoint) {
        currentMode = DragMode.SCALE;
        initialMousePoint = mousePoint;
        initialTransform = posElem.getTransform();
        pivotPoint = getCenter(posElem);
        initialDistance = pivotPoint.distance(mousePoint);
    }

    private void startRotate(PosterElement posElem, Point2D mousePoint) {
        currentMode = DragMode.ROTATE;
        initialMousePoint = mousePoint;
        initialTransform = posElem.getTransform();
        pivotPoint = getCenter(posElem);
        initialAngle = Math.atan2(mousePoint.getY() - pivotPoint.getY(), mousePoint.getX() - pivotPoint.getX());
    }

    private AffineTransform computeMove(Point2D mousePoint) {
        double deltaX = mousePoint.getX() - initialMousePoint.getX();
        double deltaY = mousePoint.getY() - initialMousePoint.getY();
        AffineTransform affineTrans = new AffineTransform();
        affineTrans.translate(deltaX, deltaY);
        affineTrans.concatenate(initialTransform);
        return affineTrans;
    }

    private AffineTransform computeScale(Point2D mousePoint) {
        double distance = pivotPoint.distance(mousePoint);
        double scale = distance / Math.max(1e-6, initialDistance);
        scale = Math.max(0.05, Math.min(scale, 100));

        AffineTransform affineTrans = new AffineTransform();
        affineTrans.translate(pivotPoint.getX(), pivotPoint.getY());
        affineTrans.scale(scale, scale);
        affineTrans.translate(-pivotPoint.getX(), -pivotPoint.getY());
        affineTrans.concatenate(initialTransform);
        return affineTrans;
    }

    private AffineTransform computeRotate(Point2D mousePoint) {
        double angleNow = Math.atan2(mousePoint.getY() - pivotPoint.getY(), mousePoint.getX() - pivotPoint.getX());
        double delta = angleNow - initialAngle;

        AffineTransform affineTrans = new AffineTransform();
        affineTrans.rotate(delta, pivotPoint.getX(), pivotPoint.getY());
        affineTrans.concatenate(initialTransform);
        return affineTrans;
    }

    private Point2D getCenter(PosterElement posElem) {
        Rectangle2D bounds = posElem.getTransformedBounds();
        return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    private boolean isOnMoveHandle(Point2D p, Rectangle2D bounds) {
        Point2D center = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
        return p.distance(center) <= HANDLE_RADIUS * 2;
    }

    private boolean isOnScaleHandle(Point2D point, Rectangle2D bounds) {
        return isNear(point, bounds.getMinX(), bounds.getMinY()) || isNear(point, bounds.getMinX(), bounds.getMaxY())
                || isNear(point, bounds.getMaxX(), bounds.getMinY()) || isNear(point, bounds.getMaxX(), bounds.getMaxY());
    }

    private boolean isOnRotateHandle(Point2D point, Rectangle2D bounds) {
        double rotX = bounds.getMaxX() + ROTATION_HANDLE_OFFSET;
        double rotY = bounds.getMinY() - ROTATION_HANDLE_OFFSET;
        return isNear(point, rotX, rotY);
    }

    private boolean isNear(Point2D point, double x, double y) {
        return point.distance(x, y) <= HANDLE_RADIUS * 2;
    }
}