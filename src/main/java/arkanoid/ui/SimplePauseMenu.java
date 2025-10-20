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

/**
 * Pause menu screen (not include highscore)
 */
public class SimplePauseMenu extends VBox {

    private Button resumeButton;
    private Button restartButton;
    private Button mainMenuButton;
    private Button settingsButton;

    private Runnable onResume;
    private Runnable onRestart;
    private Runnable onSettings;
    private Runnable onMainMenu;

    public SimplePauseMenu() {
        setupUI();
    }

    private void setupUI() {
        // VBox setup
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        this.setPadding(new Insets(50));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);

        // Semi-transparent background
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.rgb(0, 0, 0, 0.85),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        this.setBackground(new Background(backgroundFill));

        // Title retro
        Text title = new Text("PAUSED");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 42));
        title.setFill(Color.YELLOW);
        title.setStroke(Color.ORANGE);
        title.setStrokeWidth(2);

        // pixel art effect
        title.setStyle(
                "-fx-effect: dropshadow(gaussian, #ffaa00, 3, 0.8, 1, 1);"
        );

        // Subtitle
        Text subtitle = new Text("GAME TEMPORARILY STOPPED");
        subtitle.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        subtitle.setFill(Color.LIGHTGRAY);

        // Buttons
        resumeButton = createRetroButton("RESUME");
        restartButton = createRetroButton("RESTART");
        settingsButton = createRetroButton("SETTINGS");
        mainMenuButton = createRetroButton("MAIN MENU");

        // Button actions
        resumeButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onResume != null) onResume.run();
        });

        settingsButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onSettings != null) onSettings.run();
        });

        restartButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onRestart != null) onRestart.run();
        });

        mainMenuButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onMainMenu != null) onMainMenu.run();
        });

        // Add to layout
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(resumeButton, restartButton, settingsButton, mainMenuButton);
        this.getChildren().addAll(title, subtitle, buttonBox);
    }

    private Button createRetroButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        button.setPrefWidth(180);
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

    public void setOnSettings(Runnable action) {
        this.onSettings = action;
    }

    public void setOnResume(Runnable action) {
        this.onResume = action;
    }

    public void setOnRestart(Runnable action) {
        this.onRestart = action;
    }

    public void setOnMainMenu(Runnable action) {
        this.onMainMenu = action;
    }
}
