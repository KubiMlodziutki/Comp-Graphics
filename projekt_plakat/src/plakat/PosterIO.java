package plakat;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PosterIO {

    private static final String TAB = "\t";

    public static void save(File file, List<PosterElement> list) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (PosterElement posElem : list) {
                if (posElem instanceof ImageElement imgElem && imgElem.getFilePath() != null) {
                    out.print("img" + TAB + imgElem.getFilePath() + TAB);
                }
                else if (posElem instanceof ShapeElement s) {
                    out.print("shape" + TAB + s.getKindName() + TAB + s.getBaseSize() + TAB + s.getFill().getRGB() + TAB);
                }
                else if (posElem instanceof TextElement t) {
                    out.print("text" + TAB + t.getText() + TAB);
                }
                else {
                    continue;
                }

                double[] matr = new double[6];
                posElem.getTransform().getMatrix(matr);
                for (double val : matr) {
                    out.print(val + TAB);
                }
                out.println();
            }
        }
    }
    public static void saveSafe(File f, List<PosterElement> list) {
        try {
            save(f, list);
        }
        catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex,"Błąd zapisu", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<PosterElement> load(File f) throws IOException {
        List<PosterElement> result = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(line, TAB);
                if (!tok.hasMoreTokens()) {
                    continue;
                }

                String kindTok = tok.nextToken();
                PosterElement posElem = switch (kindTok) {
                    case "img" -> ImageElement.fromFile(tok.nextToken());
                    case "shape" -> {
                        ShapeElement.ShappeKind kind = "circle".equals(tok.nextToken()) ? ShapeElement.ShappeKind.CIRCLE : ShapeElement.ShappeKind.SQUARE;
                        double size = Double.parseDouble(tok.nextToken());
                        Color col = new Color(Integer.parseInt(tok.nextToken()), true);
                        yield new ShapeElement(kind, size, col);
                    }
                    case "text" -> new TextElement(tok.nextToken());
                    default -> null;
                };
                if (posElem == null) continue;

                double[] matr = new double[6];
                for (int i = 0; i < 6 && tok.hasMoreTokens(); i++) {
                    matr[i] = Double.parseDouble(tok.nextToken());
                }

                posElem.setTransform(new AffineTransform(matr));
                result.add(posElem);
            }
        }
        return result;
    }

    public static BufferedImage placeholderImage() {
        BufferedImage img = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, 120, 120);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 119, 119);
        g2d.drawString("IMG", 45, 65);
        g2d.dispose();
        return img;
    }
}
