package nutritious.prog.utils;

import nutritious.prog.entities.Crabby;
import nutritious.prog.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static nutritious.prog.utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {
    public static final String GAME_ICON = "pirate_icon.png";
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
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
    public static final String LEVEL_COMPLETED_BACKGROUND = "level_completed_background.png";
    public static final String POTIONS_ATLAS = "potions_sprites.png";
    public static final String DESTROYABLE_OBJECTS_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";

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


    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/levelsResources");
        File folder = null;

        //retrieving folder with levels form res folder
        try {
            folder = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //list of all files in the folder
        File[] files = folder.listFiles();
        File[] filesSorted = new File[files.length];

        //sorting files to be in order
        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals("level_" + (i + 1) + ".png"))
                    filesSorted[i] = files[j];

            }

        BufferedImage[] levelImages = new BufferedImage[filesSorted.length];

        //reading images from sorted files
        for (int i = 0; i < levelImages.length; i++)
            try {
                levelImages[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return levelImages;
    }

}
