package arkanoid.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Quản lý hệ thống điểm cao với khả năng lưu tên người chơi
 */
public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_HIGHSCORES = 10;

    private static List<HighScoreEntry> highScores = new ArrayList<>();

    /**
     * Lớp đại diện cho một entry điểm cao
     */
    public static class HighScoreEntry implements Comparable<HighScoreEntry> {
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
            // Sắp xếp theo điểm từ cao đến thấp
            int scoreCompare = Integer.compare(other.score, this.score);
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            // Nếu điểm bằng nhau, sắp xếp theo thời gian (cũ hơn trước)
            return Long.compare(this.timestamp, other.timestamp);
        }

        @Override
        public String toString() {
            return String.format("%-15s %8d", playerName, score);
        }
    }

    /**
     * Khởi tạo HighScoreManager và tải dữ liệu
     */
    public static void initialize() {
        loadHighScores();
        System.out.println("Đã khởi tạo HighScoreManager với " + highScores.size() + " điểm cao");
    }

    /**
     * Thêm điểm mới vào danh sách điểm cao
     *
     * @param playerName Tên người chơi
     * @param score      Điểm số
     * @return true nếu điểm được thêm vào top 10
     */
    public static boolean addHighScore(String playerName, int score) {
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score);
        highScores.add(newEntry);
        Collections.sort(highScores);

        // Giữ chỉ top 10
        if (highScores.size() > MAX_HIGHSCORES) {
            highScores = highScores.subList(0, MAX_HIGHSCORES);
        }

        saveHighScores();

        // Kiểm tra xem điểm có trong top 10 không
        return highScores.contains(newEntry);
    }

    /**
     * Kiểm tra xem điểm có đủ điều kiện vào top 10 không
     */
    public static boolean isHighScore(int score) {
        if (highScores.size() < MAX_HIGHSCORES) {
            return true;
        }
        return score > highScores.get(highScores.size() - 1).getScore();
    }

    /**
     * Lấy danh sách điểm cao
     */
    public static List<HighScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Lấy điểm cao nhất
     */
    public static int getHighestScore() {
        if (highScores.isEmpty()) {
            return 0;
        }
        return highScores.get(0).getScore();
    }

    /**
     * Lưu điểm cao vào file
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
     * Tải điểm cao từ file
     */
    @SuppressWarnings("unchecked")
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
     * Xóa tất cả điểm cao (reset)
     */
    public static void clearHighScores() {
        highScores.clear();
        saveHighScores();
        System.out.println("Đã xóa tất cả điểm cao");
    }

    /**
     * Tạo một số điểm cao mẫu để test
     */
    public static void createSampleData() {
        if (highScores.isEmpty()) {
            String[] names = {"PLAYER1", "GAMER", "MASTER", "PRO", "EXPERT", "LEGEND", "CHAMP", "HERO", "ACE", "BOSS"};
            int[] scores = {50000, 45000, 40000, 35000, 30000, 25000, 20000, 15000, 10000, 5000};

            for (int i = 0; i < names.length; i++) {
                highScores.add(new HighScoreEntry(names[i], scores[i]));
            }

            Collections.sort(highScores);
            saveHighScores();
            System.out.println("Đã tạo " + highScores.size() + " điểm cao mẫu");
        }
    }
}
