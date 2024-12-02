public class ScoreEntry {
    public String username;
    public int score;
    public long time; // Time in milliseconds

    public ScoreEntry(String username, int score, long time) {
        this.username = username;
        this.score = score;
        this.time = time;
    }

    public void addScore(String username, int score, long time) {
    }}