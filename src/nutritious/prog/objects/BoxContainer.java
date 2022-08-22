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
    }

    public void update() {
        //we update our Container only if we need to play an animation (when we brake it)
        if(playAnimation) {
            updateAnimationTick();
        }
    }
}
