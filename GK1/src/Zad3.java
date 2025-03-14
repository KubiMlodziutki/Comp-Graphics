import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Zad3 {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Użycie: java Zad3 <width> <height> <patternID> <outputFile>");
            return;
        }

        int width     = Integer.parseInt(args[0]);
        int height    = Integer.parseInt(args[1]);
        int patternID = Integer.parseInt(args[2]);
        String outputFile = args[3];

        // Utworzenie pustego obrazu
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Wybór wzorca
        switch (patternID) {
            case 1:
                pattern1(image); // kropki na szarym tle
                break;
            case 2:
                pattern2(image); // romby (obrócona „szachownica”)
                break;
            case 3:
                pattern3(image); // koncentryczne okręgi
                break;
            case 4:
                pattern4(image); // powtarzające się okręgi w kafelkach
                break;
            case 5:
                pattern5(image); // promieniste pasy
                break;
            default:
                System.out.println("Nieznany patternID: " + patternID + " (dozwolone 1..5).");
                return;
        }

        // Zapis obrazu do pliku
        try {
            ImageIO.write(image, "bmp", new File(outputFile));
            System.out.println("Zapisano do pliku: " + outputFile);
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    // Proste kolory
    private static final int BLACK = int2RGB(0, 0, 0);
    private static final int WHITE = int2RGB(255, 255, 255);
    private static final int GRAY  = int2RGB(128, 128, 128);

    /**
     * pattern1: kropki (czarne) na szarym tle (polka dots)
     */
    private static void pattern1(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int tileSize    = 80;  // rozmiar kafelka
        int circleRadius= 30;  // promień kropki

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Współrzędne w obrębie kafelka
                int localX = x % tileSize;
                int localY = y % tileSize;
                // Środek kafelka
                int cx = tileSize / 2;
                int cy = tileSize / 2;

                double dist = Math.sqrt((localX - cx)*(localX - cx) + (localY - cy)*(localY - cy));
                if (dist <= circleRadius) {
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, GRAY);
                }
            }
        }
    }

    /**
     * pattern2: romby (rotated chess-like)
     */
    private static void pattern2(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int diamondSize = 50;  // rozmiar „pola”

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Rotacja współrzędnych: (x+y) i (x-y)
                double rowIndex = Math.floor((x + y) / (double)diamondSize);
                double colIndex = Math.floor((x - y) / (double)diamondSize);

                // Naprzemienne kolory
                if (((int)rowIndex + (int)colIndex) % 2 == 0) {
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, WHITE);
                }
            }
        }
    }

    /**
     * pattern3: koncentryczne okręgi (czarno-białe)
     */
    private static void pattern3(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int cx = w / 2;
        int cy = h / 2;
        int ringWidth = 10;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double d  = Math.sqrt(dx*dx + dy*dy);
                int ringIndex = (int)(d / ringWidth);
                if (ringIndex % 2 == 0) {
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, WHITE);
                }
            }
        }
    }
    /**
     * pattern4: powtarzające się okręgi (w kafelkach)
     */
    private static void pattern4(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int tileSize  = 50;
        int ringWidth = 5;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int localX = x % tileSize;
                int localY = y % tileSize;
                int cx = tileSize / 2;
                int cy = tileSize / 2;

                double dx = localX - cx;
                double dy = localY - cy;
                double d  = Math.sqrt(dx*dx + dy*dy);
                int ringIndex = (int)(d / ringWidth);
                if (ringIndex % 2 == 0) {
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, WHITE);
                }
            }
        }
    }
    /**
     * pattern5: promieniste pasy (radial stripes)
     */
    private static void pattern5(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int cx = w / 2;
        int cy = h / 2;
        int stripesCount = 16; // liczba pasów

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double angle = Math.atan2(dy, dx); // -pi..pi
                if (angle < 0) angle += 2*Math.PI;

                double sector = 2*Math.PI / stripesCount;
                int sectorIndex = (int)(angle / sector);
                if (sectorIndex % 2 == 0) {
                    image.setRGB(x, y, BLACK);
                } else {
                    image.setRGB(x, y, WHITE);
                }
            }
        }
    }



    // Prosta metoda składania koloru RGB w int
    private static int int2RGB(int r, int g, int b) {
        r = r & 0xFF;
        g = g & 0xFF;
        b = b & 0xFF;
        return (r << 16) | (g << 8) | b;
    }
}
