import java.awt.*;
import javax.swing.*;

public class SolarSystem {
    public static void main(String[] args) {
        SolarSystemWindow wnd = new SolarSystemWindow();
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wnd.setBounds(100, 100, 1000, 800);
        wnd.setTitle("Solar System");
        wnd.setVisible(true);
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("Program interrupted");
                break;
            }
            wnd.repaint();
        }
    }
}

class SolarSystemWindow extends JFrame {
    public SolarSystemWindow() {
        SolarSystemPane pane = new SolarSystemPane();
        setContentPane(pane);
    }
}

class SolarSystemPane extends JPanel {
    private long startTime;
    private double scale;

    public SolarSystemPane() {
        setBackground(Color.BLACK);
        startTime = System.currentTimeMillis();
    }

    double earthPeriod = 20;
    int earthRadius = 7;
    int earthElipseRadiusX = 90;
    int earthElipseRadiusY = 50;
    int sunRadius;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
        int width = getWidth();
        int height = getHeight();
        int centerSunX = width / 2;
        int centerSunY = height / 2;
        double scaleX = (width / 2.0) / (3.3 * earthElipseRadiusX + 2.5 * earthRadius);
        double scaleY = (height / 2.0) / (3.3 * earthElipseRadiusY + 2.5 * earthRadius);
        scale = Math.min(scaleX, scaleY);

        int scaledEarthRadius = (int) (earthRadius * scale);
        int scaledEarthElipseRadiusX = (int) (earthElipseRadiusX * scale);
        int scaledEarthElipseRadiusY = (int) (earthElipseRadiusY * scale);

        sunRadius = (int) (15 * scale);

        double mercuryPeriod = 0.24 * earthPeriod;
        int mercuryRadius = (int) (0.38 * scaledEarthRadius);
        int mercuryElipseRadiusX = (int) (0.39 * scaledEarthElipseRadiusX);
        int mercuryElipseRadiusY = (int) (0.39 * scaledEarthElipseRadiusY);

        double venusPeriod = 0.62 * earthPeriod;
        int venusRadius = (int) (0.95 * scaledEarthRadius);
        int venusElipseRadiusX = (int) (0.6 * scaledEarthElipseRadiusX);
        int venusElipseRadiusY = (int) (0.6 * scaledEarthElipseRadiusY);

        double marsPeriod = 1.88 * earthPeriod;
        int marsRadius = (int) (0.53 * scaledEarthRadius);
        int marsElipseRadiusX = (int) (1.5 * scaledEarthElipseRadiusX);
        int marsElipseRadiusY = (int) (1.5 * scaledEarthElipseRadiusY);

        double jupiterPeriod = 11.86 * earthPeriod;
        int jupiterRadius = (int) (1.5 * scaledEarthRadius);
        int jupiterElipseRadiusX = (int) (1.8 * scaledEarthElipseRadiusX);
        int jupiterElipseRadiusY = (int) (1.8 * scaledEarthElipseRadiusY);

        double saturnPeriod = 29.46 * earthPeriod;
        int saturnRadius = (int) (1.3 * scaledEarthRadius);
        int saturnElipseRadiusX = (int) (2.5 * scaledEarthElipseRadiusX);
        int saturnElipseRadiusY = (int) (2.5 * scaledEarthElipseRadiusY);

        double uranusPeriod = 84.01 * earthPeriod;
        int uranusRadius = (int) (1.1 * scaledEarthRadius);
        int uranusElipseRadiusX = (int) (2.8 * scaledEarthElipseRadiusX);
        int uranusElipseRadiusY = (int) (2.8 * scaledEarthElipseRadiusY);

        double neptunePeriod = 164.79 * earthPeriod;
        int neptuneRadius = (int) (2.5 * scaledEarthRadius);
        int neptuneElipseRadiusX = (int) (3.3 * scaledEarthElipseRadiusX);
        int neptuneElipseRadiusY = (int) (3.3 * scaledEarthElipseRadiusY);

