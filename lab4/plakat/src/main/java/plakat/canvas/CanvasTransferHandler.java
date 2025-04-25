package plakat.canvas;

import plakat.ImageElement;
import plakat.ShapeElement;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

class CanvasTransferHandler extends TransferHandler {

    private static final DataFlavor IMAGE_FLAVOR  = DataFlavor.imageFlavor;
    private static final DataFlavor STRING_FLAVOR = DataFlavor.stringFlavor;
    private final ElementStore store;
    private final PosterCanvas canvas;

    CanvasTransferHandler(ElementStore s, PosterCanvas c) {
        store = s;
        canvas = c;
    }

    @Override
    public boolean canImport(TransferSupport sup) {
        return sup.isDataFlavorSupported(IMAGE_FLAVOR) || sup.isDataFlavorSupported(STRING_FLAVOR);
    }

    @Override
    public boolean importData(TransferSupport sup) {
        if (!canImport(sup)){
            return false;
        }
        try {
            Point2D pt = sup.getDropLocation().getDropPoint();
            Transferable tr = sup.getTransferable();
            if (tr.isDataFlavorSupported(STRING_FLAVOR)) {
                String data = (String) tr.getTransferData(STRING_FLAVOR);
                if (data.startsWith("img:")) {
                    String path = data.substring(4);
                    BufferedImage img = (BufferedImage) tr.getTransferData(IMAGE_FLAVOR);
                    ImageElement el = new ImageElement(img, path);
                    el.translate(pt.getX() - img.getWidth() / 2.0,pt.getY() - img.getHeight() / 2.0);
                    store.add(el); canvas.repaintCanvas(); return true;
                }
                if (data.startsWith("shape:")) {
                    ShapeElement el = ShapeElement.fromDragString(data);
                    el.translate(pt.getX(), pt.getY());
                    store.add(el); canvas.repaintCanvas(); return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}