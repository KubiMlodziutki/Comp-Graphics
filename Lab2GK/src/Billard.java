import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.lang.Thread;

public class Billard {
    public static void main(String[] args) {
        int numberOfBalls = 5;
        double friction = 0.01;
        if (args.length >= 2) {
            try {
                numberOfBalls = Integer.parseInt(args[0]);
                friction = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Usage: <numberOfBalls> <friction>");
                System.exit(1);
            }
        }
        BilliardWindow wnd = new BilliardWindow(numberOfBalls, friction);
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wnd.setBounds(70, 70, 800, 600);
        wnd.setVisible(true);
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Program interrupted");
            }
            wnd.repaint();
        }
    }
}

class BilliardWindow extends JFrame {
    public BilliardWindow(int numberOfBalls, double friction) {
        setContentPane(new BilliardPane(numberOfBalls, friction));
        setTitle("Billard Table");
    }
}

class BilliardPane extends JPanel {
    ArrayList<Ball> balls;
    double friction;

    BilliardPane(int numberOfBalls, double friction) {
        super();
        setBackground(Color.green);
        this.friction = friction;
        balls = new ArrayList<>();
        for (int i = 0; i < numberOfBalls; i++) {
            balls.add(new Ball());
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateBalls();
        for (Ball b : balls) {
            b.draw(g);
        }
    }

    private void updateBalls() {
        for (int i = 0; i < balls.size(); i++) {
            Ball b1 = balls.get(i);
            b1.move(getWidth(), getHeight(), friction);
            for (int j = i + 1; j < balls.size(); j++) {
                Ball b2 = balls.get(j);
                if (b1.checkCollision(b2)) {
                    b1.processCollision(b2);
                }
            }
        }
    }
}

class Ball {
    double x;
    double y;
    double vx;
    double vy;
    int radius;

    Ball() {
        radius = 15;
        x = Math.random() * 300 + 100;
        y = Math.random() * 200 + 100;
        vx = Math.random() * 4 - 2;
        vy = Math.random() * 4 - 2;
    }

    void move(int width, int height, double friction) {
        x += vx;
        y += vy;
        vx *= (1 - friction);
        vy *= (1 - friction);
        if (Math.abs(vx) < 0.01) vx = 0;
        if (Math.abs(vy) < 0.01) vy = 0;
        if (x - radius < 0) {
            x = radius;
            vx = -vx;
        }
        if (x + radius > width) {
            x = width - radius;
            vx = -vx;
        }
        if (y - radius < 0) {
            y = radius;
            vy = -vy;
        }
        if (y + radius > height) {
            y = height - radius;
            vy = -vy;
        }
    }

    void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)(x - radius), (int)(y - radius), 2 * radius, 2 * radius);
    }

    boolean checkCollision(Ball other) {
        double dx = other.x - x;
        double dy = other.y - y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist < (radius + other.radius);
    }

    void processCollision(Ball other) {
        double dx = other.x - x;
        double dy = other.y - y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist == 0) {
            return;
        }
        double nx = dx / dist;
        double ny = dy / dist;
        double p = 2 * (vx*nx + vy*ny - other.vx*nx - other.vy*ny) / 2;
        vx = vx - p * nx;
        vy = vy - p * ny;
        other.vx = other.vx + p * nx;
        other.vy = other.vy + p * ny;
    }
}
