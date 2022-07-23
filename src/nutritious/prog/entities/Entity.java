package nutritious.prog.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics graphics) {
        //for debugging hitbox
        graphics.setColor(Color.RED);
        graphics.drawRect((int)hitbox.x,(int)hitbox.y,(int) hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(float x, float y, int width, int height) {
        //we set hitbox to bo exact same as entity model
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

//    protected void updateHitbox() {
//        hitbox.x = (int)x;
//        hitbox.y = (int)y;
//    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
