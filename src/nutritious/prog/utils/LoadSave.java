package nutritious.prog.utils;

import nutritious.prog.entities.Crabby;
import nutritious.prog.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static nutritious.prog.utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
//    public static final String LEVEL_ONE_ATLAS = "level_one_data.png";
    public static final String LEVEL_ONE_ATLAS = "level_one_data_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BACKGROUND_IMG = "playing_bg_img.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";

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

    //this method will search through the level atlas and
    //find tiles that are marked for enemies
    //(they will have different color (more explanation in GetLevelData() method))
    public static ArrayList<Crabby> GetCrabs() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_ATLAS);
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                //we go to file and get colors of pixels
                Color color = new Color(img.getRGB(i, j));
                //if we find a color with its green value equal CRABBY (0)
                //we add crabby at that position
                int value = color.getGreen();
                if (value == CRABBY)
                    //if we find a spot to place a crabby we add it to the list
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;

    }

    //displaying level as a two-dimensional array
    public static int[][] GetLevelData() {
        //initialising two-dimensional array for storing certain level tiles
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_ATLAS);
        int[][] levelData = new int[img.getHeight()][img.getWidth()]; // the image is bigger than what we see on the screen

        //The size of the img will be the size of the lvl.
        //20 x 40 will make a lvl 20 tiles wide and 40 in height.
        //each tile type is a represented by a different color in the level_data.png type files
        //and depending on what color we get, we will print certain tile
        for(int j = 0; j < img.getHeight(); j++) {
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();  // this color will represent index of a sprite in an array
                if(value >= 48) value = 0;  // checking if value is not bigger than our sprites array
                levelData[j][i] = value;
            }
        }
        return levelData;
    }
}
