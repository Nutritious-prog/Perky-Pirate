package nutritious.prog.objects;

import nutritious.prog.entities.Player;
import nutritious.prog.gameStates.Playing;
import nutritious.prog.levels.Level;
import nutritious.prog.main.Game;
import nutritious.prog.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static nutritious.prog.utils.Constants.ObjectConstants.*;
import static nutritious.prog.utils.Constants.Projectiles.*;
import static nutritious.prog.utils.HelperMethods.CanCannonSeePlayer;
import static nutritious.prog.utils.HelperMethods.IsProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private BufferedImage[] cannonImages;
    private BufferedImage spikesImage, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<BoxContainer> boxContainers;
    private ArrayList<Spikes> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();              //the level doesn't determine how many projectiles we have, so we can initialise it like that

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
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImages() {
        //POTIONS
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTIONS_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++)
            for (int i = 0; i < potionImages[j].length; i++)
                potionImages[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        //BOXES
        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.DESTROYABLE_OBJECTS_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int j = 0; j < containerImages.length; j++)
            for (int i = 0; i < containerImages[j].length; i++)
                containerImages[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        //SPIKES
        spikesImage = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        //CANNONS
        cannonImages = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for(int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = temp.getSubimage(i * 40, 0,40, 26);
        }

        //CANNON BALLS
        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (BoxContainer bc : boxContainers)
            if (bc.isActive())
                bc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-CANNON_BALL_DMG);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int distanceBetweenPlayerAndCannon = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return distanceBetweenPlayerAndCannon <= Game.TILES_SIZE * 5;
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjectType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;
        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.playAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInFrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
                                c.setAnimation(true);
                            }
            c.update();
            //cannonball should be shot at certain animation frame
            //animation tick condition is needed for cannon to shoot only once (one animation takes some time and cannon will shoot huge amount of balls during this time)
            if(c.getAnimationIndex() == 4 && c.getAnimationTick() == 0) {
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c) {
        //depending on cannons direction the projectile will go certain way
        int direction = c.getObjectType() == CANNON_LEFT ? -1 : 1;
        projectiles.add(new Projectile((int)(c.getHitbox().x), (int)(c.getHitbox().y),direction ));
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawSpikes(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for(Cannon c : cannons) {
            int x = (int)(c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            //we mirror the image if it is facing to the other side
            if(c.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImages[c.getAnimationIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
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

        for (Cannon c : cannons)
            c.reset();
    }

}
