package nutritious.prog.objects;

import java.awt.geom.Rectangle2D;

import static nutritious.prog.utils.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int direction;              //if it is 1 projectile is going to the right, if it is -1 projectile will go to the left
    private boolean active = true;

    public Projectile(int x, int y, int direction) {
        hitbox = new Rectangle2D.Float(x, y, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.direction = direction;
    }

    public void updatePos() {
        hitbox.x += (int)(direction * SPEED);
    }

    //for resetting purposes
    public void setPosition(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
