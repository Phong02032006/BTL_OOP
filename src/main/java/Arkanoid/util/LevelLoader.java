package Arkanoid.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Arkanoid.Object.*;

public class LevelLoader {

    public static List<Brick> loadLevel(String fileName, double brickWidth, double brickHeight) {
        List<Brick> bricks = new ArrayList<>();

        try {
            InputStream is = LevelLoader.class.getResourceAsStream("/levels/" + fileName);
            if (is == null) {
                System.err.println("Không tìm thấy level: " + fileName);
                return bricks;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            List<String> lines = new ArrayList<>();
            String line;


            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            br.close();

            if (lines.isEmpty()) return bricks;


            int numRows = lines.size();
            int numCols = lines.getFirst().length();

            double totalWidth = numCols * brickWidth;

            double offsetX = (Constant.SCREEN_WIDTH - totalWidth) / 2;
            double offsetY = 60;


            // Duyệt từng dòng ký tự và tạo gạch
            for (int row = 0; row < numRows; row++) {
                String currentLine = lines.get(row);
                for (int col = 0; col < currentLine.length(); col++) {
                    char c = currentLine.charAt(col);

                    double x = offsetX + col * brickWidth;
                    double y = offsetY + row * brickHeight;

                    Brick brick = null;

                    switch (c) {
                        case 'N':
                            brick = new NormalBrick(x, y, brickWidth, brickHeight);
                            break;
                        case 'S':
                            brick = new StrongBrick(x, y, brickWidth, brickHeight, 2);
                            break;
                        case 'U':
                            brick = new UnbreakableBrick(x, y, brickWidth, brickHeight);
                            break;
                        case '.':
                        default:
                            break;
                    }

                    if (brick != null) {
                        if (!brick.isUnbreakable() && Math.random() < 0.2) {
                            double px = x + brickWidth / 2 - Constant.POWERUP_SIZE / 2;
                            double py = y + brickHeight / 2 - Constant.POWERUP_SIZE / 2;

                            PowerUp powerUp;
                            if (Math.random() < 0.5) {
                                powerUp = new ExpandedPaddlePowerUp(px, py);
                            } else {
                                powerUp = new FastBallPowerUp(px, py);
                            }

                            brick.setPowerUp(powerUp);
                        }

                        bricks.add(brick);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bricks;
    }
}