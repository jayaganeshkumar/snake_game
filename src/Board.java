import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
   
    private static final long serialVersionUID = 1L;
	private int count = 0;
	private final int frame_width = 600;
    private final int frame_height = 600;
    private final int dot_size = 10;
    private final int dots_num = 2000;
    private final int rand_pos = 29;
    private int speed = 120;

    private final int x[] = new int[dots_num];
    private final int y[] = new int[dots_num];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = false;
    
    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.blue);
        setFocusable(true);

        setPreferredSize(new Dimension(frame_width, frame_height));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon image_dot = new ImageIcon("src/resources/dot.png");
        ball = image_dot.getImage();


        ImageIcon image_apple = new ImageIcon("src/resources/apple.png");
        apple = image_apple.getImage();

        ImageIcon image_head = new ImageIcon("src/resources/head.png");
        head = image_head.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(speed, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);
            String counts = "Score: " +count;
            Font small = new Font("Arial", Font.BOLD, 15);
            g.setColor(Color.white);
            g.setFont(small);
            
            g.drawString(counts, (frame_width-100),(frame_height-580));

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }
    
    private void gameOver(Graphics g) {
        
        String msg = "Score: "+count;
        String gameover = "Game Over";
        String restart = "Press Space Bar to restart";
        Font small = new Font("Arial", Font.BOLD, 30);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (frame_width - metr.stringWidth(msg)) / 2, (frame_height / 2)-100);
        g.drawString(gameover, (frame_width - metr.stringWidth(gameover)) / 2, (frame_height / 2)-50);
        g.drawString(restart, (frame_width - metr.stringWidth(restart)) / 2, (frame_height / 2));
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            count++;
            locateApple();
        }
    }


    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= dot_size;
        }

        if (rightDirection) {
            x[0] += dot_size;
        }

        if (upDirection) {
            y[0] -= dot_size;
        }

        if (downDirection) {
            y[0] += dot_size;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= frame_height) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= frame_width) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * rand_pos);
        apple_x = ((r * dot_size));

        r = (int) (Math.random() * rand_pos);
        apple_y = ((r * dot_size));
    }

    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }
        
        repaint();
    }
    
    public void boardrestart() {
    	dots = 3;
    	count = 0;
    	speed = 120;
    	inGame = true;
    	rightDirection = true;
    	leftDirection = false;
    	upDirection  = false;
    	downDirection = false;
    	initBoard();
    
    }
    
	private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if((key == KeyEvent.VK_SPACE) && (inGame == false)) {
            	boardrestart();
            }
            
        }
    }
}