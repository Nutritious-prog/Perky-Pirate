package nutritious.prog.objects;

import nutritious.prog.main.Game;

import static nutritious.prog.utils.Constants.ObjectConstants.BOX;

//boxes and barrels
public class BoxContainer extends GameObject{
    public BoxContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    private void createHitbox() {
        if(objectType == BOX) {
            initHitbox(25, 18);
            xDrawOffset = (int)(7 * Game.SCALE);
            yDrawOffset = (int)(12 * Game.SCALE);
        } else {
            initHitbox(23,25);
            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(5 * Game.SCALE);
        }

        //this line moves the barrels to the floor
        //and this 2 is here because it evens the difference between hit box and actual tile size
        //the sprite is 30 pixels high and the tile is 32 pixels
        hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;
    }

    public void update() {
        //we update our Container only if we need to play an animation (when we brake it)
        if(playAnimation) {
            updateAnimationTick();
        }
    }
}
