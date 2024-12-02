import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleLoginWindow {
    private int boardWidth;
    private int boardHeight;
    private Pacman pacmanGame;
    private ScoreManager scoreManager;

    public SimpleLoginWindow(Pacman pacmanGame, ScoreManager scoreManager, int boardWidth, int boardHeight) {
        this.pacmanGame = pacmanGame;
        this.scoreManager = scoreManager;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public void showLoginWindow() {
        // Create the frame
        JFrame frame = new JFrame("Login Window");
        frame.setSize(600, 300); // Set the height to a shorter value
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set the background color of the frame
        frame.getContentPane().setBackground(new Color(26, 39, 80));  // Baby pink color

        // Heading label
        JLabel headingLabel = new JLabel("Welcome to Chase The Thief Game", JLabel.CENTER);
        headingLabel.setBounds(0, 10, boardWidth, 30);  // Full width, centered
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headingLabel.setForeground(Color.WHITE);
        frame.add(headingLabel);

        // Label for username
        JLabel userLabel = new JLabel("Enter Your Name:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        int labelWidth = userLabel.getPreferredSize().width;
        int combinedWidth = labelWidth + 10 + 160; // 10px gap and text field width
        int startX = (boardWidth - combinedWidth) / 2;
        userLabel.setBounds(startX, 90, labelWidth, 25);  // Place label at the start
        frame.add(userLabel);

        // Text field for username input
        JTextField userTextField = new JTextField(20);
        userTextField.setBounds(startX + labelWidth + 10, 90, 160, 25);  // Right beside the label
        userTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(userTextField);

        // Button to submit (Login)
        JButton loginButton = new JButton("Start Game");
        loginButton.setBounds((boardWidth - 200) / 2, 130, 200, 30);  // Increased width of the button
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(Color.WHITE);  // Light coral color
        loginButton.setForeground(Color.BLACK);
        frame.add(loginButton);

        // Button to view scores
        JButton scoresButton = new JButton("Scores");
        scoresButton.setBounds((boardWidth - 100) / 2, 170, 100, 30);  // Centered horizontally below Login button
        scoresButton.setFont(new Font("Arial", Font.BOLD, 16));
        scoresButton.setBackground(Color.WHITE);  // Light coral color
        scoresButton.setForeground(Color.BLACK);
        frame.add(scoresButton);

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                if (!username.isEmpty()) {
                    // Set the username in Pacman
                    pacmanGame.setUsername(username);

                    // Start the game directly
                    pacmanGame.startGameWithUsername(username);

                    // Close the login window after starting the game
                    frame.dispose(); // Make sure this happens only after the game starts
                }
            }
        });


        // Action listener for the scores button
        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the scores window
                ScoreManager.showScoresWindow(scoreManager);
            }
        });

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Set the frame visibility
        frame.setVisible(true);
    }
}
