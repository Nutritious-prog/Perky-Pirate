package nutritious.prog.levels;

import nutritious.prog.entities.Crabby;
import nutritious.prog.main.Game;
import nutritious.prog.objects.BoxContainer;
import nutritious.prog.objects.Potion;
import nutritious.prog.utils.HelperMethods;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static nutritious.prog.utils.HelperMethods.GetCrabs;
import static nutritious.prog.utils.HelperMethods.GetLevelData;
import static nutritious.prog.utils.HelperMethods.GetPlayerSpawn;

public class Level {
    private BufferedImage levelImg;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<BoxContainer> boxContainers;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage levelImg) {
        this.levelImg = levelImg;
        createLevelData();
        createEnemies();
        createPotions();
        createBoxContainers();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createBoxContainers() {
        boxContainers = HelperMethods.GetContainers(levelImg);
    }

    private void createPotions() {
        potions = HelperMethods.GetPotions(levelImg);
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(levelImg);
    }

    private void calcLvlOffsets() {
        //we only want to move our level as far as it has its boundaries,
        //we don't want to exceed them and go into darkness,
        //so we create max offset our level can go
        lvlTilesWide = levelImg.getWidth();
        //the offset is calculated by subtracting our visible game part from the
        //whole levels length
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        //conversion to pixels
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(levelImg);
    }

    private void createLevelData() {
        lvlData = GetLevelData(levelImg);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<BoxContainer> getBoxContainers() {
        return boxContainers;
    }
}
