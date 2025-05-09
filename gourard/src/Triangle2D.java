import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Triangle2D {
    private final int xVertexA;
    private final int yVertexA;
    private final int xVertexB;
    private final int yVertexB;
    private final int xVertexC;
    private final int yVertexC;
    private final Color colorVertexA;
    private final Color colorVertexB;
    private final Color colorVertexC;

    public Triangle2D(
            int xVertexA, int yVertexA, Color colorVertexA,
            int xVertexB, int yVertexB, Color colorVertexB,
            int xVertexC, int yVertexC, Color colorVertexC
    ) {
        this.xVertexA = xVertexA;
        this.yVertexA = yVertexA;
        this.colorVertexA = colorVertexA;
        this.xVertexB = xVertexB;
        this.yVertexB = yVertexB;
        this.colorVertexB = colorVertexB;
        this.xVertexC = xVertexC;
        this.yVertexC = yVertexC;
        this.colorVertexC = colorVertexC;
    }

    public void shadeToImage(BufferedImage image) {
        int minY = Math.min(yVertexA, Math.min(yVertexB, yVertexC));
        int maxY = Math.max(yVertexA, Math.max(yVertexB, yVertexC));

        for (int scanY = minY; scanY <= maxY; scanY++) {
            EdgeIntersection leftIntersection  = findEdgeIntersection(
                    scanY,
                    xVertexA, yVertexA, colorVertexA,
                    xVertexB, yVertexB, colorVertexB
            );
            EdgeIntersection rightIntersection = findEdgeIntersection(
                    scanY,
                    xVertexA, yVertexA, colorVertexA,
                    xVertexC, yVertexC, colorVertexC
            );
            if (leftIntersection == null || rightIntersection == null) {
                continue;
            }

            int startX = leftIntersection.intersectionX;
            int endX = rightIntersection.intersectionX;
            Color colorAtStart = leftIntersection.intersectionColor;
            Color colorAtEnd = rightIntersection.intersectionColor;
            int spanWidth = endX - startX;
            if (spanWidth <= 0) {
                continue;
            }

            float redStep = (colorAtEnd.getRed() - colorAtStart.getRed()) / (float)spanWidth;
            float greenStep = (colorAtEnd.getGreen() - colorAtStart.getGreen()) / (float)spanWidth;
            float blueStep= (colorAtEnd.getBlue() - colorAtStart.getBlue()) / (float)spanWidth;

            float redAcc = colorAtStart.getRed();
            float greenAcc = colorAtStart.getGreen();
            float blueAcc = colorAtStart.getBlue();

            for (int x = startX; x <= endX; x++) {
                int r = clamp(Math.round(redAcc));
                int g = clamp(Math.round(greenAcc));
                int b = clamp(Math.round(blueAcc));
                image.setRGB(x, scanY, new Color(r, g, b).getRGB());
                redAcc += redStep;
                greenAcc += greenStep;
                blueAcc += blueStep;
            }
        }
    }

    public void shadeToScreen(Graphics g) {
        int minY = Math.min(yVertexA, Math.min(yVertexB, yVertexC));
        int maxY = Math.max(yVertexA, Math.max(yVertexB, yVertexC));

        for (int scanY = minY; scanY <= maxY; scanY++) {
            EdgeIntersection leftIntersection  = findEdgeIntersection(
                    scanY,
                    xVertexA, yVertexA, colorVertexA,
                    xVertexB, yVertexB, colorVertexB
            );
            EdgeIntersection rightIntersection = findEdgeIntersection(
                    scanY,
                    xVertexA, yVertexA, colorVertexA,
                    xVertexC, yVertexC, colorVertexC
            );
            if (leftIntersection == null || rightIntersection == null) {
                continue;
            }

            int startX = leftIntersection.intersectionX;
            int endX = rightIntersection.intersectionX;
            Color colorAtStart = leftIntersection.intersectionColor;
            Color colorAtEnd = rightIntersection.intersectionColor;
            int spanWidth = endX - startX;
            if (spanWidth <= 0) {
                continue;
            }

            float redStep = (colorAtEnd.getRed() - colorAtStart.getRed()) / (float)spanWidth;
            float greenStep = (colorAtEnd.getGreen() - colorAtStart.getGreen()) / (float)spanWidth;
            float blueStep= (colorAtEnd.getBlue() - colorAtStart.getBlue()) / (float)spanWidth;

            float redAcc = colorAtStart.getRed();
            float greenAcc = colorAtStart.getGreen();
            float blueAcc = colorAtStart.getBlue();

            for (int x = startX; x <= endX; x++) {
                int red = clamp(Math.round(redAcc));
                int green = clamp(Math.round(greenAcc));
                int blue = clamp(Math.round(blueAcc));
                g.setColor(new Color(red, green, blue));
                g.fillRect(x, scanY, 1, 1);
                redAcc += redStep;
                greenAcc += greenStep;
                blueAcc += blueStep;
            }
        }
    }

    private EdgeIntersection findEdgeIntersection(
            int scanY,
            int xStart,
            int yStart,
            Color colorStart,
            int xEnd,
            int yEnd,
            Color colorEnd
    ) {
        if (!isBetween(scanY, yStart, yEnd)) {
            return null;
        }
        float t = (scanY - yStart) / (float)(yEnd - yStart);
        int intersectionX = Math.round(xStart + t * (xEnd - xStart));
        Color intersectionColor = interpolateColor(colorStart, colorEnd, t);
        return new EdgeIntersection(intersectionX, intersectionColor);
    }

    private boolean isBetween(int value, int bound1, int bound2) {
        return (bound1 <= value && value <= bound2)
                || (bound2 <= value && value <= bound1);
    }

    private Color interpolateColor(Color c1, Color c2, float t) {
        int r = Math.round(c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = Math.round(c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = Math.round(c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(clamp(r), clamp(g), clamp(b));
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static class EdgeIntersection {
        final int intersectionX;
        final Color intersectionColor;
        EdgeIntersection(int intersectionX, Color intersectionColor) {
            this.intersectionX = intersectionX;
            this.intersectionColor = intersectionColor;
        }
    }
}
