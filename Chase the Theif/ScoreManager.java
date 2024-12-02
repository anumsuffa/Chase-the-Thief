import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {
    private ArrayList<ScoreRecord> scoreList;
    private static File scoreFile = new File("scores.txt");

    public ScoreManager() {
        scoreList = new ArrayList<>();
        loadScores();
    }
    public void recordScore(String username, int score, int time) {
        scoreList.add(new ScoreRecord(username, score, time));
        Collections.sort(scoreList);  // Sort by score and time
    }
    // Load scores from the file
    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(scoreFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    int time = Integer.parseInt(parts[2].trim());
                    scoreList.add(new ScoreRecord(username, score, time));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading score file.");
        }
    }

    public void displayScores() {
        // Display scores in your window, sorted by highest score, then least time
        for (ScoreRecord record : scoreList) {
            System.out.println(record.getUsername() + " - Score: " + record.getScore() + " - Time: " + record.getTime() + "s");
        }
    }

    // Save scores to the file
    public void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreFile))) {
            for (ScoreRecord entry : scoreList) {
                writer.write(entry.getUsername() + "," + entry.getScore() + "," + entry.getTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving scores.");
        }
    }

    // Add a new score and sort the list
    public void addScore(String username, int score, long time) {
        scoreList.add(new ScoreRecord(username, score, (int) time));
        sortScores();
        saveScores();
    }

    // Sort scores in descending order, then by time ascending
    private void sortScores() {
        scoreList.sort(Comparator.comparingInt((ScoreRecord entry) -> -entry.getScore())
                .thenComparingLong(entry -> entry.getTime()));
    }
    public void clearScores() {
        // Clear the score list
        scoreList.clear();

        // Save the empty list to the file
        saveScores();

        // Optionally, you can display a message or refresh the score window here.
        System.out.println("Score board cleared.");

        // If you want to update the score window, you can call showScoresWindow again.
        // This is optional based on how you want to update the GUI.
        // showScoresWindow(this);
    }

    // Display scores in a separate window with the highest at the top
    public static void showScoresWindow(ScoreManager scoreManager) {
        JFrame frame = new JFrame("Scores");
        frame.setSize(350, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the background color to baby pink for the whole window
        frame.getContentPane().setBackground(new Color(26, 39, 80)); // Baby pink

        // Use BorderLayout for the frame
        frame.setLayout(new BorderLayout());

        // Title Label (Centered at the top)
        JLabel titleLabel = new JLabel("Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE); // Set the title color to black
        frame.add(titleLabel, BorderLayout.NORTH);

        // Panel to hold the scores (with baby pink background)
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS)); // Stack items vertically
        scorePanel.setBackground(new Color(26, 39, 80)); // Baby pink background for score panel
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Remove margins around the panel

        // Loop to add scores
        for (int i = 0; i < scoreManager.scoreList.size(); i++) {
            ScoreRecord entry = scoreManager.scoreList.get(i);
            JPanel scoreEntry = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            scoreEntry.setOpaque(false); // Make the panel background transparent

            JLabel scoreLabel = new JLabel((i + 1) + ". " + entry.getUsername() + " - Score: " + entry.getScore() + " - Time: " + entry.getTime() + "s");
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            scoreLabel.setForeground(Color.WHITE); // Set score label color to black
            scoreEntry.add(scoreLabel);
            scorePanel.add(scoreEntry);

            // Add "Highest Score" label next to the first score
            if (i == 0) {
                JLabel highestScoreLabel = new JLabel(" Highest Score");
                highestScoreLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                highestScoreLabel.setForeground(Color.WHITE); // Set "Highest Score" label color to black
                scoreEntry.add(highestScoreLabel);
            }

            // Add each score entry to the scorePanel without extra vertical spacing
            scorePanel.add(scoreEntry);
        }

        // Scroll pane for the scores
        JScrollPane scrollPane = new JScrollPane(scorePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border from scroll pane
        frame.add(scrollPane, BorderLayout.CENTER);

        // Show the frame
        frame.setLocation(0, 100);
        frame.setVisible(true);
    }


}
    class ScoreRecord implements Comparable<ScoreRecord> {
    private String username;
    private int score;
    private int time;

    public ScoreRecord(String username, int score, int time) {
        this.username = username;
        this.score = score;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(ScoreRecord other) {
        if (this.score != other.score) {
            return Integer.compare(other.score, this.score);  // Sort by score descending
        } else {
            return Integer.compare(this.time, other.time);  // If scores are equal, sort by time ascending
        }
   }
}