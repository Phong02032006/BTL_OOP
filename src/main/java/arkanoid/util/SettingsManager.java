package arkanoid.util;

import java.io.*;
import java.util.Properties;

/**
 * Setting and saved manager
 */
public class SettingsManager {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String MUSIC_VOLUME_KEY = "music.volume";
    private static final String SOUND_VOLUME_KEY = "sound.volume";

    /**
     * Setting saved to file
     *
     * @param musicVolume BG volume (0.0 - 1.0)
     * @param soundVolume Effect volume (0.0 - 1.0)
     */
    public static void saveSettings(double musicVolume, double soundVolume) {
        try {
            Properties props = new Properties();
            props.setProperty(MUSIC_VOLUME_KEY, String.valueOf(musicVolume));
            props.setProperty(SOUND_VOLUME_KEY, String.valueOf(soundVolume));

            // Create file in path user.home
            String userHome = System.getProperty("user.home");
            String settingsDir = userHome + File.separator + ".arkanoid";
            File dir = new File(settingsDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File settingsFile = new File(dir, SETTINGS_FILE);

            try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
                props.store(fos, "Arkanoid Game Settings");
                System.out.println("Cài đặt đã được lưu vào: " + settingsFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Không thể lưu cài đặt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Upload setting from file
     *
     * @return Array [musicVolume, soundVolume] or null if can't upload
     */
    public static double[] loadSettings() {
        try {
            String userHome = System.getProperty("user.home");
            String settingsDir = userHome + File.separator + ".arkanoid";
            File settingsFile = new File(settingsDir, SETTINGS_FILE);

            if (!settingsFile.exists()) {
                System.out.println("File cài đặt chưa tồn tại, sử dụng cài đặt mặc định");
                return null;
            }

            Properties props = new Properties();

            try (FileInputStream fis = new FileInputStream(settingsFile)) {
                props.load(fis);

                double musicVolume = Double.parseDouble(props.getProperty(MUSIC_VOLUME_KEY, "0.5"));
                double soundVolume = Double.parseDouble(props.getProperty(SOUND_VOLUME_KEY, "1.0"));

                // Đảm bảo giá trị trong khoảng hợp lệ
                musicVolume = Math.max(0.0, Math.min(1.0, musicVolume));
                soundVolume = Math.max(0.0, Math.min(1.0, soundVolume));

                System.out.println("Đã tải cài đặt: Music=" + musicVolume + ", Sound=" + soundVolume);
                return new double[]{musicVolume, soundVolume};
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Không thể tải cài đặt: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter
     */
    public static double getDefaultMusicVolume() {
        return 0.5;
    }

    /**
     * Getter
     */
    public static double getDefaultSoundVolume() {
        return 1.0;
    }
}
