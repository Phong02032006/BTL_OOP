package arkanoid.ui;

import arkanoid.util.Constant;
import arkanoid.util.SoundManager;
import arkanoid.util.SettingsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Màn hình Settings cho phép điều chỉnh âm lượng game và nhạc nền
 */
public class SettingsScreen extends VBox {

    private Slider musicVolumeSlider;
    private Slider soundEffectsSlider;
    private Label musicVolumeLabel;
    private Label soundEffectsLabel;
    private Button backButton;
    private Button resetButton;

    private Runnable onBack;

    // Âm lượng hiện tại
    private double currentMusicVolume = 0.5;
    private double currentSoundVolume = 1.0;

    public SettingsScreen() {
        setupUI();
        loadSettings();
    }

    private void setupUI() {
        // Thiết lập VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
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

        // Phần cài đặt âm lượng
        VBox settingsSection = createSettingsSection();

        // Nút điều khiển
        VBox controlSection = createControlSection();

        this.getChildren().addAll(titleSection, settingsSection, controlSection);
    }

    private VBox createTitleSection() {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        Text settingsTitle = new Text("CÀI ĐẶT");
        settingsTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
        settingsTitle.setFill(Color.WHITE);
        settingsTitle.setStroke(Color.BLUE);
        settingsTitle.setStrokeWidth(2);

        // Hiệu ứng 3D/pixel art
        settingsTitle.setStyle(
                "-fx-effect: dropshadow(gaussian, #0066ff, 4, 0.8, 2, 2);" +
                        "-fx-effect: dropshadow(gaussian, #ffffff, 2, 1.0, 0, 0);"
        );

        titleBox.getChildren().add(settingsTitle);
        return titleBox;
    }

    private VBox createSettingsSection() {
        VBox settingsBox = new VBox(40);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setPadding(new Insets(20, 100, 20, 100));

        // Âm lượng nhạc nền
        VBox musicSection = createVolumeControl(
                "ÂM LƯỢNG NHẠC NỀN",
                "music",
                0.5
        );

        // Âm lượng hiệu ứng
        VBox soundSection = createVolumeControl(
                "ÂM LƯỢNG HIỆU ỨNG",
                "sound",
                1.0
        );

        settingsBox.getChildren().addAll(musicSection, soundSection);
        return settingsBox;
    }

    private VBox createVolumeControl(String title, String type, double defaultValue) {
        VBox controlBox = new VBox(15);
        controlBox.setAlignment(Pos.CENTER);

        // Tiêu đề
        Text titleText = createRetroText(title, Color.ORANGE, 18, true);
        controlBox.getChildren().add(titleText);

        // Slider và label
        HBox sliderContainer = new HBox(20);
        sliderContainer.setAlignment(Pos.CENTER);

        Label volumeLabel;
        Slider slider;

        if ("music".equals(type)) {
            volumeLabel = musicVolumeLabel = createVolumeLabel();
            slider = musicVolumeSlider = createVolumeSlider(defaultValue);
            musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                currentMusicVolume = newVal.doubleValue();
                updateMusicVolume(currentMusicVolume);
                musicVolumeLabel.setText(String.format("%.0f%%", currentMusicVolume * 100));
            });
        } else {
            volumeLabel = soundEffectsLabel = createVolumeLabel();
            slider = soundEffectsSlider = createVolumeSlider(defaultValue);
            soundEffectsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                currentSoundVolume = newVal.doubleValue();
                updateSoundVolume(currentSoundVolume);
                soundEffectsLabel.setText(String.format("%.0f%%", currentSoundVolume * 100));
            });
        }

        // Test button cho âm thanh
        Button testButton = createTestButton();
        testButton.setOnAction(e -> {
            SoundManager.playSound("hit_paddle.wav");
        });

        sliderContainer.getChildren().addAll(
                createRetroText("0%", Color.WHITE, 14, false),
                slider,
                createRetroText("100%", Color.WHITE, 14, false),
                volumeLabel,
                testButton
        );

        controlBox.getChildren().add(sliderContainer);
        return controlBox;
    }

    private Label createVolumeLabel() {
        Label label = new Label("50%");
        label.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        label.setMinWidth(50);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Slider createVolumeSlider(double defaultValue) {
        Slider slider = new Slider(0, 1, defaultValue);
        slider.setPrefWidth(200);
        slider.setMajorTickUnit(0.25);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(false);

        // Styling cho slider
        slider.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-control-inner-background: #333333;" +
                        "-fx-accent: #0066ff;"
        );

        return slider;
    }

    private Button createTestButton() {
        Button button = new Button("TEST");
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        button.setPrefWidth(60);
        button.setPrefHeight(30);
        button.setStyle(
                "-fx-text-fill: #ffffff;" +
                        "-fx-background-color: #0066ff;" +
                        "-fx-border-color: #ffffff;" +
                        "-fx-border-width: 1px;" +
                        "-fx-cursor: hand;"
        );

        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: #0088ff;" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 1px;" +
                            "-fx-cursor: hand;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-text-fill: #ffffff;" +
                            "-fx-background-color: #0066ff;" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 1px;" +
                            "-fx-cursor: hand;"
            );
        });

        return button;
    }

    private VBox createControlSection() {
        VBox controlBox = new VBox(20);
        controlBox.setAlignment(Pos.CENTER);

        // Nút Reset về mặc định
        resetButton = createControlButton("RESET VỀ MẶC ĐỊNH");
        resetButton.setOnAction(e -> resetToDefaults());

        // Nút Quay lại
        backButton = createControlButton("QUAY LẠI");
        backButton.setOnAction(e -> {
            saveSettings();
            if (onBack != null) onBack.run();
        });

        controlBox.getChildren().addAll(resetButton, backButton);
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
     * Cập nhật âm lượng nhạc nền
     */
    private void updateMusicVolume(double volume) {
        SoundManager.setMusicVolume(volume);
    }

    /**
     * Cập nhật âm lượng hiệu ứng
     */
    private void updateSoundVolume(double volume) {
        SoundManager.setSoundVolume(volume);
    }

    /**
     * Reset về cài đặt mặc định
     */
    private void resetToDefaults() {
        currentMusicVolume = 0.5;
        currentSoundVolume = 1.0;

        musicVolumeSlider.setValue(currentMusicVolume);
        soundEffectsSlider.setValue(currentSoundVolume);

        musicVolumeLabel.setText("50%");
        soundEffectsLabel.setText("100%");

        updateMusicVolume(currentMusicVolume);
        updateSoundVolume(currentSoundVolume);

        System.out.println("Đã reset về cài đặt mặc định");
    }

    /**
     * Lưu cài đặt vào file
     */
    private void saveSettings() {
        SettingsManager.saveSettings(currentMusicVolume, currentSoundVolume);
        System.out.println("Đã lưu cài đặt");
    }

    /**
     * Tải cài đặt từ file
     */
    private void loadSettings() {
        double[] settings = SettingsManager.loadSettings();
        if (settings != null) {
            currentMusicVolume = settings[0];
            currentSoundVolume = settings[1];

            musicVolumeSlider.setValue(currentMusicVolume);
            soundEffectsSlider.setValue(currentSoundVolume);

            musicVolumeLabel.setText(String.format("%.0f%%", currentMusicVolume * 100));
            soundEffectsLabel.setText(String.format("%.0f%%", currentSoundVolume * 100));

            updateMusicVolume(currentMusicVolume);
            updateSoundVolume(currentSoundVolume);
        }
    }

    public void setOnBack(Runnable action) {
        this.onBack = action;
    }
}
