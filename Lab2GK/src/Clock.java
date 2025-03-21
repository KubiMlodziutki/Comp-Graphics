import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.*;
import java.lang.Thread;
import java.lang.InterruptedException;
import javax.swing.*;

public class Clock {
    public static void main(String[] args) {
        double amplitudeDeg = 40.0;
        double period = 6.0;
        // Parameters handler
        if (args.length >= 2) {
            try {
                amplitudeDeg = Double.parseDouble(args[0]);
                period = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Usage: <amplitudeDeg> <period>");
                System.exit(1);
            }
        }
        // Create the window of the clock
        ClockWindow wnd = new ClockWindow(amplitudeDeg, period);

        // Closing window terminates the program
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the initial position of the window on the screen
        // and make the window visible
        wnd.setBounds(70, 70, 600, 600);
        wnd.setVisible(true);

        // Start the infinite loop of animation.
        // The program will run until the clock window is closed
        while (true) {
            try {
                // Wait a second before the clock is redisplayed
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Program interrupted");
            }
            // Redraw the clock according to current time
            wnd.repaint();
        }
    }
}

class ClockPane extends JPanel {
    final int TICK_LEN = 10;
    int center_x, center_y;
    int r_outer, r_inner;
    GregorianCalendar calendar;

    double amplitudeDeg;
    double period;
    double startTime;
    int r_ball = 15;

    ClockPane(double amplitudeDeg, double period) {
        super();
        setBackground(new Color(200, 200, 255));
        calendar = new GregorianCalendar();

        this.amplitudeDeg = amplitudeDeg;
        this.period = period;
        this.startTime = System.currentTimeMillis();
    }

    public void DrawPendulum(Graphics g) {
        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

        double tiltDeg = amplitudeDeg * Math.sin(2 * Math.PI * elapsedTime / period);
        double tiltRad = Math.toRadians(tiltDeg);

        int pendulumAttachX = center_x;
        int pendulumAttachY = center_y + r_outer;
        int r_ball = r_outer / 8;
        int length = r_outer * 4;

        int pendulumEndX = (int)(pendulumAttachX + length * Math.sin(tiltRad));
        int pendulumEndY = (int)(pendulumAttachY + length * Math.cos(tiltRad));

        g.setColor(Color.black);
        g.drawLine(pendulumAttachX, pendulumAttachY, pendulumEndX, pendulumEndY);

        g.fillOval(pendulumEndX - r_ball, pendulumEndY - r_ball, 2* r_ball, 2 * r_ball);

    }

    public void DrawTickMark(double angle, Graphics g) {
        int xw, yw, xz, yz;
        angle = Math.PI * angle / 180.0;
        xw = (int) (center_x + r_inner * Math.sin(angle));
        yw = (int) (center_y - r_inner * Math.cos(angle));
        xz = (int) (center_x + r_outer * Math.sin(angle));
        yz = (int) (center_y - r_outer * Math.cos(angle));
        g.drawLine(xw, yw, xz, yz);
    }

    public void DrawHand(double angle, int length, Graphics g) {
        int xw, yw, xz, yz;
        angle = Math.PI * angle / 180.0;
        xw = (int) (center_x + length * Math.sin(angle));
        yw = (int) (center_y - length * Math.cos(angle));
        angle += Math.PI;
        xz = (int) (center_x + TICK_LEN * Math.sin(angle));
        yz = (int) (center_y - TICK_LEN * Math.cos(angle));
        g.drawLine(xw, yw, xz, yz);
    }

    public void DrawDial(Graphics g) {
        g.drawOval(center_x - r_outer, center_y - r_outer, 2 * r_outer, 2 * r_outer);
        for (int i = 0; i <= 11; i++)
            DrawTickMark(i * 30.0, g);
    }

    public void paintComponent(Graphics g) {
        int minute, second, hour;
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Dimension size = getSize();
        double heightLimit = (size.height - 2 * r_ball) / 6.0;
        double tilt = Math.abs(Math.sin(Math.toRadians(amplitudeDeg)));
        double widthLimit = (tilt > 0) ? (size.width / 2.0 - r_ball) / (4 * tilt) : size.width / 2.0;
        r_outer = (int) Math.floor(Math.min(heightLimit, widthLimit));
        r_inner = r_outer - TICK_LEN;
        center_x = size.width / 2;
        center_y = r_outer;
        Date time = new Date();
        calendar.setTime(time);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR);
        if (hour > 11) hour = hour - 12;
        second = calendar.get(Calendar.SECOND);
        DrawDial(g);
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(5));
        DrawHand(360.0 * (hour * 60 + minute) / (60.0 * 12), (int) (0.75 * r_inner), g);
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        DrawHand(360.0 * (minute * 60 + second) / (3600.0), (int) (0.97 * r_outer), g);
        g2d.setColor(new Color(0, 0, 0));
        g2d.setStroke(new BasicStroke(1));
        DrawHand(second * 6.0, (int) (0.97 * r_inner), g);

        DrawPendulum(g);
    }
}

class ClockWindow extends JFrame {
    public ClockWindow(double amplitudeDeg, double period) {
        setContentPane(new ClockPane(amplitudeDeg, period));
        setTitle("Clock");
    }
}
