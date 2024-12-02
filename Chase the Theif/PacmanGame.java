import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class PacmanGame {
    private int elapsedTime;  // Time in seconds
    private Timer timer;
    // Elapsed time in seconds
    private JLabel timeLabel; // JLabel to display the time

    public PacmanGame() {
        this.elapsedTime = 0;
        this.timer = new Timer();
    }

    // Start the timer when the game begins
    public void startGame() {
        // Reset elapsed time to 0
        elapsedTime = 0;
        // Schedule a task to increment the elapsed time every second
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime++;
                // Update the display (you can add a method for this)
                updateTimeDisplay();
            }
        }, 1000, 1000);  // Start after 1 second and repeat every 1 second
    }

    // Stop the timer when the game ends
    public void stopGame() {
        timer.cancel();
    }

    // Method to update the time display in the game window
    private void updateTimeDisplay() {
        // Your code here to update the GUI with the elapsed time
        System.out.println("Elapsed Time: " + elapsedTime + " seconds");
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}