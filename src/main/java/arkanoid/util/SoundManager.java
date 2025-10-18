package arkanoid.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer bgMusic;
    private static double musicVolume = 0.5;
    private static double soundVolume = 1.0;

    /**
     * bg.
     */
    public static void playBackground() {
        try {
            if (bgMusic == null) {
                URL resource = SoundManager.class.getResource("/sounds/bg_music.mp3");
                if (resource == null) {
                    System.err.println("Cant find the file");
                    return;
                }
                Media media = new Media(resource.toExternalForm());
                bgMusic = new MediaPlayer(media);
                bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
                bgMusic.setVolume(musicVolume); // Âm lượng theo cài đặt
            }
            bgMusic.play();
            System.out.println("Bg music playing");
        } catch (Exception e) {
            System.err.println("Cant play music " + e.getMessage());
        }
    }

    /**
     *
     */
    public static void stopBackground() {
        if (bgMusic != null) {
            bgMusic.stop();
            System.out.println("Stop bg music");
        }
    }

    /**
     * sfx when collide.
     */
    public static void playSound(String fileName) {
        try {
            URL resource = SoundManager.class.getResource("/sounds/" + fileName);
            if (resource == null) {
                System.err.println("Cant find the file " + fileName);
                return;
            }
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(soundVolume);
            clip.play();
        } catch (Exception e) {
            System.err.println("Cant play sound " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * set bg music.
     *
     * @param volume from 0.0 to 1.0
     */
    public static void setMusicVolume(double volume) {
        musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (bgMusic != null) {
            bgMusic.setVolume(musicVolume);
        }
    }

    /**
     * sound setting.
     *
     * @param volume from 0.0 t 1.0
     */
    public static void setSoundVolume(double volume) {
        soundVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static double getSoundVolume() {
        return soundVolume;
    }

    /**
     * initialize sound from settings.
     */
    public static void initialize() {
        double[] settings = SettingsManager.loadSettings();
        if (settings != null) {
            musicVolume = settings[0];
            soundVolume = settings[1];
            System.out.println("SoundManager initialized with saved settings.");
        } else {
            System.out.println("SoundManager initialized with default settings.");
        }
    }
}
