package plakat;

import plakat.canvas.PosterCanvas;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final PosterCanvas canvas;
    private final ThumbnailPanel thumbs;

    public ControlPanel(PosterCanvas can, ThumbnailPanel thum) {
        canvas = can;
        thumbs = thum;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        initButtons();
    }

    private void initButtons() {
        add(btn("←", () -> canvas.nudgeSelected(-1, 0)));
        add(btn("→", () -> canvas.nudgeSelected( 1, 0)));
        add(btn("↑", () -> canvas.nudgeSelected( 0,-1)));
        add(btn("↓", () -> canvas.nudgeSelected( 0, 1)));
        add(btn("⟲", () -> canvas.rotateSelected(-1)));
        add(btn("⟳", () -> canvas.rotateSelected( 1)));
        add(btn("Na wierzch", canvas::bringSelectedForward));
        add(btn("Na spód", canvas::sendSelectedBackward));
        add(btn("Dodaj tekst", this::addText));
        add(btn("Importuj katalog", this::importDir));
        add(btn("Zapisz",  this::save));
        add(btn("Wczytaj", this::load));
        add(btn("Eksport PNG", this::exportPng));
    }

    private JButton btn(String txt, Runnable action) {
        JButton button = new JButton(txt);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void addText() {
        String txt = JOptionPane.showInputDialog(this, "Wpisz tekst:");
        if (txt != null && !txt.isBlank()) {
            TextElement te = new TextElement(txt.trim());
            te.translate(100, 100);
            canvas.addElement(te);
        }
    }

    private void importDir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            thumbs.loadDirectory(chooser.getSelectedFile());
        }
    }

    private void save() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            canvas.savePoster(chooser.getSelectedFile());
        }
    }

    private void load() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            var list = PosterIO.load(chooser.getSelectedFile());
            canvas.setElements(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Błąd odczytu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportPng() {
        JTextField widField = new JTextField(5), heiField = new JTextField(5);
        JCheckBox custom = new JCheckBox("Własna rozdzielczość");
        widField.setEnabled(false);
        heiField.setEnabled(false);
        custom.addActionListener(e -> {
            boolean en = custom.isSelected();
            widField.setEnabled(en);
            heiField.setEnabled(en);
        });

        JPanel panel = new JPanel();
        panel.add(custom);
        panel.add(new JLabel("Szer:")); panel.add(widField);
        panel.add(new JLabel("Wys:")); panel.add(heiField);

        if (JOptionPane.showConfirmDialog(this,panel,"Eksport PNG",JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }

        int wid = canvas.getWidth(), hei = canvas.getHeight();
        if (custom.isSelected()) {
            try {
                wid = Integer.parseInt(widField.getText().trim());
                hei = Integer.parseInt(heiField.getText().trim());
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Niepoprawne liczby","Błąd",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JFileChooser choser = new JFileChooser();
        if (choser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            canvas.exportToImage(choser.getSelectedFile(), wid, hei);
        }
    }
}
