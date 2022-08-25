package nutritious.prog.objects;

import nutritious.prog.entities.Player;
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
    private BufferedImage spikesImage;
    private ArrayList<Potion> potions;
    private ArrayList<BoxContainer> boxContainers;
    private ArrayList<Spikes> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();

        potions = new ArrayList<>();
        boxContainers = new ArrayList<>();
    }

    public void checkIfSpikesGotTouched(Player player) {
        for (Spikes s : spikes) {
            if(s.getHitbox().intersects(player.getHitbox())) {
                player.kill();
            }
        }
    }

    //this method will check if player touched the potions (if so, we change certain values - applyEffectToPlayer() method)
    public void checkIfObjectGotTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkIfObjectGotHit(Rectangle2D.Float attackbox) {
        for (BoxContainer bc : boxContainers)
            //the !playAnimation is needed to prevent player from spamming attack while the barrel is playing destroy animation
            //and therefore spawn many potions. We want one potion from one box
            if (bc.isActive() && !bc.playAnimation) {
                if (bc.getHitbox().intersects(attackbox)) {
                    //we animate containers only when they get destroyed
                    bc.setAnimation(true);
                    int type = 0;
                    //if container gets destroyed it drops one potion
                    if (bc.getObjectType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (bc.getHitbox().x + bc.getHitbox().width / 3), (int) (bc.getHitbox().y - bc.getHitbox().height / 2), type));
                    return;
                }
            }
    }

    public void loadObjects(Level newLevel) {
        //reset potions and boxes after every level reload instead of infinitely populating the arraylist
        potions = new ArrayList<>(newLevel.getPotions());
        boxContainers = new ArrayList<>(newLevel.getBoxContainers());
        //spikes are static, so we don't need the functionality like above
        spikes = newLevel.getSpikes();
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

        spikesImage = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
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
        drawSpikes(g, xLvlOffset);
    }

    private void drawSpikes(Graphics g, int xLvlOffset) {
        for(Spikes s : spikes) {
            g.drawImage(spikesImage, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getYDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
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
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion p : potions)
            p.reset();

        for (BoxContainer gc : boxContainers)
            gc.reset();
    }

}
