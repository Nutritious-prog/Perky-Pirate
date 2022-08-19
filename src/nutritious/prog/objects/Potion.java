package nutritious.prog.objects;

import nutritious.prog.main.Game;

public class Potion extends GameObject{
    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        playAnimation = true;                           //potions animation is infinite
        initHitbox(7,14);
        xDrawOffset = (int)(3 * Game.SCALE);            //sprites offset (between the beginning of  sprite to actual image of potion)
        yDrawOffset = (int)(2 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
    }
}
