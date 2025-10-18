package Arkanoid.UI;

import Arkanoid.util.Constant;
import Arkanoid.util.HighScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Màn hình Menu đơn giản (không tích hợp high score)
 */
public class SimpleMenuScreen extends VBox {

    private Text currentScoreText;
    private Text highScoreText;
    private Button startButton;
    private Button highScoresButton;
    private Button settingsButton;
    private Button exitButton;

    private Runnable onStart;
    private Runnable onHighScores;
    private Runnable onSettings;
    private Runnable onExit;

    public SimpleMenuScreen() {
        setupUI();
    }

    private void setupUI() {
        // Thiết lập VBox
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);

        // Background đen như màn hình arcade
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        this.setBackground(new Background(backgroundFill));

        // Phần trên cùng - Thông tin điểm số
        HBox topPanel = createTopPanel();

        // Logo ARKANOID
        VBox titleSection = createTitleSection();

        // Menu options
        VBox menuSection = createMenuSection();

        // Footer với thông tin copyright
        VBox footerSection = createFooterSection();

        // Layout chính
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        this.getChildren().addAll(
                topPanel,
                spacer1,
                titleSection,
                spacer2,
                menuSection,
                new Region(),
                footerSection
        );
    }

    private HBox createTopPanel() {
        HBox panel = new HBox(100);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(10, 50, 10, 50));

        // 1UP section
        VBox player1Box = new VBox(5);
        player1Box.setAlignment(Pos.CENTER);

        Text player1Label = createRetroText("1UP", Color.ORANGE, 16, true);
        currentScoreText = createRetroText("00", Color.WHITE, 20, true);

        player1Box.getChildren().addAll(player1Label, currentScoreText);

        // HIGH SCORE section (static)
        VBox highScoreBox = new VBox(5);
        highScoreBox.setAlignment(Pos.CENTER);

        Text highScoreLabel = createRetroText("HIGH SCORE", Color.ORANGE, 16, true);
        highScoreText = createRetroText(String.format("%05d", HighScoreManager.getHighestScore()), Color.WHITE, 20, true);

        highScoreBox.getChildren().addAll(highScoreLabel, highScoreText);

        panel.getChildren().addAll(player1Box, highScoreBox);
        return panel;
    }

    private VBox createTitleSection() {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        // Logo ARKANOID với hiệu ứng pixel art
        Text arkanoidTitle = new Text("ARKANOID");
        arkanoidTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 64));
        arkanoidTitle.setFill(Color.WHITE);
        arkanoidTitle.setStroke(Color.BLUE);
        arkanoidTitle.setStrokeWidth(2);

        // Hiệu ứng 3D/pixel art
        arkanoidTitle.setStyle(
                "-fx-effect: dropshadow(gaussian, #0066ff, 4, 0.8, 2, 2);" +
                        "-fx-effect: dropshadow(gaussian, #ffffff, 2, 1.0, 0, 0);"
        );

        titleBox.getChildren().add(arkanoidTitle);
        return titleBox;
    }

    private VBox createMenuSection() {
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);

        // Tạo các menu items với hiệu ứng arrow selection
        startButton = createMenuOption(" 1 PLAYER", true);
        highScoresButton = createMenuOption("  HIGH SCORES", false);
        settingsButton = createMenuOption("  SETTINGS", false);
        exitButton = createMenuOption("  EXIT", false);

        // Button actions
        startButton.setOnAction(e -> {
            if (onStart != null) onStart.run();
        });

        highScoresButton.setOnAction(e -> {
            if (onHighScores != null) onHighScores.run();
        });

        settingsButton.setOnAction(e -> {
            if (onSettings != null) onSettings.run();
        });

        exitButton.setOnAction(e -> {
            if (onExit != null) onExit.run();
        });

        // Hover effects
        setupMenuHoverEffects();

        menuBox.getChildren().addAll(startButton, highScoresButton, settingsButton, exitButton);
        return menuBox;
    }

    private Button createMenuOption(String text, boolean isSelected) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);

        if (isSelected) {
            button.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                    "-fx-text-fill: #cccccc;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        }

        return button;
    }

    private void setupMenuHoverEffects() {
        // Start button hover
        startButton.setOnMouseEntered(e -> {
            startButton.setText("► 1 PLAYER");
            startButton.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        startButton.setOnMouseExited(e -> {
            startButton.setText(" 1 PLAYER");
            startButton.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        // High scores button hover
        highScoresButton.setOnMouseEntered(e -> {
            highScoresButton.setText("► HIGH SCORES");
            highScoresButton.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        highScoresButton.setOnMouseExited(e -> {
            highScoresButton.setText("  HIGH SCORES");
            highScoresButton.setStyle(
                    "-fx-text-fill: #cccccc;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        // Settings button hover
        settingsButton.setOnMouseEntered(e -> {
            settingsButton.setText("► SETTINGS");
            settingsButton.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        settingsButton.setOnMouseExited(e -> {
            settingsButton.setText("  SETTINGS");
            settingsButton.setStyle(
                    "-fx-text-fill: #cccccc;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        // Exit button hover
        exitButton.setOnMouseEntered(e -> {
            exitButton.setText("► EXIT");
            exitButton.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });

        exitButton.setOnMouseExited(e -> {
            exitButton.setText("  EXIT");
            exitButton.setStyle(
                    "-fx-text-fill: #cccccc;" +
                            "-fx-background-color: transparent;" +
                            "-fx-cursor: hand;"
            );
        });
    }

    private VBox createFooterSection() {
        VBox footerBox = new VBox(8);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(20, 0, 20, 0));

        // Tên developer (thay thế TAITO)
        Text developer = createRetroText("ARAKNOID STUDIO", Color.ORANGE, 14, true);

        // Copyright
        Text copyright = createRetroText("© 2024 ARAKNOID STUDIO", Color.WHITE, 12, false);

        // License info
        Text license = createRetroText("JAVA FX EDITION", Color.WHITE, 12, false);

        footerBox.getChildren().addAll(developer, copyright, license);
        return footerBox;
    }

    private Text createRetroText(String text, Color color, int size, boolean bold) {
        Text textNode = new Text(text);
        FontWeight weight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
        textNode.setFont(Font.font("Courier New", weight, size));
        textNode.setFill(color);

        // Thêm hiệu ứng pixel art
        if (bold) {
            textNode.setStroke(color.darker());
            textNode.setStrokeWidth(0.5);
        }

        return textNode;
    }

    /**
     * Cập nhật điểm hiện tại (nếu cần)
     */
    public void updateCurrentScore(int score) {
        currentScoreText.setText(String.format("%05d", score));
    }

    /**
     * Cập nhật điểm cao nhất
     */
    public void updateHighScore(int highScore) {
        if (highScoreText != null) {
            highScoreText.setText(String.format("%05d", highScore));
        }
    }

    public void setOnStart(Runnable action) {
        this.onStart = action;
    }

    public void setOnHighScores(Runnable action) {
        this.onHighScores = action;
    }

    public void setOnSettings(Runnable action) {
        this.onSettings = action;
    }

    public void setOnExit(Runnable action) {
        this.onExit = action;
    }
}
