package nutritious.prog.objects;

import nutritious.prog.main.Game;

import java.awt.geom.Rectangle2D;

import static nutritious.prog.utils.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int direction;              //if it is 1 projectile is going to the right, if it is -1 projectile will go to the left
    private boolean active = true;

    public Projectile(int x, int y, int direction) {
        //projectile spawn position fix (to spawn in the cannon not on top of it)
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (direction == 1)
            xOffset = (int) (29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
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
