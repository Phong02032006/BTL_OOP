package Arkanoid.UI;

import Arkanoid.util.Constant;
import Arkanoid.util.HighScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Màn hình High Score hiển thị danh sách điểm cao
 */
public class HighScoreScreen extends VBox {
    
    private Button backButton;
    private Button clearButton;
    private VBox scoreListContainer;
    
    private Runnable onBack;
    
    public HighScoreScreen() {
        setupUI();
        loadHighScores();
    }
    
    private void setupUI() {
        // Thiết lập VBox
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(40));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        
        // Background đen như màn hình arcade
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.BLACK,
            CornerRadii.EMPTY,
            Insets.EMPTY
        );
        this.setBackground(new Background(backgroundFill));
        
        // Tiêu đề
        VBox titleSection = createTitleSection();
        
        // Phần danh sách điểm cao
        VBox scoresSection = createScoresSection();
        
        // Nút điều khiển
        VBox controlSection = createControlSection();
        
        this.getChildren().addAll(titleSection, scoresSection, controlSection);
    }
    
    private VBox createTitleSection() {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        
        Text highScoreTitle = new Text("HIGH SCORES");
        highScoreTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
        highScoreTitle.setFill(Color.WHITE);
        highScoreTitle.setStroke(Color.GOLD);
        highScoreTitle.setStrokeWidth(2);
        
        // Hiệu ứng 3D/pixel art
        highScoreTitle.setStyle(
            "-fx-effect: dropshadow(gaussian, #ffd700, 4, 0.8, 2, 2);" +
            "-fx-effect: dropshadow(gaussian, #ffffff, 2, 1.0, 0, 0);"
        );
        
        titleBox.getChildren().add(highScoreTitle);
        return titleBox;
    }
    
    private VBox createScoresSection() {
        VBox scoresBox = new VBox(10);
        scoresBox.setAlignment(Pos.CENTER);
        scoresBox.setPadding(new Insets(20, 50, 20, 50));
        
        // Header
        HBox header = createScoreHeader();
        scoresBox.getChildren().add(header);
        
        // Container cho danh sách điểm
        scoreListContainer = new VBox(5);
        scoreListContainer.setAlignment(Pos.CENTER);
        scoreListContainer.setPadding(new Insets(20, 0, 20, 0));
        
        scoresBox.getChildren().add(scoreListContainer);
        return scoresBox;
    }
    
    private HBox createScoreHeader() {
        HBox header = new HBox(50);
        header.setAlignment(Pos.CENTER);
        
        Text rankHeader = createRetroText("RANK", Color.ORANGE, 18, true);
        Text nameHeader = createRetroText("PLAYER NAME", Color.ORANGE, 18, true);
        Text scoreHeader = createRetroText("SCORE", Color.ORANGE, 18, true);
        
        header.getChildren().addAll(rankHeader, nameHeader, scoreHeader);
        return header;
    }
    
    private VBox createControlSection() {
        VBox controlBox = new VBox(20);
        controlBox.setAlignment(Pos.CENTER);
        
        // Nút Clear All Scores
        clearButton = createControlButton("XÓA TẤT CẢ ĐIỂM");
        clearButton.setOnAction(e -> clearAllScores());
        
        // Nút Quay lại
        backButton = createControlButton("QUAY LẠI");
        backButton.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });
        
        controlBox.getChildren().addAll(clearButton, backButton);
        return controlBox;
    }
    
    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        button.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-text-fill: #000000;" +
                "-fx-background-color: #ffffff;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            );
        });
        
        return button;
    }
    
    private Text createRetroText(String text, Color color, int size, boolean bold) {
        Text textNode = new Text(text);
        FontWeight weight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
        textNode.setFont(Font.font("Courier New", weight, size));
        textNode.setFill(color);
        
        if (bold) {
            textNode.setStroke(color.darker());
            textNode.setStrokeWidth(0.5);
        }
        
        return textNode;
    }
    
    /**
     * Tải và hiển thị danh sách điểm cao
     */
    private void loadHighScores() {
        scoreListContainer.getChildren().clear();
        
        List<HighScoreManager.HighScoreEntry> scores = HighScoreManager.getHighScores();
        
        if (scores.isEmpty()) {
            Text noScoresText = createRetroText("CHƯA CÓ ĐIỂM CAO NÀO", Color.WHITE, 20, true);
            scoreListContainer.getChildren().add(noScoresText);
            return;
        }
        
        for (int i = 0; i < scores.size(); i++) {
            HighScoreManager.HighScoreEntry entry = scores.get(i);
            HBox scoreRow = createScoreRow(i + 1, entry.getPlayerName(), entry.getScore());
            scoreListContainer.getChildren().add(scoreRow);
        }
        
        // Thêm các dòng trống nếu chưa đủ 10 điểm
        for (int i = scores.size(); i < 10; i++) {
            HBox emptyRow = createEmptyScoreRow(i + 1);
            scoreListContainer.getChildren().add(emptyRow);
        }
    }
    
    /**
     * Tạo một dòng điểm cao
     */
    private HBox createScoreRow(int rank, String playerName, int score) {
        HBox row = new HBox(50);
        row.setAlignment(Pos.CENTER);
        
        // Màu sắc khác nhau cho top 3
        Color rankColor = getRankColor(rank);
        Color nameColor = rank <= 3 ? Color.GOLD : Color.WHITE;
        Color scoreColor = rank <= 3 ? Color.GOLD : Color.WHITE;
        
        Text rankText = createRetroText(String.format("%2d.", rank), rankColor, 16, true);
        Text nameText = createRetroText(String.format("%-15s", playerName), nameColor, 16, true);
        Text scoreText = createRetroText(String.format("%8d", score), scoreColor, 16, true);
        
        row.getChildren().addAll(rankText, nameText, scoreText);
        return row;
    }
    
    /**
     * Tạo một dòng trống
     */
    private HBox createEmptyScoreRow(int rank) {
        HBox row = new HBox(50);
        row.setAlignment(Pos.CENTER);
        
        Text rankText = createRetroText(String.format("%2d.", rank), Color.GRAY, 16, false);
        Text nameText = createRetroText(String.format("%-15s", "---"), Color.GRAY, 16, false);
        Text scoreText = createRetroText(String.format("%8s", "---"), Color.GRAY, 16, false);
        
        row.getChildren().addAll(rankText, nameText, scoreText);
        return row;
    }
    
    /**
     * Lấy màu cho rank
     */
    private Color getRankColor(int rank) {
        switch (rank) {
            case 1: return Color.GOLD;
            case 2: return Color.SILVER;
            case 3: return Color.ORANGE;
            default: return Color.WHITE;
        }
    }
    
    /**
     * Xóa tất cả điểm cao
     */
    private void clearAllScores() {
        HighScoreManager.clearHighScores();
        loadHighScores();
        System.out.println("Đã xóa tất cả điểm cao");
    }
    
    /**
     * Làm mới danh sách điểm cao (được gọi từ bên ngoài)
     */
    public void refreshHighScores() {
        loadHighScores();
    }
    
    public void setOnBack(Runnable action) {
        this.onBack = action;
    }
}
