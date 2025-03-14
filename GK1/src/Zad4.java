import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Zad4 {
    public static void main(String[] args) {
        /*
         * Sposób użycia:
         *   java Zad4 <inputImage1> <inputImage2> <pattern> [parametry] <outputFile>
         *
         * Wzorce jak wcześniej (sinus, grid, chess) - symetryczne.
         * Jeśli maska = biały -> piksel z pierwszego obrazu.
         * Jeśli maska = czarny -> piksel z drugiego obrazu.
         */
        if (args.length < 4) {
            System.out.println("Użycie: java Zad4 <img1> <img2> <pattern> [parametry] <outputFile>");
            return;
        }

        String file1 = args[0];
        String file2 = args[1];
        String pattern = args[2].toLowerCase();
        String outputFile = args[args.length - 1];

        BufferedImage img1 = null, img2 = null;
        try {
            img1 = ImageIO.read(new File(file1));
            img2 = ImageIO.read(new File(file2));
        } catch (IOException e) {
            System.out.println("Błąd wczytywania obrazów: " + e.getMessage());
            return;
        }

        // Zakładamy, że obrazy mają tę samą rozdzielczość
        int width  = img1.getWidth();
        int height = img1.getHeight();
        if (img2.getWidth() != width || img2.getHeight() != height) {
            System.out.println("Obrazy muszą mieć tę samą rozdzielczość!");
            return;
        }

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        final int BLACK = int2RGB(0, 0, 0);
        final int WHITE = int2RGB(255, 255, 255);

        switch (pattern) {
            case "sinus": {
                if (args.length < 5) {
                    System.out.println("Brak parametru w dla sinus.");
                    return;
                }
                double w = Double.parseDouble(args[3]);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getSinusMask(x, y, w, width, height, BLACK, WHITE);
                        if (maskColor == WHITE) {
                            output.setRGB(x, y, img1.getRGB(x, y));
                        } else {
                            output.setRGB(x, y, img2.getRGB(x, y));
                        }
                    }
                }
                break;
            }
            case "grid": {
                if (args.length < 8) {
                    System.out.println("Brak parametrów dla grid.");
                    return;
                }
                int lineWidth = Integer.parseInt(args[3]);
                int distX     = Integer.parseInt(args[4]);
                int distY     = Integer.parseInt(args[5]);
                int lineR     = Integer.parseInt(args[6]);
                int lineG     = Integer.parseInt(args[7]);
                int lineB     = Integer.parseInt(args[8]);
                int lineColor = int2RGB(lineR, lineG, lineB);

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getGridMask(x, y, lineWidth, distX, distY, lineColor, WHITE, width, height);
                        if (maskColor == WHITE) {
                            output.setRGB(x, y, img1.getRGB(x, y));
                        } else {
                            output.setRGB(x, y, img2.getRGB(x, y));
                        }
                    }
                }
                break;
            }
            case "chess": {
                if (args.length < 4) {
                    System.out.println("Brak parametru squareSize dla chess.");
                    return;
                }
                int squareSize = Integer.parseInt(args[3]);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getChessMask(x, y, squareSize, BLACK, WHITE, width, height);
                        if (maskColor == WHITE) {
                            output.setRGB(x, y, img1.getRGB(x, y));
                        } else {
                            output.setRGB(x, y, img2.getRGB(x, y));
                        }
                    }
                }
                break;
            }
            default:
                System.out.println("Nieznany pattern: " + pattern);
                return;
        }

        // Zapis
        try {
            ImageIO.write(output, "bmp", new File(outputFile));
            System.out.println("Zapisano do pliku: " + outputFile);
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    // --------------------- Te same funkcje maskujące (symetryczne) ---------------------

    private static int getSinusMask(int x, int y, double w,
                                    int width, int height,
                                    int black, int white) {
        int cx = width / 2;
        int cy = height / 2;
        double dx = x - cx;
        double dy = y - cy;
        double d = Math.sqrt(dx*dx + dy*dy);

        double val = 128.0 * (Math.sin((d / w) * Math.PI) + 1.0);
        int gray = (int)Math.round(val);

        // <128 -> black, >=128 -> white
        if (gray < 128) {
            return black;
        } else {
            return white;
        }
    }

    private static int getGridMask(int x, int y,
                                   int lineWidth, int distX, int distY,
                                   int lineColor, int white,
                                   int width, int height) {
        int cx = width / 2;
        int cy = height / 2;

        double offX = x - cx;
        double offY = y - cy;

        double modX = modPos(offX, distX);
        double modY = modPos(offY, distY);

        if (modX < lineWidth || modY < lineWidth) {
            return lineColor; // "czarny" w sensie maski
        } else {
            return white;     // "biały" w sensie maski
        }
    }

    private static int getChessMask(int x, int y,
                                    int squareSize,
                                    int black, int white,
                                    int width, int height) {
        int cx = width / 2;
        int cy = height / 2;

        double offX = x - (cx - squareSize/2.0);
        double offY = y - (cy - squareSize/2.0);

        int colIndex = (int)Math.floor(offX / squareSize);
        int rowIndex = (int)Math.floor(offY / squareSize);

        if ((colIndex + rowIndex) % 2 == 0) {
            return black;
        } else {
            return white;
        }
    }

    private static double modPos(double val, int base) {
        double m = val % base;
        if (m < 0) m += base;
        return m;
    }

    private static int int2RGB(int r, int g, int b) {
        r = r & 0xFF;
        g = g & 0xFF;
        b = b & 0xFF;
        return (r << 16) | (g << 8) | b;
    }
}
