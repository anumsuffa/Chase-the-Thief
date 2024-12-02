import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;

public class Pacman extends JPanel implements ActionListener, KeyListener {
    private int score = 0;
    private final List<Integer> scoreslist = new ArrayList<>(); // Store multiple scores
    private GameOverListener gameOverListener; // Listener for game over events
    private long startTime; // To track the start time of the game
    private long endTime; // To track the end time of the game
    private String username; // Store the username
    private ScoreManager scoreManager;

    // Define the GameOverListener interface
    public interface GameOverListener {
        void onGameOver(String username, int score, long time);
    }
    public void setUsername(String username) {
        this.username = username; // Method to set the username
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image moneyFood;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       X",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'}; //up down left right
    Random random = new Random();
    int lives = 3;
    boolean gameOver = false;

    Pacman() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.WHITE);
        addKeyListener(this);
        setFocusable(true);
        startTime = System.currentTimeMillis(); // Start the timer when the game initializes

        score=0;
        //load images
        wallImage = new ImageIcon(getClass().getResource("new wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("police.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("police.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("police.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("police.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("rabbitup.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("rabbittDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("theif left.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("theif right.png")).getImage();
        moneyFood = new ImageIcon(getClass().getResource("money.png")).getImage();
        loadMap();
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(50, this); //20fps (1000/50)
        gameLoop.start();

    }
    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { //food
                    Block food = new Block(moneyFood, x + 8, y + 8, 16, 16);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (Block food : foods) {
            if (food.image != null) { // Draw food image if available
                g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
            }
        }
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        g.setColor(Color.WHITE);

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        }
    }

    public void move() {
        if (gameOver) {
            score = 0; // Explicitly reset the score when the game ends and restarts
        }

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //check ghost collisions
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    gameOver();
                    if (gameOverListener != null) {
                        gameOverListener.onGameOver(username, score, System.currentTimeMillis() - startTime); // Notify game over and pass the score
                    }
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 1;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }

    public void gameOver() {
        gameLoop.stop(); // Stop the game loop
        endTime = System.currentTimeMillis(); // Capture the end time
        long timeTaken = (endTime - startTime) / 1000; // Calculate the time taken in milliseconds

        if (gameOverListener != null) {
            gameOverListener.onGameOver(username, score, timeTaken); // Notify game over and pass the score
        }

        // Show the "Game Over" dialog with Play Again and Quit buttons
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over, " + username + "! Your Score: " + score + " Time Taken: " + timeTaken + " seconds", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

        // Add Play Again and Quit buttons
        int option = JOptionPane.showOptionDialog(
                this,
                gameOverPanel,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Play Again", "Quit" },
                "Play Again"
        );

        if (option == JOptionPane.YES_OPTION) {
            // Play Again: Restart the game
            loadMap();
            resetPositions();
            lives = 3;
            this.score = 0;
            gameOver = false;
            startTime = System.currentTimeMillis(); // Reset the timer
            gameLoop.start();
        } else if (option == JOptionPane.NO_OPTION) {
            // Quit: Close the game window
            System.exit(0);
        }
    }

    public void startGameWithUsername(String username) {
        this.username = username; // Set the username
        JFrame gameFrame = new JFrame("Ghost-Run Game - Player: " + username);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(boardWidth, boardHeight+ 50);
        gameFrame.setLayout(new BorderLayout());

        JLabel timerlabel=new JLabel("Time:0s");
        timerlabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerlabel.setBackground(Color.blue);
        timerlabel.setForeground(Color.BLACK);
        timerlabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameFrame.add(timerlabel, BorderLayout.NORTH); // Place the label at the top

        gameFrame.add(this, BorderLayout.CENTER); // Place the game canvas in the center
        gameFrame.setLocationRelativeTo(null);
        // Record the start time in milliseconds
        startTime = System.currentTimeMillis();
        // Timer for updating elapsed time
        Timer timer = new Timer(1000, e -> {
            if (!gameOver) {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timerlabel.setText("Time: " + elapsedTime + " s");
            } });

        timer.start();
        gameFrame.setVisible(true);
    }
    public void endGame() {
        gameOver = true; // Set gameOver flag to true to stop the timer

        // Calculate elapsed time
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

}}