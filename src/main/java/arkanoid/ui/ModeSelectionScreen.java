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
 * Màn hình chọn chế độ chơi với phong cách retro.
 */
public class ModeSelectionScreen extends VBox {

    private Button normalModeButton;
    private Button funnyModeButton;
    private Button backButton;

    private Runnable onNormalModeSelected;
    private Runnable onFunnyModeSelected;
    private Runnable onBackSelected;

    public ModeSelectionScreen() {
        setupUI();
    }

    private void setupUI() {
        // VBox figure
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        this.setPadding(new Insets(50));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        this.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
        )));

        // Banner
        Text title = createRetroText("CHOOSE YOUR MODE", Color.ORANGE, 36, true);

        // Button for choosing mode
        normalModeButton = createMenuOption("NORMAL MODE");
        funnyModeButton = createMenuOption("FUNNY MODE");
        backButton = createMenuOption("BACK");

        // apply action for each button
        normalModeButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onNormalModeSelected != null) onNormalModeSelected.run();
        });
        funnyModeButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onFunnyModeSelected != null) onFunnyModeSelected.run();
        });
        backButton.setOnAction(e -> {
            SoundManager.playSound("clickingg.wav");
            if (onBackSelected != null) onBackSelected.run();
        });

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(normalModeButton, funnyModeButton, backButton); // << THÊM NÚT BACK VÀO ĐÂY


        Region spacer1 = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        // Add to main layout
        this.getChildren().addAll(title, spacer1, buttonBox, spacer2);
    }

    /**
     * reusing SimpleMenuScreen for syncing button.
     */
    private Button createMenuOption(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        button.setPrefWidth(220); // Tăng chiều rộng một chút
        button.setPrefHeight(40);
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-text-fill: #cccccc; " +
                " -fx-background-color: transparent; " +
                "-fx-cursor: hand;");

        // hover effect
        button.setOnMouseEntered(e -> {
            button.setText("► " + text);
            button.setStyle("-fx-text-fill: #ffffff;" +
                    " -fx-background-color: transparent;" +
                    " -fx-cursor: hand;");
        });
        button.setOnMouseExited(e -> {
            button.setText(text);
            button.setStyle("-fx-text-fill: #cccccc; " +
                    "-fx-background-color: transparent;" +
                    " -fx-cursor: hand;");
        });

        return button;
    }

    /**
     * Reusing SimpleMenuScreen for create retro text
     */
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

    // public method for Main to call
    public void setOnNormalModeSelected(Runnable action) {
        this.onNormalModeSelected = action;
    }

    public void setOnFunnyModeSelected(Runnable action) {
        this.onFunnyModeSelected = action;
    }

    public void setOnBackSelected(Runnable action) { // << 5. TẠO PHƯƠNG THỨC SETTER
        this.onBackSelected = action;
    }
}



