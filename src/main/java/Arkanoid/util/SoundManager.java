package Arkanoid.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer bgMusic;
    private static double musicVolume = 0.5;
    private static double soundVolume = 1.0;

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
                bgMusic.setVolume(musicVolume); // Âm lượng theo cài đặt
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
            clip.setVolume(soundVolume);
            clip.play();
        } catch (Exception e) {
            System.err.println("Không thể phát âm thanh: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập âm lượng nhạc nền
     * @param volume Âm lượng từ 0.0 đến 1.0
     */
    public static void setMusicVolume(double volume) {
        musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (bgMusic != null) {
            bgMusic.setVolume(musicVolume);
        }
        System.out.println("Đã thiết lập âm lượng nhạc nền: " + (musicVolume * 100) + "%");
    }

    /**
     * Thiết lập âm lượng hiệu ứng
     * @param volume Âm lượng từ 0.0 đến 1.0
     */
    public static void setSoundVolume(double volume) {
        soundVolume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("Đã thiết lập âm lượng hiệu ứng: " + (soundVolume * 100) + "%");
    }

    /**
     * Lấy âm lượng nhạc nền hiện tại
     */
    public static double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Lấy âm lượng hiệu ứng hiện tại
     */
    public static double getSoundVolume() {
        return soundVolume;
    }

    /**
     * Khởi tạo SoundManager với cài đặt từ SettingsManager
     */
    public static void initialize() {
        double[] settings = SettingsManager.loadSettings();
        if (settings != null) {
            musicVolume = settings[0];
            soundVolume = settings[1];
            System.out.println("Đã khởi tạo SoundManager với cài đặt đã lưu");
        } else {
            System.out.println("Đã khởi tạo SoundManager với cài đặt mặc định");
        }
    }
}
