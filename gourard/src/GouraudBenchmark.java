import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.util.Random;

public class GouraudBenchmark extends JPanel {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int MANUAL_PHASE_COUNT = 3;
    private static final int MANUAL_PHASE_DURATION_MS = 3000;
    private static final int BENCHMARK_TRIANGLE_COUNT = 1000;
    private static final int BENCHMARK_PHASE_DURATION_MS = 2000;

    private final Triangle2D[] manualTriangles = new Triangle2D[] {
            new Triangle2D(200, 100, Color.RED,   100, 300, Color.GREEN, 300, 300, Color.BLUE),   // równoboczny
            new Triangle2D(400, 100, Color.WHITE, 400, 300, Color.BLACK, 600, 300, Color.GRAY),  // prostokątny
            new Triangle2D(100, 400, Color.ORANGE, 500, 400, Color.MAGENTA, 300, 450, Color.CYAN) // płaski
    };

    private final BufferedImage bufferImage = new BufferedImage(
            CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB
    );
    private final BufferedImage directImage = new BufferedImage(
            CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB
    );

    private final Random random = new Random();
    private final long startTime;
    private long lastBufferedCycle = -1;
    private long lastDirectCycle = -1;

    public GouraudBenchmark() {
        this.startTime = System.currentTimeMillis();
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.WHITE);
        Timer repaintTimer = new Timer(16, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;
        long manualTotalTime = MANUAL_PHASE_COUNT * MANUAL_PHASE_DURATION_MS;

        if (elapsed < manualTotalTime) {
            int index = (int)(elapsed / MANUAL_PHASE_DURATION_MS);
            manualTriangles[index].shadeToScreen(g);
            return;
        }

        long benchElapsed = elapsed - manualTotalTime;
        long phaseDuration = BENCHMARK_PHASE_DURATION_MS;
        long cycleDuration = phaseDuration * 2;
        long cycleCount = benchElapsed / cycleDuration;
        long posInCycle = benchElapsed % cycleDuration;

        if (posInCycle < phaseDuration) {
            if (cycleCount != lastDirectCycle) {
                regenerateDirectPhase();
                lastDirectCycle = cycleCount;
            }
            g.drawImage(directImage, 0, 0, null);
        } else {
            if (cycleCount != lastBufferedCycle) {
                regenerateBufferedPhase();
                lastBufferedCycle = cycleCount;
            }
            g.drawImage(bufferImage, 0, 0, null);
        }
    }

    private void regenerateBufferedPhase() {
        Graphics2D g2d = bufferImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        g2d.setComposite(AlphaComposite.SrcOver);
        long startNano = System.nanoTime();
        long estimatedPixels = 0;
        for (int i = 0; i < BENCHMARK_TRIANGLE_COUNT; i++) {
            Triangle2D triang = createRandomTriangle();
            triang.shadeToImage(bufferImage);
            estimatedPixels += (CANVAS_WIDTH * CANVAS_HEIGHT) / 100;
        }
        long endNano = System.nanoTime();
        g2d.dispose();
        double secs = (endNano - startNano) / 1e9;
        System.out.printf(
                "Buffered: %,d triangles in %.3f s → %,d triangles/s, %,d pixels/s%n",
                BENCHMARK_TRIANGLE_COUNT,
                secs,
                (int)(BENCHMARK_TRIANGLE_COUNT / secs),
                (int)(estimatedPixels / secs)
        );
    }

    private void regenerateDirectPhase() {
        Graphics2D g2d = directImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        g2d.setComposite(AlphaComposite.SrcOver);
        long startNano = System.nanoTime();
        for (int i = 0; i < BENCHMARK_TRIANGLE_COUNT; i++) {
            Triangle2D tri = createRandomTriangle();
            tri.shadeToScreen(g2d);
        }
        long endNano = System.nanoTime();
        g2d.dispose();
        double secs = (endNano - startNano) / 1e9;
        System.out.printf(
                "On-screen: %,d triangles in %.3f s → %,d triangles/s%n",
                BENCHMARK_TRIANGLE_COUNT,
                secs,
                (int)(BENCHMARK_TRIANGLE_COUNT / secs)
        );
    }

    private Triangle2D createRandomTriangle() {
        return new Triangle2D(
                random.nextInt(CANVAS_WIDTH), random.nextInt(CANVAS_HEIGHT), createRandomColor(),
                random.nextInt(CANVAS_WIDTH), random.nextInt(CANVAS_HEIGHT), createRandomColor(),
                random.nextInt(CANVAS_WIDTH), random.nextInt(CANVAS_HEIGHT), createRandomColor()
        );
    }

    private Color createRandomColor() {
        return new Color(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Gouraud Benchmark");
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            GouraudBenchmark panel = new GouraudBenchmark();
            window.setContentPane(panel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
