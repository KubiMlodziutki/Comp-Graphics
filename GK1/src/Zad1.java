import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Zad1 {
    public static void main(String[] args) {
        /*
         * Sposób użycia:
         *   java Zad1 <width> <height> <pattern> [parametry] <outputFile>
         *
         * pattern = "sinus":
         *   [param] -> w (double/int)
         *   Przykład: java Zad1 800 600 sinus 20 pierscienie.bmp
         *
         * pattern = "grid":
         *   [parametry] -> lineWidth distX distY lineR lineG lineB bgR bgG bgB
         *   Przykład: java Zad1 800 600 grid 2 50 50 255 0 0 255 255 255 siatka.bmp
         *
         * pattern = "chess":
         *   [parametry] -> squareSize r1 g1 b1 r2 g2 b2
         *   Przykład: java Zad1 400 400 chess 50 255 255 255 0 0 0 szachownica.bmp
         *
         * Wzory "grid" i "chess" są przesunięte tak, aby były symetryczne względem środka obrazu.
         */
        if (args.length < 3) {
            System.out.println("Użycie: java Zad1 <width> <height> <pattern> [parametry] <outputFile>");
            return;
        }

        int width  = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        String pattern = args[2].toLowerCase();
        String outputFile = args[args.length - 1];

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        switch (pattern) {
            case "sinus": {
                if (args.length < 5) {
                    System.out.println("Brak parametru w dla sinus.");
                    return;
                }
                double w = Double.parseDouble(args[3]);
                drawSinusRings(image, w);
                break;
            }
            case "grid": {
                if (args.length < 12) {
                    System.out.println("Brak parametrów dla grid.");
                    return;
                }
                int lineWidth = Integer.parseInt(args[3]);
                int distX     = Integer.parseInt(args[4]);
                int distY     = Integer.parseInt(args[5]);
                int lineR     = Integer.parseInt(args[6]);
                int lineG     = Integer.parseInt(args[7]);
                int lineB     = Integer.parseInt(args[8]);
                int bgR       = Integer.parseInt(args[9]);
                int bgG       = Integer.parseInt(args[10]);
                int bgB       = Integer.parseInt(args[11]);
                drawSymGrid(image, lineWidth, distX, distY,
                        int2RGB(lineR, lineG, lineB),
                        int2RGB(bgR, bgG, bgB));
                break;
            }
            case "chess": {
                if (args.length < 11) {
                    System.out.println("Brak parametrów dla chess.");
                    return;
                }
                int squareSize = Integer.parseInt(args[3]);
                int r1 = Integer.parseInt(args[4]);
                int g1 = Integer.parseInt(args[5]);
                int b1 = Integer.parseInt(args[6]);
                int r2 = Integer.parseInt(args[7]);
                int g2 = Integer.parseInt(args[8]);
                int b2 = Integer.parseInt(args[9]);
                drawSymChess(image, squareSize,
                        int2RGB(r1, g1, b1),
                        int2RGB(r2, g2, b2));
                break;
            }
            default:
                System.out.println("Nieznany pattern: " + pattern);
                return;
        }

        // Zapis
        try {
            ImageIO.write(image, "bmp", new File(outputFile));
            System.out.println("Zapisano obraz do pliku: " + outputFile);
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    // (a) Rozmyte pierścienie (sinus) - już z natury symetryczne wokół środka
    private static void drawSinusRings(BufferedImage image, double w) {
        int width = image.getWidth();
        int height = image.getHeight();
        int cx = width / 2;
        int cy = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double d = Math.sqrt(dx*dx + dy*dy);

                double val = 128.0 * (Math.sin((d / w) * Math.PI) + 1.0);
                int gray = (int)Math.round(val);
                if (gray < 0)   gray = 0;
                if (gray > 255) gray = 255;

                int color = int2RGB(gray, gray, gray);
                image.setRGB(x, y, color);
            }
        }
    }

    // (b) Regularna siatka kolorów - symetryczna względem środka
    private static void drawSymGrid(BufferedImage image,
                                    int lineWidth, int distX, int distY,
                                    int lineColor, int bgColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int cx = width / 2;
        int cy = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Liczymy offset względem środka
                double offX = x - cx;
                double offY = y - cy;

                // Dodatnia wersja modulo
                double modX = modPos(offX, distX);
                double modY = modPos(offY, distY);

                if (modX < lineWidth || modY < lineWidth) {
                    image.setRGB(x, y, lineColor);
                } else {
                    image.setRGB(x, y, bgColor);
                }
            }
        }
    }

    // (c) Szachownica - symetryczna względem środka (środek obrazu = róg 4 pól)
    private static void drawSymChess(BufferedImage image,
                                     int squareSize,
                                     int color1, int color2) {
        int width = image.getWidth();
        int height = image.getHeight();
        int cx = width / 2;
        int cy = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double offX = x - (cx - squareSize/2.0);
                double offY = y - (cy - squareSize/2.0);

                int colIndex = (int)Math.floor(offX / squareSize);
                int rowIndex = (int)Math.floor(offY / squareSize);

                if ((colIndex + rowIndex) % 2 == 0) {
                    image.setRGB(x, y, color1);
                } else {
                    image.setRGB(x, y, color2);
                }
            }
        }
    }

    // Dodatnia wersja modulo (dla liczb ujemnych)
    private static double modPos(double val, int base) {
        double m = val % base;
        if (m < 0) {
            m += base;
        }
        return m;
    }

    // Składanie koloru
    private static int int2RGB(int r, int g, int b) {
        r = r & 0xFF;
        g = g & 0xFF;
        b = b & 0xFF;
        return (r << 16) | (g << 8) | b;
    }
}
