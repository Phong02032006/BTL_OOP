package arkanoid.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Highscore manager with ability to save player name.
 */
public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_HIGHSCORES = 10;

    private static List<HighScoreEntry> highScores = new ArrayList<>();

    /**
     * Class represents a high scoring entry
     */
    public static class HighScoreEntry implements Comparable<HighScoreEntry>, Serializable {
        private String playerName;
        private int score;
        private long timestamp;

        public HighScoreEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
            this.timestamp = System.currentTimeMillis();
        }

        public HighScoreEntry(String playerName, int score, long timestamp) {
            this.playerName = playerName;
            this.score = score;
            this.timestamp = timestamp;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public int compareTo(HighScoreEntry other) {
            int scoreCompare = Integer.compare(other.score, this.score);
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            return Long.compare(this.timestamp, other.timestamp);
        }

        @Override
        public String toString() {
            return String.format("%-15s %8d", playerName, score);
        }
    }

    /**
     * Initialize HighScoreManager and load data
     */
    public static void initialize() {
        loadHighScores();
        System.out.println("Đã khởi tạo HighScoreManager với " + highScores.size() + " điểm cao");
    }

    /**
     * Add new score to high score list
     *
     * @param playerName Player name
     * @param score Score
     * @return true if score is added to top 10
     */
    public static boolean addHighScore(String playerName, int score) {
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score);
        highScores.add(newEntry);
        Collections.sort(highScores);

        if (highScores.size() > MAX_HIGHSCORES) {
            highScores = highScores.subList(0, MAX_HIGHSCORES);
        }

        /*
          new thread to save highscores so that the main thread(JavaFX Application Thread)
          can continute handle UI updates, animations, and user input without interruption.
         */
        new Thread(() -> {
            saveHighScores();
        }).start();

        // check if new highscore added to top 10
        return highScores.contains(newEntry);
    }

    /**
     * Check if your score qualifies for the top 10
     */
    public static boolean isHighScore(int score) {
        if (highScores.size() < MAX_HIGHSCORES) {
            return true;
        }
        return score > highScores.get(highScores.size() - 1).getScore();
    }

    /**
     * Getter
     */
    public static List<HighScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Get the highest score
     */
    public static int getHighestScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return highScores.get(0).getScore();
    }

    /**
     * Save highscore to file
     */
    private static void saveHighScores() {
        try {
            String userHome = System.getProperty("user.home");
            String settingsDir = userHome + File.separator + ".arkanoid";
            File dir = new File(settingsDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File highscoresFile = new File(dir, HIGHSCORE_FILE);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(highscoresFile))) {
                oos.writeObject(highScores);
                System.out.println("Đã lưu " + highScores.size() + " điểm cao vào file");
            }

        } catch (IOException e) {
            System.err.println("Không thể lưu điểm cao: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Upload highscore from file
     */
    private static void loadHighScores() {
        try {
            String userHome = System.getProperty("user.home");
            String settingsDir = userHome + File.separator + ".arkanoid";
            File highscoresFile = new File(settingsDir, HIGHSCORE_FILE);

            if (!highscoresFile.exists()) {
                System.out.println("File điểm cao chưa tồn tại, khởi tạo danh sách trống");
                highScores = new ArrayList<>();
                return;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(highscoresFile))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    highScores = (List<HighScoreEntry>) obj;
                    Collections.sort(highScores);
                    System.out.println("Đã tải " + highScores.size() + " điểm cao từ file");
                } else {
                    highScores = new ArrayList<>();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Không thể tải điểm cao: " + e.getMessage());
            e.printStackTrace();
            highScores = new ArrayList<>();
        }
    }

    /**
     *  reset
     */
    public static void clearHighScores() {
        highScores.clear();

        /*
          new thread to save the file so that the main thread(JavaFX Application Thread)
          can continute handle UI updates, animations, and user input without interruption.
         */
        new Thread(() -> {
            saveHighScores();
        }).start();
        System.out.println("Erase all");
    }
}
