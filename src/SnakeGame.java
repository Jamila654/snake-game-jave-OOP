import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
// import java.util.random.*; //random points at which the food will appear
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile { // purpose of making this class it to define tile size which is 25 by 25
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody; // arraylist of type class tile which connects/make snake body

    // food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY; // to make the snake move every 1/10 milisecond
    boolean gameOver = false; // 2 conditions either it collides with itself or with wall.

    // Images
    Image snakeHeadImage;
    Image foodImage;

    // JFrame parentFrame;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5); // default starting position
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10); // default starting position for food
        random = new Random();
        placeFood(); // define method later below

        velocityX = 0;
        velocityY = 0;

        try { //in try the food and the snake head images are loaded and their size is the same as 1 tile(25x25) and the catch block detects error if the image does not exist.
            snakeHeadImage = new ImageIcon(getClass().getResource("/assets/snakehead.png"))
                    .getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            foodImage = new ImageIcon(getClass().getResource("/assets/snakefood.png"))
                    .getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        } catch (NullPointerException e) {
            System.err.println("Error: Image file not found. Ensure the images are in the 'src/assets' folder.");
            e.printStackTrace();
        }

        gameLoop = new Timer(100, this); //every 1/10 milisec
        gameLoop.start(); //starts the timer
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // grid for us to visualize
        g.setColor(Color.green);
        // for (int i = 0; i < boardWidth / tileSize; i++) {
        //     // x1 y1 x2 y2
        //     g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // vertically
        //     g.drawLine(0, i * tileSize, boardWidth, i * tileSize); // horizontally
        // }

        // Food
        g.drawImage(foodImage, food.x * tileSize, food.y * tileSize, null);

        // Snake head
        g.drawImage(snakeHeadImage, snakeHead.x * tileSize, snakeHead.y * tileSize, null);

        Graphics2D g2d = (Graphics2D) g;
    int angle = 0;

    // Determine the angle based on the current direction
    if (velocityX == 1) {
        angle = 90;  // Right
    } else if (velocityX == -1) {
        angle = 270; // Left
    } else if (velocityY == 1) {
        angle = 180; // Down
    } else if (velocityY == -1) {
        angle = 180;   // Up
    }

    // Rotate the snake head image before drawing
    g2d.rotate(Math.toRadians(angle), snakeHead.x * tileSize + tileSize / 2, snakeHead.y * tileSize + tileSize / 2);
    g.drawImage(snakeHeadImage, snakeHead.x * tileSize, snakeHead.y * tileSize, null);
    g2d.rotate(-Math.toRadians(angle), snakeHead.x * tileSize + tileSize / 2, snakeHead.y * tileSize + tileSize / 2);


        // snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fillOval(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        // score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize - 3);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize, tileSize - 7);
        }
        
    }
    


    

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize); // 600/25 = 24 so the x will be at random position from 0 to 24
        food.y = random.nextInt(boardHeight / tileSize); // same as x
    }

    public boolean collision(Tile tile1, Tile tile2) { // to check if the snake eats the food or itself
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { // to connect the first part with snake head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else { // moving snake body along the line
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // when it collides with itself
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }

        }

        // if it collides with walls
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // we need updown,left,right
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    public boolean gameOver() {
        return gameOver;
    }

    // do not need these
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
