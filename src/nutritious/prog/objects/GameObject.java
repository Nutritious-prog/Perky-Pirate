package nutritious.prog.objects;

import nutritious.prog.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static nutritious.prog.utils.Constants.EntityConstants.ANI_TIME;
import static nutritious.prog.utils.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean playAnimation;                //potions will animate infinitely but barrels will animate only while destroyed
    protected boolean active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if(animationTick >= ANI_TIME) {
            animationTick = 0;
            animationIndex++;
        }
        if(animationIndex >= GetSpriteAmount(objectType)) {
            animationIndex = 0;
            if(objectType == BARREL || objectType == BOX) {
                playAnimation = false;
                active = false;
            }
        }
    }

    public void reset() {
        animationIndex = 0;
        animationTick = 0;
        active = true;

        if(objectType == BARREL || objectType == BOX) {
            playAnimation = false;
        } else {
            playAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        //we set hitbox to bo exact same as entity model
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public void drawHitbox(Graphics graphics, int xLvlOffset) {
        //for debugging hitbox
        graphics.setColor(Color.RED);
        graphics.drawRect((int)hitbox.x - xLvlOffset,(int)hitbox.y,(int) hitbox.width, (int)hitbox.height);
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public int getXDrawOffset() {
        return xDrawOffset;
    }

    public int getYDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimation(boolean playAnimation) {
        this.playAnimation = playAnimation;
    }
}
