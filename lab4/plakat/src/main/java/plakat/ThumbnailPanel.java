package plakat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Scrollable;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailPanel extends JPanel implements Scrollable {

    private static final int THUMB_SIZE = 80;
    private static final int COLUMNS = 3;
    private static final int HORIZONTAL_GAP = 5;
    private static final int VERTICAL_GAP = 5;

    public ThumbnailPanel(File directory) {
        setLayout(new GridLayout(0, COLUMNS, HORIZONTAL_GAP, VERTICAL_GAP));
        setBackground(new Color(220, 220, 220));
        loadDirectory(directory);
    }

    public void loadDirectory(File dir) {
        if (dir == null || !dir.isDirectory()){
            return;
        }
        File[] files = dir.listFiles((d, fname) -> fname.matches(".*(?i)\\.(png|jpe?g|gif)"));
        if (files == null){
            return;
        }
        removeAll();
        for (File f : files){
            addThumb(f);
        }
        revalidate();
        repaint();
    }

    private void addThumb(File f) {
        try {
            BufferedImage full = ImageIO.read(f);
            if (full == null){
                return;
            }
            BufferedImage thumb = new BufferedImage(THUMB_SIZE, THUMB_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = thumb.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(full, 0, 0, THUMB_SIZE, THUMB_SIZE, null);
            g2d.dispose();
            JLabel label = new JLabel(new ImageIcon(thumb));
            label.setTransferHandler(new ImageHandler(full, f.getAbsolutePath()));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    comp.getTransferHandler().exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });
            add(label);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ImageHandler extends TransferHandler {
        private final BufferedImage image;
        private final String path;

        ImageHandler(BufferedImage img, String p) {
            image = img;
            path = p;
        }

        @Override
        public int getSourceActions(JComponent comp) {
            return COPY;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{ DataFlavor.imageFlavor, DataFlavor.stringFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.imageFlavor) || flavor.equals(DataFlavor.stringFlavor);
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return ImageHandler.this.getTransferDataFlavors();
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return ImageHandler.this.isDataFlavorSupported(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    if (flavor.equals(DataFlavor.imageFlavor)) {
                        return image;
                    }
                    if (flavor.equals(DataFlavor.stringFlavor)){
                        return "img:" + path;
                    }
                    return null;
                }
            };
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return THUMB_SIZE + VERTICAL_GAP;
    }
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (THUMB_SIZE + VERTICAL_GAP) * COLUMNS;
    }
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
