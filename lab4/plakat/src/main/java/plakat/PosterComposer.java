package plakat;

import plakat.canvas.PosterCanvas;
import javax.swing.*;
import java.awt.*;

public class PosterComposer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PosterComposer::initGUI);
    }

    private static void initGUI() {
        JFrame frame = new JFrame("Kreator plakatów");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ThumbnailPanel thumbs = new ThumbnailPanel(new java.io.File("images"));
        ShapeGalleryPanel shapes = new ShapeGalleryPanel();
        PosterCanvas canvas = new PosterCanvas();
        ControlPanel ctrlPan = new ControlPanel(canvas, thumbs);

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(thumbs), BorderLayout.CENTER);
        left.add(shapes, BorderLayout.SOUTH);

        frame.add(left, BorderLayout.WEST);
        frame.add(new JScrollPane(canvas), BorderLayout.CENTER);
        frame.add(ctrlPan, BorderLayout.SOUTH);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
