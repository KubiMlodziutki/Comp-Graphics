package my_vector_editor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;

public class EditorControlPanel extends JPanel {

    private final VectorEditorPanel editorPanel;

    private JRadioButton lineRadioButton, rectRadioButton, circleRadioButton;

    private JTextField redField, greenField, blueField;

    private JButton setColorButton;

    private JButton saveVectorButton, loadVectorButton, saveRasterButton;

    public EditorControlPanel(VectorEditorPanel editorPanel) {
        this.editorPanel = editorPanel;
        initializeComponents();
        layoutComponents();
        setupListeners();
    }

    private void initializeComponents() {
        lineRadioButton = new JRadioButton("Line");
        rectRadioButton = new JRadioButton("Rectangle");
        circleRadioButton= new JRadioButton("Circle");

        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(lineRadioButton);
        shapeGroup.add(rectRadioButton);
        shapeGroup.add(circleRadioButton);
        lineRadioButton.setSelected(true);

        redField = new JTextField("0", 3);
        greenField = new JTextField("0", 3);
        blueField = new JTextField("0", 3);
        setColorButton = new JButton("Set color");

        saveVectorButton = new JButton("Save (vector)");
        loadVectorButton = new JButton("Load (vector)");
        saveRasterButton = new JButton("Save (raster)");
    }

    private void layoutComponents() {
        add(lineRadioButton);
        add(rectRadioButton);
        add(circleRadioButton);

        add(new JLabel("R:"));
        add(redField);
        add(new JLabel("G:"));
        add(greenField);
        add(new JLabel("B:"));
        add(blueField);
        add(setColorButton);

        add(saveVectorButton);
        add(loadVectorButton);
        add(saveRasterButton);
    }

    private int limitValues(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private void setupListeners() {
        lineRadioButton.addActionListener(e -> editorPanel.setCurrentShapeType(ShapeType.LINE));
        rectRadioButton.addActionListener(e -> editorPanel.setCurrentShapeType(ShapeType.RECTANGLE));
        circleRadioButton.addActionListener(e -> editorPanel.setCurrentShapeType(ShapeType.CIRCLE));

        setColorButton.addActionListener(e -> {
            try {
                int r = Integer.parseInt(redField.getText());
                int g = Integer.parseInt(greenField.getText());
                int b = Integer.parseInt(blueField.getText());

                r = limitValues(r, 0, 255);
                redField.setText(String.valueOf(r));
                g = limitValues(g, 0, 255);
                greenField.setText(String.valueOf(g));
                b = limitValues(b, 0, 255);
                blueField.setText(String.valueOf(b));

                editorPanel.setCurrentColor(new Color(r, g, b));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EditorControlPanel.this, "Wrong RGB parameters");
            }
        });
        saveVectorButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(EditorControlPanel.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveVectorToFile(file, editorPanel.getShapesList());
            }
        });
        loadVectorButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(EditorControlPanel.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadVectorFromFile(file, editorPanel.getShapesList());
                editorPanel.repaint();
            }
        });
        saveRasterButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(EditorControlPanel.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveRasterImage(file, editorPanel);
            }
        });
    }

    private void saveVectorToFile(File file, List<AbstractShape> shapes) {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (AbstractShape shape : shapes) {
                pw.println(shape.toFileString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void loadVectorFromFile(File file, List<AbstractShape> shapes) {
        shapes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseShapeLine(line, shapes);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void parseShapeLine(String line, List<AbstractShape> shapes) {
        String[] parts = line.split("\\s+");
        if (parts.length == 0) return;

        String shapeType = parts[0].toUpperCase();
        switch (shapeType) {
            case "L":
                if (parts.length == 8) {
                    int x1 = Integer.parseInt(parts[1]);
                    int y1 = Integer.parseInt(parts[2]);
                    int x2 = Integer.parseInt(parts[3]);
                    int y2 = Integer.parseInt(parts[4]);
                    int r = Integer.parseInt(parts[5]);
                    int g = Integer.parseInt(parts[6]);
                    int b = Integer.parseInt(parts[7]);
                    shapes.add(new LineShape(x1, y1, x2, y2, new Color(r, g, b)));
                }
                break;
            case "R":
                if (parts.length == 8) {
                    int rx = Integer.parseInt(parts[1]);
                    int ry = Integer.parseInt(parts[2]);
                    int w  = Integer.parseInt(parts[3]);
                    int h  = Integer.parseInt(parts[4]);
                    int r = Integer.parseInt(parts[5]);
                    int g = Integer.parseInt(parts[6]);
                    int b = Integer.parseInt(parts[7]);
                    shapes.add(new RectangleShape(rx, ry, w, h, new Color(r, g, b)));
                }
                break;
            case "C":
                if (parts.length == 7) {
                    int cx = Integer.parseInt(parts[1]);
                    int cy = Integer.parseInt(parts[2]);
                    int rad = Integer.parseInt(parts[3]);
                    int r = Integer.parseInt(parts[4]);
                    int g = Integer.parseInt(parts[5]);
                    int b = Integer.parseInt(parts[6]);
                    shapes.add(new CircleShape(cx, cy, rad, new Color(r, g, b)));
                }
                break;
            default:
                System.out.println("Unrecognized shape type: " + line);
        }
    }
    private void saveRasterImage(File file, JPanel panel) {
        BufferedImage image = new BufferedImage(
                panel.getWidth(),
                panel.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics g = image.getGraphics();
        panel.paint(g);
        g.dispose();

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

