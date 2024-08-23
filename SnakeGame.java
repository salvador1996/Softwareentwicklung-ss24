import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int UNIT_SIZE = 10;
    private static final int ALL_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int[] x = new int[ALL_UNITS];
    private final int[] y = new int[ALL_UNITS];
    private int bodyParts = 3;
    private int foodX;
    private int foodY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        random = new Random();
        addKeyListener(new SnakeKeyAdapter());
        setFocusable(true);

        startGame();
    }

    private void startGame() {
        running = true;
        spawnFood();
        timer = new Timer(140, new GameCycle());
        timer.start();
    }

    private void spawnFood() {
        foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    private void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            spawnFood();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        if (running) {
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                g.setColor(Color.BLUE);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                checkFood();
                checkCollision();
                move();
            }
            repaint();
        }
    }

    private class SnakeKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
                direction = 'L';
            }

            if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
                direction = 'R';
            }

            if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
                direction = 'U';
            }

            if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
                direction = 'D';
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new SnakeGame();
            ex.setVisible(true);
        });
    }
}
