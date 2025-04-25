package plakat;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ShapeGalleryPanel extends JPanel {

    private static final String[] TYPES = {"square", "circle"};
    private Color currentColor = Color.ORANGE;

    private final JButton colorButton;

    public ShapeGalleryPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(200, 200, 200));
        colorButton = new JButton("Kolor kształtu");
        colorButton.addActionListener(e -> {
            Color col = JColorChooser.showDialog(this, "Wybierz kolor", currentColor);
            if (col != null) {
                currentColor = col;
                refresh();
            }
        });
        add(colorButton);
        refresh();
    }

    private void refresh() {
        removeAll();
        add(colorButton);
        for (String type : TYPES) {
            add(makeLabel(type));
        }
        revalidate();
        repaint();
    }

    private JLabel makeLabel(String type) {
        int miniature = 60;
        BufferedImage icon = new BufferedImage(miniature, miniature, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        g.setColor(currentColor);
        if ("square".equals(type)) {
            g.fillRect(5, 5, miniature - 10, miniature - 10);
        }
        else {
            g.fillOval(5, 5, miniature - 10, miniature - 10);
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, miniature - 1, miniature - 1);
        g.dispose();
        JLabel label = new JLabel(new ImageIcon(icon));
        label.setTransferHandler(new TransferHandler("text") {
            @Override public int getSourceActions(JComponent comp) {
                return COPY;
            }

            @Override protected Transferable createTransferable(JComponent comp) {
                return new StringSelection("shape:" + type + ":" + currentColor.getRGB());
            }
        });
        label.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                JComponent comp = (JComponent) e.getSource();
                comp.getTransferHandler().exportAsDrag(comp, e, TransferHandler.COPY);
            }
        });
        return label;
    }
}