        if (width <=  neptuneElipseRadiusX) {

        }

        drawSun(g2d, centerSunX, centerSunY);
        drawPlanet(centerSunX, centerSunY, g2d, mercuryElipseRadiusY, mercuryElipseRadiusX, mercuryPeriod, mercuryRadius, elapsedTime, Color.gray, false);
        drawPlanet(centerSunX, centerSunY, g2d, venusElipseRadiusY, venusElipseRadiusX, venusPeriod, venusRadius, elapsedTime, Color.orange, false);
        drawPlanet(centerSunX, centerSunY, g2d, scaledEarthElipseRadiusY, scaledEarthElipseRadiusX, earthPeriod, scaledEarthRadius, elapsedTime, Color.green, true);
        drawPlanet(centerSunX, centerSunY, g2d, marsElipseRadiusY, marsElipseRadiusX, marsPeriod, marsRadius, elapsedTime, Color.red, false);
        drawPlanet(centerSunX, centerSunY, g2d, jupiterElipseRadiusY, jupiterElipseRadiusX, jupiterPeriod, jupiterRadius, elapsedTime, new Color(183, 196, 114), false);
        drawPlanet(centerSunX, centerSunY, g2d, saturnElipseRadiusY, saturnElipseRadiusX, saturnPeriod, saturnRadius, elapsedTime, new Color(240, 255, 161), false);
        drawPlanet(centerSunX, centerSunY, g2d, uranusElipseRadiusY, uranusElipseRadiusX, uranusPeriod, uranusRadius, elapsedTime, new Color(120, 162, 161), false);
        drawPlanet(centerSunX, centerSunY, g2d, neptuneElipseRadiusY, neptuneElipseRadiusX, neptunePeriod, neptuneRadius, elapsedTime, Color.blue, false);
    }

    public void drawSun(Graphics2D g2d, int centerX, int centerY) {
        int sunRadius = (int) (15 * scale);
        g2d.setColor(Color.yellow);
        g2d.fillOval(centerX - sunRadius, centerY - sunRadius, 2 * sunRadius, 2 * sunRadius);
    }

    public void drawPlanet(int centerSunX, int centerSunY, Graphics2D g2d, int elipseHeight, int elipseWidth,
                           double orbitPeriod, int planetRadius, double elapsedTime, Color planetColor, boolean isMoon) {
        double angle = 2 * Math.PI * (elapsedTime / orbitPeriod);

        int planetX = (int) (centerSunX + elipseWidth * Math.cos(angle));
        int planetY = (int) (centerSunY + elipseHeight * Math.sin(angle));

        if (isMoon) {
            drawMoon(g2d, planetX, planetY, (int)(earthRadius * 1.5 * scale), 2.0, (int)(earthRadius / 5.0 * scale), elapsedTime);
        }

        g2d.setColor(Color.gray);
        g2d.drawOval(centerSunX - elipseWidth, centerSunY - elipseHeight, (2 * elipseWidth), (2 * elipseHeight));

        g2d.setColor(planetColor);
        g2d.fillOval(planetX - planetRadius, planetY - planetRadius, 2 * planetRadius, 2 * planetRadius);
    }

    private void drawMoon(Graphics2D g2d, int earthX, int earthY, int moonOrbitRadius, double moonPeriod, int moonRadius, double elapsedTime) {
        double angle = 2 * Math.PI * (elapsedTime / moonPeriod);

        int moonX = (int) (earthX + moonOrbitRadius * Math.cos(angle));
        int moonY = (int) (earthY + moonOrbitRadius * Math.sin(angle));

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(earthX - moonOrbitRadius, earthY - moonOrbitRadius, (2 * moonOrbitRadius), (2 * moonOrbitRadius));

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(moonX - moonRadius, moonY - moonRadius, 2 * moonRadius, 2 * moonRadius);
    }
}
