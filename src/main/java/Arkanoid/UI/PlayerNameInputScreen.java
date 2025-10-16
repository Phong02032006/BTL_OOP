package Arkanoid.UI;

import Arkanoid.util.Constant;
import Arkanoid.util.HighScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



/**
 * Màn hình nhập tên người chơi khi đạt điểm cao
 */
public class PlayerNameInputScreen extends VBox {
    
    private TextField nameTextField;
    private Button submitButton;
    private Button skipButton;
    private Text messageText;
    
    private int playerScore;
    private Runnable onSubmit;
    private Runnable onSkip;
    
    public PlayerNameInputScreen(int score) {
        this.playerScore = score;
        setupUI();
        setupEventHandlers();
    }
    
    private void setupUI() {
        // Thiết lập VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(40));
        this.setPrefSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        
        // Background đen với hiệu ứng overlay
        BackgroundFill backgroundFill = new BackgroundFill(
            Color.color(0, 0, 0, 0.8),
            CornerRadii.EMPTY,
            Insets.EMPTY
        );
        this.setBackground(new Background(backgroundFill));
        
        // Container chính
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(50));
        mainContainer.setMaxWidth(600);
        
        // Background cho container
        BackgroundFill containerFill = new BackgroundFill(
            Color.color(0.1, 0.1, 0.1, 0.9),
            new CornerRadii(10),
            Insets.EMPTY
        );
        mainContainer.setBackground(new Background(containerFill));
        
        // Border
        BorderStroke borderStroke = new BorderStroke(
            Color.WHITE,
            BorderStrokeStyle.SOLID,
            new CornerRadii(10),
            new BorderWidths(2)
        );
        mainContainer.setBorder(new Border(borderStroke));
        
        // Tiêu đề
        VBox titleSection = createTitleSection();
        
        // Phần nhập tên
        VBox inputSection = createInputSection();
        
        // Nút điều khiển
        VBox controlSection = createControlSection();
        
        mainContainer.getChildren().addAll(titleSection, inputSection, controlSection);
        this.getChildren().add(mainContainer);
    }
    
    private VBox createTitleSection() {
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        
        Text congratsText = new Text("CHÚC MỪNG!");
        congratsText.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        congratsText.setFill(Color.GOLD);
        congratsText.setStroke(Color.ORANGE);
        congratsText.setStrokeWidth(1);
        
        Text scoreText = new Text("ĐIỂM CỦA BẠN: " + playerScore);
        scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        scoreText.setFill(Color.WHITE);
        
        Text instructionText = new Text("Bạn đã lọt vào TOP 10!");
        instructionText.setFont(Font.font("Courier New", FontWeight.NORMAL, 18));
        instructionText.setFill(Color.LIGHTBLUE);
        
        Text namePromptText = new Text("Nhập tên của bạn:");
        namePromptText.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));
        namePromptText.setFill(Color.WHITE);
        
        titleBox.getChildren().addAll(congratsText, scoreText, instructionText, namePromptText);
        return titleBox;
    }
    
    private VBox createInputSection() {
        VBox inputBox = new VBox(15);
        inputBox.setAlignment(Pos.CENTER);
        
        // TextField để nhập tên
        nameTextField = new TextField();
        nameTextField.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        nameTextField.setPrefWidth(300);
        nameTextField.setPrefHeight(40);
        nameTextField.setPromptText("Nhập tên...");
        
        // Styling cho TextField
        nameTextField.setStyle(
            "-fx-background-color: #000000;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-border-color: #ffffff;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 5px 10px;"
        );
        
        // Focus styling
        nameTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                nameTextField.setStyle(
                    "-fx-background-color: #000000;" +
                    "-fx-text-fill: #ffffff;" +
                    "-fx-border-color: #0066ff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-padding: 5px 10px;"
                );
            } else {
                nameTextField.setStyle(
                    "-fx-background-color: #000000;" +
                    "-fx-text-fill: #ffffff;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-padding: 5px 10px;"
                );
            }
        });
        
        // Message text
        messageText = new Text();
        messageText.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        messageText.setFill(Color.RED);
        messageText.setVisible(false);
        
        inputBox.getChildren().addAll(nameTextField, messageText);
        return inputBox;
    }
    
    private VBox createControlSection() {
        VBox controlBox = new VBox(15);
        controlBox.setAlignment(Pos.CENTER);
        
        // Nút Submit
        submitButton = createControlButton("XÁC NHẬN", true);
        submitButton.setOnAction(e -> submitScore());
        
        // Nút Skip
        skipButton = createControlButton("BỎ QUA", false);
        skipButton.setOnAction(e -> {
            if (onSkip != null) onSkip.run();
        });
        
        controlBox.getChildren().addAll(submitButton, skipButton);
        return controlBox;
    }
    
    private Button createControlButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        button.setPrefWidth(200);
        button.setPrefHeight(45);
        
        if (isPrimary) {
            button.setStyle(
                "-fx-text-fill: #000000;" +
                "-fx-background-color: #0066ff;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            );
        }
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            if (isPrimary) {
                button.setStyle(
                    "-fx-text-fill: #000000;" +
                    "-fx-background-color: #0088ff;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-cursor: hand;"
                );
            } else {
                button.setStyle(
                    "-fx-text-fill: #000000;" +
                    "-fx-background-color: #ffffff;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (isPrimary) {
                button.setStyle(
                    "-fx-text-fill: #000000;" +
                    "-fx-background-color: #0066ff;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-cursor: hand;"
                );
            } else {
                button.setStyle(
                    "-fx-text-fill: #ffffff;" +
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2px;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Xử lý Enter key trong TextField
        nameTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitScore();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                if (onSkip != null) onSkip.run();
            }
        });
        
        // Tự động focus vào TextField khi màn hình hiển thị
        nameTextField.requestFocus();
    }
    
    private void submitScore() {
        String playerName = nameTextField.getText().trim();
        
        // Kiểm tra tên hợp lệ
        if (playerName.isEmpty()) {
            showMessage("Vui lòng nhập tên trước khi xác nhận!");
            return;
        }
        
        if (playerName.length() > 15) {
            showMessage("Tên không được quá 15 ký tự!");
            return;
        }
        
        // Kiểm tra ký tự không hợp lệ
        if (!playerName.matches("[A-Za-z0-9\\s]+")) {
            showMessage("Tên chỉ được chứa chữ cái, số và khoảng trắng!");
            return;
        }
        
        // Chuyển thành uppercase và loại bỏ khoảng trắng thừa
        playerName = playerName.toUpperCase().replaceAll("\\s+", " ");
        
        // Thêm điểm vào HighScoreManager
        boolean isHighScore = HighScoreManager.addHighScore(playerName, playerScore);
        
        if (isHighScore) {
            System.out.println("Đã thêm điểm cao: " + playerName + " - " + playerScore);
            if (onSubmit != null) onSubmit.run();
        } else {
            showMessage("Có lỗi xảy ra khi lưu điểm!");
        }
    }
    
    private void showMessage(String message) {
        messageText.setText(message);
        messageText.setVisible(true);
    }
    
    public void setOnSubmit(Runnable action) {
        this.onSubmit = action;
    }
    
    public void setOnSkip(Runnable action) {
        this.onSkip = action;
    }
}
