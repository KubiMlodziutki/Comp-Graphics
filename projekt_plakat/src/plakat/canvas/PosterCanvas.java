package plakat.canvas;

import plakat.PosterElement;
import plakat.PosterIO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.List;

public class PosterCanvas extends JPanel {
    private final ElementStore store = new ElementStore();
    private final InteractionController controller = new InteractionController(store, this);
    private final CanvasTransferHandler transferHandler = new CanvasTransferHandler(store, this);
    private static final double HANDLE_RADIUS = 8;
    private static final double ROTATION_HANDLE_OFFSET = 30;

    public PosterCanvas() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(controller);
        addMouseMotionListener(controller);
        setTransferHandler(transferHandler);
    }

    public void addElement(PosterElement posElem) {
        store.add(posElem);
        repaint();
    }

    public void nudgeSelected(double deltaX, double deltaY) {
        PosterElement posElemSelected = store.getSelected();
        if (posElemSelected == null) return;
        AffineTransform affineTrans = new AffineTransform();
        affineTrans.translate(deltaX, deltaY);
        affineTrans.concatenate(posElemSelected.getTransform());
        posElemSelected.setTransform(affineTrans);
        repaint();
    }

    public void rotateSelected(double degrees) {
        PosterElement posElemSelected = store.getSelected();
        if (posElemSelected == null) return;
        Rectangle2D bounds = posElemSelected.getTransformedBounds();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();
        AffineTransform affineTrans = new AffineTransform();
        affineTrans.rotate(Math.toRadians(degrees), centerX, centerY);
        affineTrans.concatenate(posElemSelected.getTransform());
        posElemSelected.setTransform(affineTrans);
        repaint();
    }

    public void bringSelectedForward() {
        store.toFront();
        repaint();
    }
    public void sendSelectedBackward() {
        store.toBack();
        repaint();
    }

    public void savePoster(File file) {
        PosterIO.saveSafe(file, store.all());
    }

    public void exportToImage(File file, int width, int height) {
        CanvasUtil.export(this, file, width, height);
    }

    public void setElements(List<PosterElement> elements) {
        store.setAll(elements);
        repaint();
    }

    void repaintCanvas() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        paintGrid(g2d);
        for (PosterElement posElem : store.all()) {
            posElem.paint(g2d);
        }
        PosterElement posElemSelected = store.getSelected();
        if (posElemSelected != null){
            drawHandles(g2d, posElemSelected);
        }
        g2d.dispose();
    }

    private void paintGrid(Graphics2D g2d) {
        int cell = 20;
        for (int y = 0; y < getHeight(); y += cell) {
            for (int x = 0; x < getWidth(); x += cell) {
                boolean light = ((x / cell) + (y / cell)) % 2 == 0;
                g2d.setColor(light ? new Color(240,240,240) : Color.WHITE);
                g2d.fillRect(x, y, cell, cell);
            }
        }
    }

    private void drawHandles(Graphics2D g2d, PosterElement posElem) {
        Rectangle2D bounds = posElem.getTransformedBounds();
        drawHandle(g2d, bounds.getCenterX(), bounds.getCenterY(), Color.BLUE);
        double[] leftRight = { bounds.getMinX(), bounds.getMaxX() };
        double[] upperLower = { bounds.getMinY(), bounds.getMaxY() };
        for (double x : leftRight) {
            for (double y : upperLower) {
                drawHandle(g2d, x, y, Color.RED);
            }
        }
        drawHandle(g2d, bounds.getMaxX() + ROTATION_HANDLE_OFFSET,bounds.getMinY() - ROTATION_HANDLE_OFFSET, Color.MAGENTA);
    }

    private void drawHandle(Graphics2D g2d, double x, double y, Color color) {
        g2d.setColor(color);
        g2d.fill(new Rectangle2D.Double(x - HANDLE_RADIUS,y - HANDLE_RADIUS,HANDLE_RADIUS * 2,HANDLE_RADIUS * 2));
    }
}
