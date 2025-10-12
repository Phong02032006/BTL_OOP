package Arkanoid.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * SpriteManager đơn giản:
 * Load ảnh từ resources
 * Cache ảnh để không load nhiều lần
 */
public class SpriteManager {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String path) {
        if (!cache.containsKey(path)) {
            Image img = new Image(SpriteManager.class.getResourceAsStream(path));
            cache.put(path, img);
        }
        return cache.get(path);
    }
}
