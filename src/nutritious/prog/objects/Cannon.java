package nutritious.prog.objects;

import nutritious.prog.main.Game;

import static nutritious.prog.utils.Constants.ObjectConstants.*;

public class Cannon extends GameObject{
    private int tileY;

    public Cannon(int x, int y, int objectType) {
        super(x, y, objectType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(CANNON_WIDTH_DEFAULT,CANNON_HEIGHT_DEFAULT);
        //to make sure the cannon is in the center of a tile we place it on
        hitbox.x -= (int)(4 * Game.SCALE);
        hitbox.y += (int)(6 * Game.SCALE);
    }

    public void update() {
        if(playAnimation) {
            updateAnimationTick();
        }
    }

    public int getTileY() {
        return tileY;
    }
}
