package nutritious.prog.objects;

import nutritious.prog.gameStates.Playing;
import nutritious.prog.levels.Level;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static nutritious.prog.utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<BoxContainer> boxContainers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();

        potions = new ArrayList<>();
        boxContainers = new ArrayList<>();
    }

    public void loadObjects(Level newLevel) {
        potions = newLevel.getPotions();
        boxContainers = newLevel.getBoxContainers();
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTIONS_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++)
            for (int i = 0; i < potionImages[j].length; i++)
                potionImages[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.DESTROYABLE_OBJECTS_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int j = 0; j < containerImages.length; j++)
            for (int i = 0; i < containerImages[j].length; i++)
                containerImages[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
    }

    public void update() {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (BoxContainer bc : boxContainers)
            if (bc.isActive())
                bc.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (BoxContainer bc : boxContainers)
            if (bc.isActive()) {
                int type = 0;
                if (bc.getObjectType() == BARREL)
                    type = 1;
                g.drawImage(containerImages[type][bc.getAnimationIndex()],
                        (int) (bc.getHitbox().x - bc.getXDrawOffset() - xLvlOffset),
                        (int) (bc.getHitbox().y - bc.getYDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjectType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImages[type][p.getAnimationIndex()],
                        (int) (p.getHitbox().x - p.getXDrawOffset() - xLvlOffset),
                        (int) (p.getHitbox().y - p.getYDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        for (Potion p : potions)
            p.reset();

        for (BoxContainer gc : boxContainers)
            gc.reset();
    }

}
