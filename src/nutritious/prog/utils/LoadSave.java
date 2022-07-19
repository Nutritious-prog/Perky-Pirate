package nutritious.prog.utils;

import nutritious.prog.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_ATLAS = "level_one_data.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    //displaying level as a two-dimensional array
    public static int[][] GetLevelData() {
        int[][] levelData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_ATLAS);

        for(int j = 0; j < img.getHeight(); j++) {
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();  // this color will represent index of a sprite in an array
                if(value >= 48) value = 0;  // checking if value is not bigger than our sprites array
                levelData[j][i] =value;
            }
        }
        return levelData;
    }
}