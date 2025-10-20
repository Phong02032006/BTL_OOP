package arkanoid.ui;

import arkanoid.util.Constant;
import arkanoid.util.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Simple gameOver Screen (do not include highscore)
 */
public class SimpleGameOverScreen extends VBox {

    private Text scoreText;
    private Button restartButton;
    private Button mainMenuButton;
    private Text messageText;

    private int finalScore;
    private Runnable onRestart;
    private Runnable onMainMenu;

    public SimpleGameOverScreen() {
        setupUI();
    }

    private void setupUI() {
        // VBox Setup
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(50));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);

        // Background arcade
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        this.setBackground(new Background(backgroundFill));

        // Title  retro
        Text title = new Text("GAME OVER");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
        title.setFill(Color.RED);
        title.setStroke(Color.DARKRED);
        title.setStrokeWidth(2);

        //  pixel art
        title.setStyle(
                "-fx-effect: dropshadow(gaussian, #ff0000, 4, 0.8, 2, 2);" +
                        "-fx-effect: dropshadow(gaussian, #ffffff, 2, 1.0, 0, 0);"
        );

        // Score display
        scoreText = new Text("FINAL SCORE: 00000");
        scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        scoreText.setFill(Color.YELLOW);
        scoreText.setStroke(Color.ORANGE);
        scoreText.setStrokeWidth(1);

        // Message text
        messageText = new Text("BETTER LUCK NEXT TIME!");
        messageText.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));
        messageText.setFill(Color.LIGHTGREEN);
        messageText.setTextAlignment(TextAlignment.CENTER);

        // Buttons
        restartButton = createRetroButton("PLAY AGAIN");
        mainMenuButton = createRetroButton("MAIN MENU");

        restartButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onRestart != null) onRestart.run();
        });

        mainMenuButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onMainMenu != null) onMainMenu.run();
        });

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(restartButton, mainMenuButton);

        // Add to layout
        this.getChildren().addAll(title, scoreText, messageText, buttonBox);
    }

    private Button createRetroButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        button.setPrefWidth(200);
        button.setPrefHeight(40);

        button.setStyle(
                "-fx-text-fill: #ffffff;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: #ff6600;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-cursor: hand;"
        );

        // Hover effect
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-text-fill: #ffcc00;" +
                                "-fx-background-color: rgba(255, 102, 0, 0.2);" +
                                "-fx-border-color: #ffcc00;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-text-fill: #ffffff;" +
                                "-fx-background-color: transparent;" +
                                "-fx-border-color: #ff6600;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-cursor: hand;"
                )
        );

        return button;
    }

    public void setScore(int score) {
        this.finalScore = score;
        scoreText.setText("FINAL SCORE: " + String.format("%05d", score));
    }

    public void setOnRestart(Runnable action) {
        this.onRestart = action;
    }

    public void setOnMainMenu(Runnable action) {
        this.onMainMenu = action;
    }
}

