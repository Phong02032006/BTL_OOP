package Arkanoid.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer bgMusic;

    /**
     * phát nhạc nền
     */
    public static void playBackground() {
        try {
            if (bgMusic == null) {
                URL resource = SoundManager.class.getResource("/sounds/bg_music.mp3");
                if (resource == null) {
                    System.err.println("Không tìm thấy file nhạc nền!");
                    return;
                }
                Media media = new Media(resource.toExternalForm());
                bgMusic = new MediaPlayer(media);
                bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
                bgMusic.setVolume(0.5); // Âm lượng 50%
            }
            bgMusic.play();
            System.out.println("Nhạc nền đang phát...");
        } catch (Exception e) {
            System.err.println("Không thể phát nhạc nền: " + e.getMessage());
        }
    }

    /**
     * dừng nhạc nền
     */
    public static void stopBackground() {
        if (bgMusic != null) {
            bgMusic.stop();
            System.out.println("Dừng nhạc nền.");
        }
    }

    /**
     * phát hiệu ứng khi va chạm
     */
    public static void playSound(String fileName) {
        try {
            URL resource = SoundManager.class.getResource("/sounds/" + fileName);
            if (resource == null) {
                System.err.println("⚠️ Không tìm thấy file âm thanh: " + fileName);
                return;
            }
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.play();
        } catch (Exception e) {
            System.err.println("Không thể phát âm thanh: " + fileName);
            e.printStackTrace();
        }
    }
}
