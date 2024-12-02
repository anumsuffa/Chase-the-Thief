public class App {
    public static void main(String[] args) throws Exception {
        // Initialize ScoreManager
        ScoreManager scoreManager = new ScoreManager();

        // Set up the board dimensions
        int boardWidth = 608; // Set width according to your game size
        int boardHeight = 672; // Set height according to your game size

        // Initialize the Pacman game
        Pacman pacmanGame = new Pacman();
        PacmanGame game = new PacmanGame();

        // Set up the game to handle the end of each game session and save the score
        pacmanGame.setGameOverListener(new Pacman.GameOverListener() {
            @Override
            public void onGameOver(String username, int score, long time) {
                scoreManager.addScore(username, score, time);// Save the score when the game is over
            }
        });

        // Call the login window with ScoreManager
        SimpleLoginWindow loginWindow = new SimpleLoginWindow(pacmanGame, scoreManager, boardWidth, boardHeight);
        loginWindow.showLoginWindow();// Display the login window


    }
}