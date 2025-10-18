package arkanoid.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Load image from resources
 * Cache to not load multiple times
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
