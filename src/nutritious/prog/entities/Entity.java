package nutritious.prog.entities;

import nutritious.prog.main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex;
    protected int state;
    protected float airSpeed;
    protected float walkSpeed;
    protected boolean inAir = false;

    protected int maxHealth;
    protected int currentHealth;

    protected Rectangle2D.Float attackBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void drawHitbox(Graphics graphics, int xLvlOffset) {
        //for debugging hitbox
        graphics.setColor(Color.RED);
        graphics.drawRect((int)hitbox.x - xLvlOffset,(int)hitbox.y,(int) hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        //we set hitbox to bo exact same as entity model
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

//    protected void updateHitbox() {
//        hitbox.x = (int)x;
//        hitbox.y = (int)y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
