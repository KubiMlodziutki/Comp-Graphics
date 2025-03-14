import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Zad2 {
    public static void main(String[] args) {
        /*
         * Sposób użycia:
         *   java Zad2 <inputImage> <pattern> [parametry] <outputFile>
         *
         * pattern = "sinus":
         *   [param] -> w
         *
         * pattern = "grid":
         *   [parametry] -> lineWidth distX distY lineR lineG lineB
         *
         * pattern = "chess":
         *   [parametry] -> squareSize
         *                 (tu można dodać kolory, ale w tym zadaniu
         *                  wystarczy, że "czarne" pola -> black,
         *                  "białe" pola -> white)
         *
         * Gdzie maska jest black -> piksel = black
         * Gdzie maska jest white -> piksel oryginalny z inputImage
         *
         * UWAGA: wszystkie wzory rysujemy symetrycznie (jak w Zad1).
         */
        if (args.length < 3) {
            System.out.println("Użycie: java Zad2 <inputImage> <pattern> [parametry] <outputFile>");
            return;
        }

        String inputFile  = args[0];
        String pattern    = args[1].toLowerCase();
        String outputFile = args[args.length - 1];

        BufferedImage inputImage;
        try {
            inputImage = ImageIO.read(new File(inputFile));
        } catch (IOException e) {
            System.out.println("Błąd wczytywania: " + e.getMessage());
            return;
        }

        int width  = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Kolory
        final int BLACK = int2RGB(0, 0, 0);
        final int WHITE = int2RGB(255, 255, 255);

        // Wybór wzorca
        switch (pattern) {
            case "sinus": {
                if (args.length < 4) {
                    System.out.println("Brak parametru w dla sinus.");
                    return;
                }
                double w = Double.parseDouble(args[2]);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getSinusMask(x, y, w, width, height, BLACK, WHITE);
                        if (maskColor == BLACK) {
                            outputImage.setRGB(x, y, BLACK);
                        } else {
                            outputImage.setRGB(x, y, inputImage.getRGB(x, y));
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
                int lineWidth = Integer.parseInt(args[2]);
                int distX     = Integer.parseInt(args[3]);
                int distY     = Integer.parseInt(args[4]);
                int lineR     = Integer.parseInt(args[5]);
                int lineG     = Integer.parseInt(args[6]);
                int lineB     = Integer.parseInt(args[7]);
                int lineColor = int2RGB(lineR, lineG, lineB);

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getGridMask(x, y, lineWidth, distX, distY, lineColor, WHITE, width, height);
                        if (maskColor == lineColor) {
                            outputImage.setRGB(x, y, lineColor);
                        } else {
                            // tu 'white' oznacza, że przepuszczamy oryginał
                            outputImage.setRGB(x, y, inputImage.getRGB(x, y));
                        }
                    }
                }
                break;
            }
            case "chess": {
                if (args.length < 3) {
                    System.out.println("Brak parametru squareSize dla chess.");
                    return;
                }
                int squareSize = Integer.parseInt(args[2]);
                // "Czarne" pola -> black, "białe" pola -> white
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int maskColor = getChessMask(x, y, squareSize, BLACK, WHITE, width, height);
                        if (maskColor == BLACK) {
                            outputImage.setRGB(x, y, BLACK);
                        } else {
                            outputImage.setRGB(x, y, inputImage.getRGB(x, y));
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
            ImageIO.write(outputImage, "bmp", new File(outputFile));
            System.out.println("Zapisano wynik do: " + outputFile);
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    // ======================= MASKI SYMETRYCZNE ===========================

    // (a) Sinus: czarny czy biały w zależności od wartości sinus
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

        // Przyjmijmy umownie: <128 -> black, >=128 -> white
        if (gray < 128) {
            return black;
        } else {
            return white;
        }
    }

    // (b) Grid: linia -> lineColor, reszta -> white
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
            return lineColor;
        } else {
            return white;
        }
    }

    // (c) Chess: "czarne" pole -> black, "białe" pole -> white
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

    // ======================= POMOCNICZE ===========================
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
